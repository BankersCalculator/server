package com.myZipPlan.server.community.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.community.domain.Comment;
import com.myZipPlan.server.community.dto.comment.AddCommentRequest;
import com.myZipPlan.server.community.dto.comment.CommentResponse;
import com.myZipPlan.server.community.dto.comment.UpdateCommentRequest;
import com.myZipPlan.server.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{postId}")
    public ApiResponse<CommentResponse> addComment(@PathVariable Long postId, @RequestBody AddCommentRequest addCommentRequest) {
        CommentResponse commentResponse = commentService.addComment(postId, addCommentRequest);
        return ApiResponse.ok(commentResponse);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest updateCommentRequest) {
        CommentResponse updatedComment = commentService.updateComment(commentId, updateCommentRequest);
        return ApiResponse.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId, @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ApiResponse.ok(null);
    }

    // 댓글 좋아요
    @PostMapping("/{commentId}/like")
    public ApiResponse<Void> likeComment(@PathVariable Long commentId) {
        commentService.likeComment(commentId);
        return ApiResponse.ok(null);
    }

    // 댓글 좋아요 취소
    @PostMapping("/{commentId}/unlike")
    public ApiResponse<Void> unlikeComment(@PathVariable Long commentId) {
        commentService.unlikeComment(commentId);
        return ApiResponse.ok(null);
    }

    // 대댓글 작성
    @PostMapping("/{parentCommentId}/reply")
    public ApiResponse<CommentResponse> addReply(@PathVariable Long parentCommentId, @RequestParam Long userId, @RequestBody String content) {
        Comment reply = commentService.addReply(parentCommentId, userId, content);
        CommentResponse commentResponse = CommentResponse.fromEntity(reply);
        return ApiResponse.ok(commentResponse);
    }
}
