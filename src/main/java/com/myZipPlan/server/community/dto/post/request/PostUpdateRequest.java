package com.myZipPlan.server.community.dto.post.request;


import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.community.domain.Post;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
public class PostUpdateRequest {

    private String title;              // 게시글 제목
    private String content;            // 게시글 내용
    private MultipartFile imageFile;   // 새로 업로드될 이미지 파일 (null일 수 있음)
    private Long loanAdviceResultId;   // 새로운 LoanAdviceResult ID (null일 수 있음)
    private String existingImageUrl;   // 기존 이미지 URL

    public void updatePost(Post post, LoanAdviceResult loanAdviceResult, String imageUrl) {
        log.info("updatePost method called with title: {}, content: {}, imageUrl: {}, loanAdviceResultId: {}",
                this.title, this.content, imageUrl, this.loanAdviceResultId);

        if (this.title != null) {
            post.setTitle(this.title);
        }
        if (this.content != null) {
            post.setContent(this.content);
        }
        post.setLoanAdviceResult(loanAdviceResult);
        post.setImageUrl(imageUrl);
        post.setLastModifiedDate(LocalDateTime.now()); // 수정 시간 업데이트
    }
}
