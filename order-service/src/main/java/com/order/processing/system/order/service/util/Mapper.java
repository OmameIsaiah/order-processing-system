package com.order.processing.system.order.service.util;


import com.order.processing.system.order.service.dto.request.CreateOrderRequestDTO;
import com.order.processing.system.order.service.dto.response.ApiResponse;
import com.order.processing.system.order.service.dto.response.OrderResponseDTO;
import com.order.processing.system.order.service.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static com.order.processing.system.order.service.util.AppMessages.ORDER_RETRIEVED_SUCCESSFULLY;


public class Mapper {
    public static final String TOTAL_RECORDS = "totalRecords";
    public static final String TOTAL_PAGES = "totalPage";
    public static final String PAGEABLE = "pageable";

    public static Order mapOrderRequestDTOToEntityClass(CreateOrderRequestDTO request) {
        return Optional.ofNullable(
                        Objects.isNull(request) ? null :
                                Order.builder()
                                        .uuid(UUID.randomUUID().toString())
                                        .quantity(request.getQuantity())
                                        .build())
                .orElse(null);
    }

    public static OrderResponseDTO mapOrderToResponseDTO(Order order) {
        return Optional.ofNullable(
                        Objects.isNull(order) ? null :
                                OrderResponseDTO.builder()
                                        .uuid(order.getUuid())
                                        .product(order.getProduct())
                                        .quantity(order.getQuantity())
                                        .status(order.getStatus().label)
                                        .dateCreated(order.getDateCreated())
                                        .lastModified(order.getLastModified())
                                        .build())
                .orElse(null);
    }


    public static ResponseEntity<ApiResponse> processOrderPageResponse(Page<Order> list) {
        Map<String, Object> map = new HashMap<>();
        map.put(PAGEABLE, list.getPageable());
        map.put(TOTAL_RECORDS, list.getTotalElements());
        map.put(TOTAL_PAGES, list.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ORDER_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapOrderToResponseDTO)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()),
                        map
                ));
    }

    public static Page<Order> convertListToPage(List<Order> list, int pageNo, int pageSize) {
        return PaginationUtil.getPage(list, pageNo, pageSize);
    }

    public static ResponseEntity<ApiResponse> processOrderListResponse(List<Order> list, int pageNo, int pageSize) {
        return processOrderPageResponse(convertListToPage(list, pageNo, pageSize));
    }
}
