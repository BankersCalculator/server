package com.myZipPlan.server.community.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikeCommentRequest {
    private Long userId;  // 댓글에 좋아요를 누른 사용자 ID
    public LikeCommentRequest(Long userId) {
        this.userId = userId;
    }
}
