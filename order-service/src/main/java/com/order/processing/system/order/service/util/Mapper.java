package com.order.processing.system.order.service.util;


import com.order.processing.system.order.service.dto.request.CreateOrderRequestDTO;
import com.order.processing.system.order.service.dto.response.ApiResponse;
import com.order.processing.system.order.service.dto.response.OrderResponseDTO;
import com.order.processing.system.order.service.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.order.processing.system.order.service.util.AppMessages.ORDER_RETRIEVED_SUCCESSFULLY;


public class Mapper {
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
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ORDER_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapOrderToResponseDTO)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    public static ResponseEntity<ApiResponse> processOrderListResponse(List<Order> list) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ORDER_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapOrderToResponseDTO)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }
}
