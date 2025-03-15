package com.myZipPlan.server.community.controller;

import org.slf4j.Logger;
import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.community.dto.comment.*;
import com.myZipPlan.server.community.service.CommentService;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;
    private static final Logger log = LoggerFactory.getLogger(CommentApiController.class);


    // 댓글 작성
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{postId}")
    public ApiResponse<CommentResponse> createComment(@PathVariable Long postId, @RequestBody CommentCreateRequest commentCreateRequest) {
        String oauthProviderId = SecurityUtils.getProviderId();
        CommentResponse commentResponse = commentService.createComment(oauthProviderId, postId, commentCreateRequest);
        return ApiResponse.ok(commentResponse);
    }

    // 댓글 수정
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        String oauthProviderId = SecurityUtils.getProviderId();
        CommentResponse updatedComment = commentService.updateComment(oauthProviderId, commentId, commentUpdateRequest);
        return ApiResponse.ok(updatedComment);
    }

    // 댓글 삭제
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(@PathVariable Long commentId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        commentService.deleteComment(oauthProviderId, commentId);
        return ApiResponse.ok("댓글을 성공적으로 삭제하였습니다.");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{commentId}/like")
    public ApiResponse<String> likeComment(@PathVariable Long commentId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        commentService.likeComment(oauthProviderId, commentId);
        return ApiResponse.ok("좋아요를 성공적으로 눌렀습니다.");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{commentId}/unlike")
    public ApiResponse<String> unlikeComment(@PathVariable Long commentId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        commentService.unlikeComment(oauthProviderId, commentId);
        return ApiResponse.ok("좋아요를 성공적으로 취소하였습니다.");
    }

    @GetMapping("/{postId}")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long postId) {
        String oauthProviderId = null;
        try {
            oauthProviderId = SecurityUtils.getProviderId();
        } catch (IllegalStateException e) {
            log.info("Security Context에 인증 정보가 없습니다. 비로그인 상태로 댓글을 조회합니다.", e);
        }
        List<CommentResponse> comments = commentService.getComments(oauthProviderId, postId);
        return ApiResponse.ok(comments);
    }
}
