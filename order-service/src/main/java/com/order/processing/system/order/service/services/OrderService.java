package com.order.processing.system.order.service.services;

import com.order.processing.system.order.service.dto.request.CancelOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.ConfirmOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.CreateOrderRequestDTO;
import com.order.processing.system.order.service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<ApiResponse> createOrder(CreateOrderRequestDTO createOrderRequestDTO);

    ResponseEntity<ApiResponse> confirmOrder(ConfirmOrderRequestDTO confirmOrderRequestDTO);

    ResponseEntity<ApiResponse> cancelOrder(CancelOrderRequestDTO cancelOrderRequestDTO);

    ResponseEntity<ApiResponse> viewOrderById(String orderUuid);

    ResponseEntity<ApiResponse> viewAllOrders(Integer page, Integer size);

    ResponseEntity<ApiResponse> viewUsersOrders(String userId, Integer page, Integer size);

    ResponseEntity<ApiResponse> deleteOrder(String orderUuid);
}
