package com.myZipPlan.server.community.dto.post.response;

import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
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
public class PostCreateResponse {
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
    private final LoanAdviceSummaryResponse loanAdviceSummaryReport;    // 보고서

    @Builder
    public PostCreateResponse(Long id, String title, String content, String author
            , String imageUrl, int likes
            , List<CommentResponse> comments
            , LocalDateTime createdDate, LocalDateTime lastModifiedDate
            , String avatarUrl, String timeAgo
            , LoanAdviceSummaryResponse loanAdviceSummaryReport
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
        this.loanAdviceSummaryReport = loanAdviceSummaryReport;
    }

    // Post 엔티티를 PostCreateResponse로 변환하는 메소드
    public static PostCreateResponse fromEntity(Post post, LoanAdviceSummaryResponse loanAdviceSummaryReport) {
        return PostCreateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getEmail()) // (임시) 작성자 이메일로 잠시 사용. 닉네임 이후 대체
                .imageUrl(post.getImageUrl())
                .likes(0)
                .comments(null)
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .avatarUrl("카카오프로필")  // (임시) 작성자의 아바타 URL. 사용할 이미지 공유 받으면 대체할 것.
                .timeAgo(null)  // "n시간 전"으로 작성 시간 표시
                .loanAdviceSummaryReport(loanAdviceSummaryReport)
                .build();
    }
}
