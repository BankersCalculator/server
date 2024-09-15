package com.myZipPlan.server.community.domain;

import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;  // 작성자 정보

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "createdDate", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "LastModifiedDate", nullable = false)
    private LocalDateTime LastModifiedDate;

    // 이미지 업로드 정보 (S3 링크 등)
    @Column(length = 500)
    private String imageUrl;

    // 좋아요 수
    @Column(name = "likes", nullable = false)
    private int likes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_advice_result_id")
    private LoanAdviceResult loanAdviceResult;

    @Builder
    public Post(String title, String content, User user, String imageUrl, int likes, LoanAdviceResult loanAdviceResult) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdDate = LocalDateTime.now();
        this.LastModifiedDate = LocalDateTime.now();
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.loanAdviceResult = loanAdviceResult;
    }

    // LoanAdviceResult에서 필요한 필드 가져오기
    public String getLoanProductName() {
        return loanAdviceResult != null ? loanAdviceResult.getLoanProductName() : null;
    }

    public BigDecimal getPossibleLoanLimit() {
        return loanAdviceResult != null ? loanAdviceResult.getPossibleLoanLimit() : null;
    }

    public String getLoanProductCode() {
        return loanAdviceResult != null ? loanAdviceResult.getLoanProductCode() : null;
    }

    public BigDecimal getExpectedLoanRate() {
        return loanAdviceResult != null ? loanAdviceResult.getExpectedLoanRate() : null;
    }
}
