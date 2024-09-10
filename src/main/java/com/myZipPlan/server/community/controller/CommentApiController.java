package com.myZipPlan.server.community.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.community.domain.Comment;
import com.myZipPlan.server.community.dto.comment.*;
import com.myZipPlan.server.community.service.CommentService;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
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
        String oauthProviderId = SecurityUtils.getProviderId();
        CommentResponse commentResponse = commentService.addComment(oauthProviderId, postId, addCommentRequest);
        return ApiResponse.ok(commentResponse);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest updateCommentRequest) {
        String oauthProviderId = SecurityUtils.getProviderId();
        CommentResponse updatedComment = commentService.updateComment(oauthProviderId, commentId, updateCommentRequest);
        return ApiResponse.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        commentService.deleteComment(oauthProviderId, commentId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{commentId}/like")
    public ApiResponse<Void> likeComment(@PathVariable Long commentId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        commentService.likeComment(oauthProviderId, commentId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{commentId}/unlike")
    public ApiResponse<Void> unlikeComment(@PathVariable Long commentId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        commentService.unlikeComment(oauthProviderId, commentId);
        return ApiResponse.ok(null);
    }

    // 대댓글 작성
    @PostMapping("/{parentCommentId}/reply")
    public ApiResponse<CommentResponse> addReply(@PathVariable Long parentCommentId, @RequestBody AddReplyRequest addReplyRequest) {
        Comment reply = commentService.addReply(parentCommentId, addReplyRequest);
        CommentResponse commentResponse = CommentResponse.fromEntity(reply);
        return ApiResponse.ok(commentResponse);
    }
}
