package com.order.processing.system.order.service.services.impl;


import com.order.processing.system.order.service.dto.request.CancelOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.ConfirmOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.CreateOrderRequestDTO;
import com.order.processing.system.order.service.dto.response.ApiResponse;
import com.order.processing.system.order.service.dto.response.ProductResponseDTO;
import com.order.processing.system.order.service.enums.OrderStatus;
import com.order.processing.system.order.service.exceptions.RecordNotFoundException;
import com.order.processing.system.order.service.model.Order;
import com.order.processing.system.order.service.repository.OrderRepository;
import com.order.processing.system.order.service.services.OrderService;
import com.order.processing.system.order.service.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.order.processing.system.order.service.util.AppMessages.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public ResponseEntity<ApiResponse> createOrder(CreateOrderRequestDTO createOrderRequestDTO) {
        ProductResponseDTO productResponse = checkProductAvailabilityInInventoryService(createOrderRequestDTO);
        Order mappedOrder = Mapper.mapOrderRequestDTOToEntityClass(createOrderRequestDTO);
        mappedOrder.setUuid(UUID.randomUUID().toString());
        mappedOrder.setProduct(productResponse.getName());
        mappedOrder.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(mappedOrder);

        //TODO call inventory service to subtract the quantity of order placed from the stock

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        ORDER_CREATED_SUCCESSFULLY,
                        Mapper.mapOrderToResponseDTO(savedOrder)
                ));
    }

    private ProductResponseDTO checkProductAvailabilityInInventoryService(CreateOrderRequestDTO createOrderRequestDTO) {

        return ProductResponseDTO.builder()
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse> confirmOrder(ConfirmOrderRequestDTO confirmOrderRequestDTO) {
        Optional<Order> optional = validateOrderUuid(confirmOrderRequestDTO.getOrderUuid());
        Order order = optional.get();
        order.setStatus(OrderStatus.SUCCESS);
        order.setLastModified(LocalDateTime.now());
        order = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ORDER_CONFIRMED_SUCCESSFULLY,
                        Mapper.mapOrderToResponseDTO(order)
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> cancelOrder(CancelOrderRequestDTO cancelOrderRequestDTO) {
        Optional<Order> optional = validateOrderUuid(cancelOrderRequestDTO.getOrderUuid());
        Order order = optional.get();
        order.setStatus(OrderStatus.CANCELLED);
        order.setLastModified(LocalDateTime.now());
        order = orderRepository.save(order);
        //TODO Add back the quantity of the product in inventory service for the order that was initially reserved
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ORDER_CANCELLED_SUCCESSFULLY,
                        Mapper.mapOrderToResponseDTO(order)
                ));
    }

    private Optional<Order> validateOrderUuid(String orderUuid) {
        Optional<Order> optional = orderRepository.findOrderByUuid(orderUuid);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(NO_ORDER_FOUND_WITH_ID);
        }
        return optional;
    }

    @Override
    public ResponseEntity<ApiResponse> viewOrderById(String orderUuid) {
        Optional<Order> optional = validateOrderUuid(orderUuid);
        Order order = optional.get();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ORDER_RETRIEVED_SUCCESSFULLY,
                        Mapper.mapOrderToResponseDTO(order)
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> viewAllOrders(Integer page, Integer size) {
        Sort orders = Sort.by("dateCreated").descending();
        Page<Order> list = orderRepository.findAllOrders(PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size), orders));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_ORDER_FOUND);
        }
        return Mapper.processOrderPageResponse(list);
    }

    @Override
    public ResponseEntity<ApiResponse> viewUsersOrders(String userId, Integer page, Integer size) {
        Sort orders = Sort.by("dateCreated").descending();
        //TODO When you add users account, update this to fetch only orders placed by a user.
        Page<Order> list = orderRepository.findAllOrders(PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size), orders));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_ORDER_FOUND);
        }
        return Mapper.processOrderPageResponse(list);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteOrder(String orderUuid) {
        Optional<Order> optional = validateOrderUuid(orderUuid);
        orderRepository.deleteOrderByUuid(optional.get().getUuid());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ORDER_DELETED_SUCCESSFULLY
                ));
    }
}
