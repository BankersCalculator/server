package com.myZipPlan.server.common.api;

import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;


@Getter
public class SliceResponse<T> {

    private final List<T> content;
    private final int currentPage;
    private final int size;
    private final Boolean isFirst;
    private final Boolean isLast;

    private SliceResponse(Slice<T> data) {
        this.content = data.getContent();
        this.currentPage = data.getNumber() + 1;
        this.size = data.getSize();
        this.isFirst = data.isFirst();
        this.isLast = data.isLast();
    }

    public static <T> SliceResponse<T> of(List<T> content, Pageable pageable, boolean hasNext) {
        return new SliceResponse<>(new SliceImpl<>(content, pageable, hasNext));
    }
}
