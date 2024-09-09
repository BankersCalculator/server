package com.myZipPlan.server.community.dto.post;

import com.myZipPlan.server.community.enums.PostSortType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSortRequest {
    private PostSortType sortType;
}
