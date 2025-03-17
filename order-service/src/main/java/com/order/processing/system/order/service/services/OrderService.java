package com.order.processing.system.order.service.services;

import com.order.processing.system.order.service.dto.request.CancelOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.ConfirmOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.CreateOrderRequestDTO;
import com.order.processing.system.order.service.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<ApiResponse> createOrder(CreateOrderRequestDTO createOrderRequestDTO,
                                            HttpServletRequest httpServletRequest);

    ResponseEntity<ApiResponse> confirmOrder(ConfirmOrderRequestDTO confirmOrderRequestDTO,
                                             HttpServletRequest httpServletRequest);

    ResponseEntity<ApiResponse> cancelOrder(CancelOrderRequestDTO cancelOrderRequestDTO,
                                            HttpServletRequest httpServletRequest);

    ResponseEntity<ApiResponse> viewOrderById(String orderUuid,
                                              HttpServletRequest httpServletRequest);

    ResponseEntity<ApiResponse> viewAllOrders(Integer page, Integer size,
                                              HttpServletRequest httpServletRequest);

    ResponseEntity<ApiResponse> viewUsersOrders(String userId, Integer page, Integer size,
                                                HttpServletRequest httpServletRequest);

    ResponseEntity<ApiResponse> deleteOrder(String orderUuid,
                                            HttpServletRequest httpServletRequest);
}
