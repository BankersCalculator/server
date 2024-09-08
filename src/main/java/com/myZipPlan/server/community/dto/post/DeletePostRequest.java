package com.myZipPlan.server.community.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeletePostRequest {
    private Long userId;  // 게시글을 삭제하는 사용자 ID

    public DeletePostRequest(Long userId) {
        this.userId = userId;
    }
}
