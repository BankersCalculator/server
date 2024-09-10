package com.myZipPlan.server.community.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest {
    private String updatedContent;  // 수정된 댓글 내용
}
