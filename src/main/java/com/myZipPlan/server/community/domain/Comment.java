package com.myZipPlan.server.community.domain;

import com.myZipPlan.server.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    // 좋아요 수 추가
    @Column(name = "likes", nullable = false)
    private int likes = 0;  // 기본값 0

    // 부모 댓글 (대댓글인 경우)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;


    // 생성자
    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.createdDate = LocalDateTime.now();
        this.likes = 0;
    }
}
