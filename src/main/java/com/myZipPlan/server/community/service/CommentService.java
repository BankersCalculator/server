package com.myZipPlan.server.community.service;

import com.myZipPlan.server.community.domain.Comment;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.comment.AddCommentRequest;
import com.myZipPlan.server.community.dto.comment.CommentResponse;
import com.myZipPlan.server.community.dto.comment.UpdateCommentRequest;
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

    // 댓글 작성
    public CommentResponse addComment(Long postId, AddCommentRequest addCommentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(addCommentRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = new Comment(post, user, addCommentRequest.getContent());
        comment = commentRepository.save(comment);
        return CommentResponse.fromEntity(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUser().getId().equals(updateCommentRequest.getUserId())) {
            throw new IllegalArgumentException("Only the author can update the comment");
        }

        comment.setContent(updateCommentRequest.getUpdatedContent());
        comment.setLastModifiedDate(LocalDateTime.now());
        return CommentResponse.fromEntity(comment);
    }

    //댓글삭제
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("User is not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
