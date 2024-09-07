package com.myZipPlan.server.community.dto;

import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.domain.Comment;
import com.myZipPlan.server.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponse {
    private final Long id;                   // 게시글 ID
    private final String title;              // 게시글 제목
    private final String content;            // 게시글 내용
    private final String author;            // 작성자 이름 (User 엔티티에서 가져옴)
    private final String imageUrl;           // 이미지 URL
    private final int likes;                 // 좋아요 수
    private final List<String> comments;     // 댓글 목록
    private final LocalDateTime createdDate; // 작성일자
    private final LocalDateTime lastModifiedDate; // 수정일자

    @Builder
    public PostResponse(Long id, String title, String content, String author, String imageUrl, int likes, List<String> comments, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.comments = comments;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    // Post 엔티티를 PostResponse로 변환하는 메소드
    public static PostResponse fromEntity(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getEmail()) // 작성자의 이메일을 author로 사용
                .imageUrl(post.getImageUrl())
                .likes(post.getLikes())
                .comments(post.getComments().stream()
                        .map(Comment::getContent) // 각 댓글의 내용을 가져옴
                        .collect(Collectors.toList()))
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .build();
    }
}
