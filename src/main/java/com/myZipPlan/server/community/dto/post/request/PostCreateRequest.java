package com.myZipPlan.server.community.dto.post.request;

import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequest {

    private String title;
    private String content;
    private MultipartFile imageFile;
    private Long loanAdviceResultId;

    // 유효성 검증 메서드
    public void validate() {
        // 제목 검증
        if (this.title == null || this.title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해야 합니다.");
        }

        // 내용 검증
        if (this.content == null || this.content.trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해야 합니다.");
        }

        // 이미지 파일 검증
        if (this.imageFile != null) {
            // 1. 파일이 비어있는지 확인
            if (this.imageFile.isEmpty()) {
                throw new IllegalArgumentException("업로드된 파일이 없습니다.");
            }

            // 2. 파일 형식 검증 (예: 이미지 파일만 허용)
            String contentType = this.imageFile.getContentType();
            if (contentType == null || !isImageFile(contentType)) {
                throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
            }

            // 3. 파일 크기 검증 (예: 5MB 이하 파일만 허용)
            long maxFileSize = 5 * 1024 * 1024; // 5MB
            if (this.imageFile.getSize() > maxFileSize) {
                throw new IllegalArgumentException("파일 크기는 5MB 이하로 제한됩니다.");
            }
        }
    }

    // 이미지 파일인지 확인하는 유틸리티 메서드
    private boolean isImageFile(String contentType) {
        return contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif");
    }


    // toEntity() 메서드를 통해 Post 엔티티로 변환
    public Post toEntity(User user, String imageUrl, LoanAdviceResult loanAdviceResult) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .imageUrl(imageUrl)
                .likes(0) // 초기 좋아요 수는 0으로 설정
                .loanAdviceResult(loanAdviceResult)
                .build();
    }
}
