package com.myZipPlan.server.community.service;

import com.myZipPlan.server.common.enums.RoleType;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글 작성
    @Transactional
    public CommentResponse createComment(String oauthProviderId, Long postId, CommentCreateRequest commentCreateRequest) {
        User user = getUserByOauthProviderId(oauthProviderId);
        Post post = getPostById(postId);

        Comment comment = new Comment(post, user, commentCreateRequest.getContent());
        comment = commentRepository.save(comment);
        return CommentResponse.fromEntity(comment, false, "N");
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(String oauthProviderId, Long commentId, CommentUpdateRequest commentUpdateRequest) {
        User user = getUserByOauthProviderId(oauthProviderId);
        Comment comment = getCommentById(commentId);

        String authority = determineAuthority(user, comment);
        validateUserAuthority(user, comment, authority);

        comment.setContent(commentUpdateRequest.getUpdatedContent());
        comment.setLastModifiedDate(LocalDateTime.now());

        boolean like = commentLikeRepository.findByCommentAndUser(comment, user).isPresent();
        return CommentResponse.fromEntity(comment, like, authority);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(String oauthProviderId, Long commentId) {
        User user = getUserByOauthProviderId(oauthProviderId);
        Comment comment = getCommentById(commentId);

        String authority = determineAuthority(user, comment);
        validateUserAuthority(user, comment, authority);

        commentRepository.delete(comment);
    }

    @Transactional
    public void likeComment(String oauthProviderId, Long commentId) {
        User user = getUserByOauthProviderId(oauthProviderId);
        Comment comment = getCommentById(commentId);

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
        User user = userRepository.findByProviderId(oauthProviderId)
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

    @Transactional
    public List<CommentResponse> getComments(String oauthProviderId, Long postId) {
        Optional<User> optionalUser = (oauthProviderId != null)
                ? userRepository.findByProviderId(oauthProviderId)
                : Optional.empty(); // 비로그인 상태일 경우 Optional.empty()

        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
                .map(comment -> {

                    boolean like = false;
                    String authority = "N";
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        like = commentLikeRepository.findByCommentAndUser(comment, user).isPresent();
                        authority = determineAuthority(user, comment);
                    }
                    return CommentResponse.fromEntity(comment, like, authority);
                })
                .collect(Collectors.toList());
    }

    private String determineAuthority(User user, Comment comment) {
        if (isCommentOwner(user, comment)) {
            return "ALL";
        } else if (user.getRoleType() == RoleType.ADMIN) {
            return "DELETE";
        } else {
            return "N";
        }
    }

    private User getUserByOauthProviderId(String oauthProviderId) {
        return userRepository.findByProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    private Post getPostById(Long postId) {
        return postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    private boolean isCommentOwner(User user, Comment comment) {
        return comment.getUser().getProviderId().equals(user.getProviderId());
    }

    private void validateUserAuthority(User user, Comment comment, String authority) {
        if (!isCommentOwner(user, comment) && !(authority.equals("DELETE") || authority.equals("ALL"))) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
