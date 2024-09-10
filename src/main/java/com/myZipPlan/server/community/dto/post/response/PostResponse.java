package com.myZipPlan.server.community.dto.post.response;

import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.comment.CommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PostResponse {
    private final Long id;                   // 게시글 ID
    private final String title;              // 게시글 제목
    private final String content;            // 게시글 내용
    private final String author;             // 작성자 이름, 카카오톡 닉네임
    private final String imageUrl;           // 이미지 URL
    private final int likes;                 // 좋아요 수
    private final List<CommentResponse> comments;     // 댓글 목록
    private final LocalDateTime createdDate; // 작성일자
    private final LocalDateTime lastModifiedDate; // 수정일자

    private final String avatarUrl;          // 작성자 아바타 URL
    private final String timeAgo;            // "n시간 전"과 같은 형태로 변환된 작성 시간
    private final LoanAdviceResult loanAdviceResult;    // 보고서

    @Builder
    public PostResponse(Long id, String title, String content, String author
                       , String imageUrl, int likes
                       , List<CommentResponse> comments
                       , LocalDateTime createdDate, LocalDateTime lastModifiedDate
                       , String avatarUrl, String timeAgo
                       , LoanAdviceResult loanAdviceResult
                       ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.comments = comments;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;

        this.avatarUrl = avatarUrl;
        this.timeAgo = timeAgo;
        this.loanAdviceResult = loanAdviceResult;
    }

    // Post 엔티티를 PostResponse로 변환하는 메소드
    public static PostResponse fromEntity(Post post, LoanAdviceResult loanAdviceResult) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getEmail()) // (임시) 작성자 이메일로 잠시 사용. 닉네임 이후 대체
                .imageUrl(post.getImageUrl())
                .likes(post.getLikes())
                .comments(post.getComments().stream()
                        .map(CommentResponse::fromEntity)  // 댓글 목록 변환
                        .collect(Collectors.toList()))
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .avatarUrl("카카오프로필")  // (임시) 작성자의 아바타 URL. 사용할 이미지 공유 받으면 대체할 것.
                .timeAgo(calculateTimeAgo(post.getCreatedDate()))  // "n시간 전"으로 작성 시간 표시
                .loanAdviceResult(loanAdviceResult)
                .build();
    }


    // "n시간 전", "n일 전", "n개월 전", "n년 전" 과 같은 형태로 변환하는 유틸리티 메소드
    private static String calculateTimeAgo(LocalDateTime createdDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdDate, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        long months = ChronoUnit.MONTHS.between(createdDate, now);  // To calculate the number of months
        long years = ChronoUnit.YEARS.between(createdDate, now);  // To calculate the number of years

        if (minutes < 60) {
            return minutes + "분 전"; // Less than an hour ago
        } else if (hours < 13) {
            return hours + "시간 전";  // Less than 13 hours ago
        } else if (days < 31) {
            return days + "일 전";  // Less than 31 days ago
        } else if (months < 12) {
            return months + "개월 전";  // Less than 12 months ago
        } else {
            return years + "년 전";  // 1 year or more
        }
    }

}
