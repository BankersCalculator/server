package com.myZipPlan.server.community.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCommentRequest {
    private String content;  // 댓글 내용
}
