package com.myZipPlan.server.community.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCommentRequest {
    private Long userId;  // 댓글 작성자 ID
    private String content;  // 댓글 내용
}
