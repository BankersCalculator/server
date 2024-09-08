package com.myZipPlan.server.community.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddReplyRequest {
    private Long userId;    // 대댓글을 작성한 사용자 ID
    private String content; // 대댓글 내용

    public AddReplyRequest(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }
}
