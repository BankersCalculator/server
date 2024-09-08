package com.myZipPlan.server.community.controller;

import com.myZipPlan.server.common.api.ApiResponse;
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
}
