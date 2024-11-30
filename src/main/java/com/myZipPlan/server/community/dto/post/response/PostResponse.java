package com.myZipPlan.server.community.dto.post.response;

import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.common.util.DateTimeUtil;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.comment.CommentResponse;
import com.myZipPlan.server.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private int commentCount;
    private final LocalDateTime createdDate; // 작성일자
    private final LocalDateTime lastModifiedDate; // 수정일자

    private final String avatarUrl;          // 작성자 아바타 URL
    private final String timeAgo;            // "n시간 전"과 같은 형태로 변환된 작성 시간
    private final LoanAdviceSummaryResponse loanAdviceSummaryReport;    // 보고서

    private final boolean like;

    private final String updateDeleteAuthority;

    private final String loginUserName;


    @Builder
    public PostResponse(Long id, String title, String content, String author
                       , String imageUrl, int likes
                       , List<CommentResponse> comments
                       , LocalDateTime createdDate, LocalDateTime lastModifiedDate
                       , String avatarUrl, String timeAgo
                       , LoanAdviceSummaryResponse loanAdviceSummaryReport
                       , Boolean like
                       , String updateDeleteAuthority
                       , String loginUserName
                       ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.comments = comments;
        this.commentCount = (comments != null) ? comments.size() : 0;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;

        this.avatarUrl = avatarUrl;
        this.timeAgo = timeAgo;
        this.loanAdviceSummaryReport = loanAdviceSummaryReport;
        this.like = like;

        this.updateDeleteAuthority = updateDeleteAuthority;

        this.loginUserName = loginUserName;
    }


    public static PostResponse fromEntity(User user, Post post
                                          , List<CommentResponse> comments
                                          , LoanAdviceSummaryResponse loanAdviceSummaryReport
                                          , Boolean like
                                          , String updateDeleteAuthority) {
        return PostResponse.builder()
                .loginUserName(user.getName())
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getName())
                .imageUrl(post.getImageUrl())
                .likes(post.getLikes())
                .comments(comments)
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .avatarUrl(post.getUser().getAnimalProfileImageUrl())
                .timeAgo(DateTimeUtil.calculateTimeAgo(post.getCreatedDate()))  // "n시간 전"으로 작성 시간 표시
                .loanAdviceSummaryReport(loanAdviceSummaryReport)
                .like(like != null ? like : false)
                .updateDeleteAuthority(updateDeleteAuthority)
                .build();
    }

    public static PostResponse fromEntity(Post post
                                        , List<CommentResponse> comments
                                        , LoanAdviceSummaryResponse loanAdviceSummaryReport
                                        , Boolean like
                                        , String updateDeleteAuthority) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getName())
                .imageUrl(post.getImageUrl())
                .likes(post.getLikes())
                .comments(comments)
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .avatarUrl(post.getUser().getProfileImageUrl())
                .timeAgo(DateTimeUtil.calculateTimeAgo(post.getCreatedDate()))  // "n시간 전"으로 작성 시간 표시
                .loanAdviceSummaryReport(loanAdviceSummaryReport)
                .like(like != null ? like : false)
                .updateDeleteAuthority(updateDeleteAuthority)
                .build();
    }
}
