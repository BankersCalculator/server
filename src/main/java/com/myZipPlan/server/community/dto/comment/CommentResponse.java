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
    private final Long userId;  // 댓글 작성자 ID
    private final String content;  // 댓글 내용
    private final LocalDateTime createdDate;  // 작성일
    private final LocalDateTime lastModifiedDate;  // 수정일


    @Builder
    public CommentResponse(Long id, Long postId, Long userId, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate, CommentResponse childComment) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    // Comment 엔티티를 CommentResponse로 변환하는 메소드
    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .lastModifiedDate(comment.getLastModifiedDate())
                .build();
    }
}
