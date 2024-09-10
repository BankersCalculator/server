package com.myZipPlan.server.community.dto.post.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostLikeRequest {
    private Long userId;  // 게시글에 좋아요를 누른 사용자 ID

    public PostLikeRequest(Long userId) {
        this.userId = userId;
    }
}
