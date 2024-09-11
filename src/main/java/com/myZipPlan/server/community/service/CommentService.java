package com.myZipPlan.server.community.service;

import com.myZipPlan.server.community.domain.Comment;
import com.myZipPlan.server.community.domain.CommentLike;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.comment.CommentCreateRequest;

import com.myZipPlan.server.community.dto.comment.CommentResponse;
import com.myZipPlan.server.community.dto.comment.CommentUpdateRequest;
import com.myZipPlan.server.community.repository.CommentLikeRepository;
import com.myZipPlan.server.community.repository.CommentRepository;
import com.myZipPlan.server.community.repository.PostRepository;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글 작성
    public CommentResponse createComment(String oauthProviderId, Long postId, CommentCreateRequest commentCreateRequest) {
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment(post, user, commentCreateRequest.getContent());
        comment = commentRepository.save(comment);
        return CommentResponse.fromEntity(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(String oauthProviderId, Long commentId, CommentUpdateRequest commentUpdateRequest) {
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUser().getOauthProviderId().equals(user.getOauthProviderId())) {
            throw new IllegalArgumentException("해당 글 작성자만 수정이 할 수 있습니다.");
        }

        comment.setContent(commentUpdateRequest.getUpdatedContent());
        comment.setLastModifiedDate(LocalDateTime.now());
        return CommentResponse.fromEntity(comment);
    }

    //댓글삭제
    public void deleteComment(String oauthProviderId, Long commentId) {
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUser().getOauthProviderId().equals(user.getOauthProviderId())) {
            throw new IllegalArgumentException("해당 글 작성자만 삭제 할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void likeComment(String oauthProviderId, Long commentId) {
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 이미 좋아요를 눌렀는지 확인
        boolean alreadyLiked = commentLikeRepository.findByCommentAndUser(comment, user).isPresent();
        if (alreadyLiked) {
            throw new IllegalArgumentException("이미 좋아요를 누른 댓글입니다.");
        }

        // 좋아요 추가
        commentLikeRepository.save(new CommentLike(comment, user));
        comment.setLikes(comment.getLikes() + 1);  // 좋아요 수 증가
        commentRepository.save(comment);
    }


    @Transactional
    public void unlikeComment(String oauthProviderId, Long commentId) {
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 좋아요를 누른 기록이 있는지 확인
        CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글에 좋아요를 누르지 않았습니다."));

        // 좋아요 제거
        commentLikeRepository.delete(commentLike);
        comment.setLikes(comment.getLikes() - 1);  // 좋아요 수 감소
        commentRepository.save(comment);
    }
}
