package com.myZipPlan.server.community.controller;

import org.slf4j.Logger;
import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.common.enums.community.PostSortType;
import com.myZipPlan.server.community.dto.post.request.PostCreateRequest;
import com.myZipPlan.server.community.dto.post.request.PostUpdateRequest;
import com.myZipPlan.server.community.dto.post.response.PostResponse;
import com.myZipPlan.server.community.service.PostService;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;
    private static final Logger log = LoggerFactory.getLogger(PostApiController.class);

    private String validateAndGetOauthProviderId() {
        String oauthProviderId = getOauthProviderId();
        if (oauthProviderId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return oauthProviderId;
    }

    private String getOauthProviderId() {
        try {
            return SecurityUtils.getProviderId();
        } catch (IllegalStateException e) {
            log.info("Security Context에 인증 정보가 없습니다. 비로그인 상태로 요청을 처리합니다.", e);
            return null;
        }
    }

    @PostMapping
    public ApiResponse<PostResponse> createPost(@ModelAttribute PostCreateRequest postCreateRequest) throws IOException {
        String oauthProviderId = validateAndGetOauthProviderId();
        PostResponse postResponse = postService.createPost(postCreateRequest, oauthProviderId);
        return ApiResponse.ok(postResponse);
    }

    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable Long postId, @ModelAttribute PostUpdateRequest postUpdateRequest) throws IOException {
        String oauthProviderId = validateAndGetOauthProviderId();
        PostResponse postResponse = postService.updatePost(oauthProviderId, postId, postUpdateRequest);
        return ApiResponse.ok(postResponse);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(@PathVariable Long postId) {
        String oauthProviderId = validateAndGetOauthProviderId();
        postService.deletePost(oauthProviderId, postId);
        return ApiResponse.ok("게시글이 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long postId) {
        String oauthProviderId = validateAndGetOauthProviderId();
        PostResponse post = postService.getPostById(oauthProviderId, postId);
        return ApiResponse.ok(post);
    }

    @PostMapping("/{postId}/like")
    public ApiResponse<String> likePost(@PathVariable Long postId) {
        String oauthProviderId = validateAndGetOauthProviderId();
        postService.likePost(oauthProviderId, postId);
        return ApiResponse.ok("좋아요를 눌렀습니다.");
    }

    @PostMapping("/{postId}/unlike")
    public ApiResponse<String> unlikePost(@PathVariable Long postId) {
        String oauthProviderId = validateAndGetOauthProviderId();
        postService.unlikePost(oauthProviderId, postId);
        return ApiResponse.ok("좋아요를 취소했습니다.");
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String oauthProviderId = getOauthProviderId();
        List<PostResponse> posts = postService.getAllPosts(oauthProviderId, page, size);
        return ApiResponse.ok(posts);
    }

    @GetMapping("/sorted")
    public ApiResponse<List<PostResponse>> getPostsBySortType(
            @RequestParam(defaultValue = "LATEST") PostSortType sortType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String oauthProviderId = getOauthProviderId();
        List<PostResponse> posts = postService.getPostsBySortType(oauthProviderId, sortType, page, size);
        return ApiResponse.ok(posts);
    }
}
