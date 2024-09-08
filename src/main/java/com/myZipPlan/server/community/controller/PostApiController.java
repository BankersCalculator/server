package com.myZipPlan.server.community.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.post.AddPostRequest;
import com.myZipPlan.server.community.dto.post.PostResponse;
import com.myZipPlan.server.community.dto.post.UpdatePostRequest;
import com.myZipPlan.server.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;

    // 게시글 작성
    @PostMapping
    public ApiResponse<PostResponse> addPost(@RequestBody AddPostRequest addPostRequest, @RequestParam Long userId) throws IOException {
        Post post = postService.addPost(addPostRequest, userId);
        PostResponse postResponse = PostResponse.fromEntity(post);
        return ApiResponse.ok(postResponse);
    }

    // 게시글 목록 조회
    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
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
    public ApiResponse<PostResponse> updatePost(@PathVariable Long postId, @RequestParam Long userId, @RequestBody UpdatePostRequest updatePostRequest) throws IOException {
        Post updatedPost = postService.updatePost(postId, userId, updatePostRequest);
        PostResponse postResponse = PostResponse.fromEntity(updatedPost);
        return ApiResponse.ok(postResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@RequestParam Long userId, @PathVariable Long postId) {
        postService.deletePost(userId, postId);
        return ApiResponse.ok(null);
    }

    // 게시글 좋아요
    @PostMapping("/{postId}/like")
    public ApiResponse<Void> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return ApiResponse.ok(null);
    }

    // 게시글 좋아요 취소
    @PostMapping("/{postId}/unlike")
    public ApiResponse<Void> unlikePost(@PathVariable Long postId) {
        postService.unlikePost(postId);
        return ApiResponse.ok(null);
    }
}
