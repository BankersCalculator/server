package com.myZipPlan.server.community.dto.post.request;


import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.community.domain.Post;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostUpdateRequest {

    private String title;              // 게시글 제목
    private String content;            // 게시글 내용
    private MultipartFile imageFile;   // 새로 업로드될 이미지 파일 (null일 수 있음)
    private Long loanAdviceResultId;   // 새로운 LoanAdviceResult ID (null일 수 있음)
    private String existingImageUrl;   // 기존 이미지 URL

    // toEntity() 메서드는 필요 없으며, 대신 기존 게시글을 업데이트하는 방식으로 작성할 수 있습니다.
    public void updatePost(Post post, LoanAdviceResult loanAdviceResult, String imageUrl) {
        if (this.title != null) {
            post.setTitle(this.title);
        }
        if (this.content != null) {
            post.setContent(this.content);
        }
        if (loanAdviceResult != null) {
            post.setLoanAdviceResult(loanAdviceResult);
        }
        if (imageUrl != null) {
            post.setImageUrl(imageUrl);
        }
        post.setLastModifiedDate(LocalDateTime.now()); // 수정 시간 업데이트
    }
}
