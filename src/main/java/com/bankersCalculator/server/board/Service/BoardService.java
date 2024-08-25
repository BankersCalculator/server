package com.bankersCalculator.server.board.Service;

import com.bankersCalculator.server.board.entity.Board;
import com.bankersCalculator.server.board.dto.BoardRequest;
import com.bankersCalculator.server.board.dto.BoardResponse;
import com.bankersCalculator.server.board.repository.BoardRepository;
import com.bankersCalculator.server.common.api.SliceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public SliceResponse<BoardResponse> getAllPosts(Pageable pageable) {

        return SliceResponse.of(new ArrayList<>(), pageable, false);
    }

    public BoardResponse getPostById(Long id) {
        return boardRepository.findById(id)
            .map(this::convertToDto)
            .orElse(null);
    }

    public BoardResponse createPost(BoardRequest request) {
        Board post = new Board();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(request.getAuthor());
        Board savedPost = boardRepository.save(post);
        return convertToDto(savedPost);
    }

    public BoardResponse updatePost(Long id, BoardRequest request) {
        Board post = boardRepository.findById(id).orElse(null);
        if (post != null) {
            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            post.setAuthor(request.getAuthor());
            Board updatedPost = boardRepository.save(post);
            return convertToDto(updatedPost);
        }
        return null;
    }

    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    private BoardResponse convertToDto(Board post) {
        BoardResponse response = new BoardResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setAuthor(post.getAuthor());
        response.setCreatedDate(post.getCreatedDate().toLocalDate());
        response.setModifiedDate(post.getModifiedDate().toLocalDate());
        return response;
    }
}