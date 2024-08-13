package com.bankersCalculator.server.board.cotroller;

import com.bankersCalculator.server.board.Service.BoardService;
import com.bankersCalculator.server.board.dto.BoardRequest;
import com.bankersCalculator.server.board.dto.BoardResponse;
import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.common.api.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/posts")
    public ApiResponse<SliceResponse<BoardResponse>> getAllPosts(Pageable pageable) {
        SliceResponse<BoardResponse> allPosts = boardService.getAllPosts(pageable);
        return ApiResponse.ok(allPosts);
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<BoardResponse> getPostById(@PathVariable Long postId) {
        BoardResponse post = boardService.getPostById(postId);
        return ApiResponse.ok(post);
    }

    @PostMapping("/posts")
    public ApiResponse<BoardResponse> createPost(@RequestBody BoardRequest request) {
        BoardResponse createdPost = boardService.createPost(request);
        return ApiResponse.ok(createdPost);
    }

    @PutMapping("/posts/{postId}")
    public ApiResponse<BoardResponse> updatePost(@PathVariable Long postId, @RequestBody BoardRequest request) {
        BoardResponse updatedPost = boardService.updatePost(postId, request);
        return ApiResponse.ok(updatedPost);
    }

    @DeleteMapping("/posts/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        boardService.deletePost(postId);
        return ApiResponse.ok(null);
    }

}
