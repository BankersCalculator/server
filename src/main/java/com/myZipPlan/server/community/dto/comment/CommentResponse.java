package com.myZipPlan.server.community.dto.comment;

import com.myZipPlan.server.community.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private final Long id;  // 댓글 ID
    private final Long postId;  // 댓글이 달린 게시글 ID
    private final String author;  // 댓글 작성자 ID
    private final String content;  // 댓글 내용
    private final LocalDateTime createdDate;  // 작성일
    private final LocalDateTime lastModifiedDate;  // 수정일
    private final boolean like; //유저 댓글 좋아요 여부
    private final int likes;                 // 좋아요 수



    @Builder
    public CommentResponse(Long id, Long postId, String author, String content
                          , LocalDateTime createdDate, LocalDateTime lastModifiedDate
                          , boolean like
                          , int likes) {
        this.id = id;
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.like = like;
        this.likes = likes;
    }

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .author(comment.getUser().getEmail())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .createdDate(comment.getCreatedDate())
                .lastModifiedDate(comment.getLastModifiedDate())
                .like(false)
                .build();
    }

    public static CommentResponse fromEntity(Comment comment, boolean like) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .author(comment.getUser().getEmail())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .createdDate(comment.getCreatedDate())
                .lastModifiedDate(comment.getLastModifiedDate())
                .like(like)
                .build();
    }
}
