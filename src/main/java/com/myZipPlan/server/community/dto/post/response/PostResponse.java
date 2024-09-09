package com.myZipPlan.server.community.dto.post.response;

import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.comment.CommentResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponse {
    private final Long id;                   // 게시글 ID
    private final String title;              // 게시글 제목
    private final String content;            // 게시글 내용
    private final String author;            // 작성자 이름 (User 엔티티에서 가져옴) , 어떤값을 받아올지 정해야함.
    private final String imageUrl;           // 이미지 URL
    private final int likes;                 // 좋아요 수
    private final List<CommentResponse> comments;     // 댓글 목록
    private final LocalDateTime createdDate; // 작성일자
    private final LocalDateTime lastModifiedDate; // 수정일자

    //private final String nickname;           // 작성자 닉네임
    //private final String avatarUrl;          // 작성자 아바타 URL
    //private final String timeAgo;            // "n시간 전"과 같은 형태로 변환된 작성 시간
    //private final ReportComponent report;    // 보고서 컴포넌트

    @Builder
    public PostResponse(Long id, String title, String content, String author, String imageUrl, int likes,  List<CommentResponse> comments, LocalDateTime createdDate, LocalDateTime lastModifiedDate
                       //, String nickname, String avatarUrl, String timeAgo, ReportComponent report
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
      //  this.nickname = nickname;
      //  this.avatarUrl = avatarUrl;
      //  this.timeAgo = timeAgo;
       // this.report = report;
    }

    // Post 엔티티를 PostResponse로 변환하는 메소드
    public static PostResponse fromEntity(Post post
                                         //, ReportComponent reportComponent
                                         ) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getEmail()) // 작성자의 이메일을 author로 사용
                .imageUrl(post.getImageUrl())
                .likes(post.getLikes())
                .comments(post.getComments().stream()
                        .map(CommentResponse::fromEntity)  // 댓글 목록 변환
                        .collect(Collectors.toList()))
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
               // .nickname("호랑이")
               // .avatarUrl("카카오프로필")  // 작성자의 아바타 URL
               // .timeAgo(calculateTimeAgo(post.getCreatedDate()))  // "n시간 전"으로 작성 시간 표시
              //  .report(reportComponent)  // 보고서 컴포넌트 (임시설정)
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
