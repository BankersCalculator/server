package com.myZipPlan.server.community.controller;

import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.common.enums.community.PostSortType;
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
    public ApiResponse<PostResponse> createPost(@ModelAttribute  PostCreateRequest postCreateRequest) throws IOException {
        //security session에 있는 oauthProviderId GET
        String oauthProviderId = SecurityUtils.getProviderId();
        Post post = postService.createPost(postCreateRequest, oauthProviderId);
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
    public ApiResponse<List<PostResponse>> getPostsBySortType(@RequestParam PostSortType sortType) {
        List<PostResponse> posts = postService.getPostsBySortType(sortType);
        return ApiResponse.ok(posts);
    }

    // 게시글 상세조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long postId) {
        if (postId <= 0) {
            throw new IllegalArgumentException("올바르지 않은 게시글 ID입니다.");
        }
        PostResponse post = postService.getPostById(postId);
        return ApiResponse.ok(post);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable Long postId, @ModelAttribute PostUpdateRequest postUpdateRequest) throws IOException {
        String oauthProviderId = SecurityUtils.getProviderId();
        Post updatedPost = postService.updatePost(oauthProviderId, postId, postUpdateRequest);
        LoanAdviceResult loanAdviceResult = updatedPost.getLoanAdviceResult();
        PostResponse postResponse = PostResponse.fromEntity(updatedPost, loanAdviceResult);

        return ApiResponse.ok(postResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(@PathVariable Long postId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        postService.deletePost(oauthProviderId, postId);
        return ApiResponse.ok("게시글이 성공적으로 삭제되었습니다.");
    }


    @PostMapping("/{postId}/like")
    public ApiResponse<String> likePost(@PathVariable Long postId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        postService.likePost(oauthProviderId, postId);
        return ApiResponse.ok( "좋아요를 눌렀습니다.");
    }

    @PostMapping("/{postId}/unlike")
    public ApiResponse<String> unlikePost(@PathVariable Long postId) {
        String oauthProviderId = SecurityUtils.getProviderId();
        postService.unlikePost(oauthProviderId, postId);
        return ApiResponse.ok( "좋아요를 취소했습니다.");
    }
}
