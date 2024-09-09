package com.myZipPlan.server.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostSortType {
    LATEST("최신순")
    , POPULAR("인기순");

    private final String description;

}
