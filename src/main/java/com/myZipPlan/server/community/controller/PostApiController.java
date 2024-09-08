package com.myZipPlan.server.community.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.AddPostRequest;
import com.myZipPlan.server.community.dto.PostResponse;
import com.myZipPlan.server.community.dto.UpdatePostRequest;
import com.myZipPlan.server.community.service.PostService;
import com.myZipPlan.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ApiResponse<Post> addPost(@RequestBody AddPostRequest addPostRequest, @RequestParam Long userId) throws IOException {
        Post createPost = postService.addPost(addPostRequest, userId);
        return ApiResponse.ok(createPost);
    }

    // 게시글 목록 조회
    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ApiResponse.ok(posts);
    }

    // 게시글 단일 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long postId) {
        PostResponse post = postService.getPostById(postId);
        return ApiResponse.ok(post);
    }

    // 게시글 수정
    @PutMapping("/{userId}/{postId}")
    public ApiResponse<Post> updatePost(@PathVariable Long userId, @PathVariable Long postId, @RequestBody UpdatePostRequest updatePostRequest) throws IOException {
        Post updatedPost = postService.updatePost(postId, userId, updatePostRequest);
        return ApiResponse.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{userId}/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long userId,
                                           @PathVariable Long postId) {
        postService.deletePost(userId, postId);
        return ApiResponse.ok(null);
    }
}
