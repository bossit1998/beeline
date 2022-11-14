package com.beeline.testproject.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.beeline.testproject.util.CONSTANT.defaultPage;
import static com.beeline.testproject.util.CONSTANT.defaultPageSize;

public class Util {
    public static Pageable getPageable(Integer page, Integer size) {
        if (page == null) {
            page = Integer.valueOf(defaultPage);
        }
        if (size == null) {
            size = Integer.valueOf(defaultPageSize);
        }

        page = page == 0 ? 0 : page - 1;
        return PageRequest.of(page, size);
    }
}