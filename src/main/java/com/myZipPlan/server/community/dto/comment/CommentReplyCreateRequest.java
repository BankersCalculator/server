package com.myZipPlan.server.community.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentReplyCreateRequest {
    private String content; // 대댓글 내용

    public CommentReplyCreateRequest(String content) {
        this.content = content;
    }
}
