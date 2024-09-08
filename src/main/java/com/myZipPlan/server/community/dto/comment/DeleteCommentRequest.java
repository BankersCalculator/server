package com.myZipPlan.server.community.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteCommentRequest {
    private Long userId;  // 댓글을 삭제하는 사용자 ID

    public DeleteCommentRequest(Long userId) {
        this.userId = userId;
    }
}
