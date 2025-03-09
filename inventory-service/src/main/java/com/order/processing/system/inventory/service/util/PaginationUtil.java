package com.order.processing.system.inventory.service.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginationUtil {

    public static <T> Page<T> getPage(List<T> list, int pageNumber, int pageSize) {
        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, list.size());

        if (fromIndex > list.size() || fromIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid page number");
        }
        List<T> subList = list.subList(fromIndex, toIndex);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return new PageImpl<>(subList, pageable, list.size());
    }
}
