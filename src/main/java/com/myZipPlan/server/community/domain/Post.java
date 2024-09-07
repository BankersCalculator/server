package com.myZipPlan.server.community.domain;

import com.myZipPlan.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 작성자 정보

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "createdDate", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "LastModifiedDate", nullable = false)
    private LocalDateTime LastModifiedDate;

    // 이미지 업로드 정보 (S3 링크 등)
    private String imageUrl;

    // 좋아요 수
    @Column(name = "likes", nullable = false)
    private int likes;

    @Builder
    public Post(String title, String content, User user, String imageUrl, int likes) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdDate = LocalDateTime.now();
        this.LastModifiedDate = LocalDateTime.now();
        this.imageUrl = imageUrl;
        this.likes = likes;
    }
}
