package com.myZipPlan.server.community.controller;

import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.post.request.PostCreateRequest;
import com.myZipPlan.server.community.dto.post.request.PostSortRequest;
import com.myZipPlan.server.community.dto.post.request.PostUpdateRequest;
import com.myZipPlan.server.community.dto.post.response.PostResponse;
import com.myZipPlan.server.community.service.PostService;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;
    private final LoanAdviceResultRepository loanAdviceResultRepository;

    // 게시글 작성
    @PostMapping
    public ApiResponse<PostResponse> addPost(@RequestBody PostCreateRequest postCreateRequest) throws IOException {
        //security session에 있는 oauthProviderId GET
        String oauthProviderId = SecurityUtils.getProviderId();
        Post post = postService.addPost(postCreateRequest, oauthProviderId);
        LoanAdviceResult loanAdviceResult = loanAdviceResultRepository.findById(postCreateRequest.getLoanAdviceResultId())
                .orElseThrow(() -> new IllegalArgumentException("LoanAdviceResult를 찾을 수 없습니다."));

        PostResponse postResponse = PostResponse.fromEntity(post, loanAdviceResult);
        return ApiResponse.ok(postResponse);
    }

    // 게시글 목록 조회
    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ApiResponse.ok(posts);
    }

    // 게시글 목록 조회 (정렬 기능 포함)
    @GetMapping("/sorted")
    public ApiResponse<List<PostResponse>> getPostsBySortType(@RequestBody PostSortRequest postSortRequest) {
        List<PostResponse> posts = postService.getPostsBySortType(postSortRequest.getSortType());
        return ApiResponse.ok(posts);
    }

    // 게시글 상세조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long postId) {
        PostResponse post = postService.getPostById(postId);
        return ApiResponse.ok(post);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest updatePostRequest) throws IOException {
        String oauthProviderId = SecurityUtils.getProviderId();
        Post updatedPost = postService.updatePost(oauthProviderId, postId, updatePostRequest);
        LoanAdviceResult loanAdviceReport =
        PostResponse postResponse = PostResponse.fromEntity(updatedPost);
        return ApiResponse.ok(postResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        postService.deletePost(oauthProviderId, postId);
        return ApiResponse.ok(null);
    }


    @PostMapping("/{postId}/like")
    public ApiResponse<Void> likePost(@PathVariable Long postId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        postService.likePost(oauthProviderId, postId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{postId}/unlike")
    public ApiResponse<Void> unlikePost(@PathVariable Long postId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        postService.unlikePost(oauthProviderId, postId);
        return ApiResponse.ok(null);
    }
}
