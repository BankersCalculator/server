package com.myZipPlan.server.community.dto.post.request;

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

    // toEntity() 메서드를 통해 Post 엔티티로 변환
    public Post toEntity(User user, String imageUrl) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .imageUrl(imageUrl)
                .likes(0) // 초기 좋아요 수는 0으로 설정
                .build();
    }
}
