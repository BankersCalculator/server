package com.myZipPlan.server.community.dto.post.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostUpdateRequest {

    private String title;         // 수정할 게시글 제목
    private String content;       // 수정할 게시글 내용
    private MultipartFile imageFile;  // 수정할 이미지 파일

    public PostUpdateRequest(String title, String content, MultipartFile imageFile) {
        this.title = title;
        this.content = content;
        this.imageFile = imageFile;
    }
}
