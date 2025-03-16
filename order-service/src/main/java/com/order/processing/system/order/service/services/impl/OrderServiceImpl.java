package com.order.processing.system.order.service.services.impl;


import com.order.processing.system.order.service.dto.request.CancelOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.ConfirmOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.CreateOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.UpdateProductStockRequestDTO;
import com.order.processing.system.order.service.dto.response.ApiResponse;
import com.order.processing.system.order.service.dto.response.ProductResponseDTO;
import com.order.processing.system.order.service.enums.OrderStatus;
import com.order.processing.system.order.service.exceptions.BadRequestException;
import com.order.processing.system.order.service.exceptions.RecordNotFoundException;
import com.order.processing.system.order.service.model.Order;
import com.order.processing.system.order.service.repository.OrderRepository;
import com.order.processing.system.order.service.services.OrderService;
import com.order.processing.system.order.service.util.Mapper;
import com.order.processing.system.order.service.util.SecurityUtils;
import com.order.processing.system.order.service.util.Utils;
import com.order.processing.system.proto.CheckStockRequest;
import com.order.processing.system.proto.ProductResponse;
import com.order.processing.system.proto.ProductServiceGrpc;
import com.order.processing.system.proto.ViewAllProductsRequest;
import io.grpc.StatusRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.order.processing.system.order.service.util.AppMessages.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;
    private final ProductGrpcService productService;

    @Override
    public ResponseEntity<ApiResponse> createOrder(CreateOrderRequestDTO createOrderRequestDTO,
                                                   HttpServletRequest httpServletRequest) {
        String userid = SecurityUtils.getUseridFromRequestHeader(httpServletRequest);
        ProductResponseDTO productResponse = checkProductAvailabilityInInventoryService(createOrderRequestDTO);
        Order mappedOrder = Mapper.mapOrderRequestDTOToEntityClass(createOrderRequestDTO);
        String userId = Objects.isNull(userid) ? "anonymous" : userid;
        mappedOrder.setUserId(userId);
        mappedOrder.setUuid(UUID.randomUUID().toString());
        mappedOrder.setProduct(productResponse.getName());
        mappedOrder.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(mappedOrder);

        //Make call inventory service to subtract the quantity of order placed from the stock
        productService.updateStockAvailability(
                UpdateProductStockRequestDTO.builder()
                        .productUuid(createOrderRequestDTO.getProductUuid())
                        .stockAvailable(productResponse.getQuantity() - createOrderRequestDTO.getQuantity())
                        .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        ORDER_CREATED_SUCCESSFULLY,
                        Mapper.mapOrderToResponseDTO(savedOrder)
                ));
    }

    private ProductResponseDTO checkProductAvailabilityInInventoryService(CreateOrderRequestDTO createOrderRequestDTO) {
        CheckStockRequest request = CheckStockRequest.newBuilder()
                .setProductUuid(createOrderRequestDTO.getProductUuid())
                .build();
        try {
            com.order.processing.system.proto.ApiResponse response = this.productServiceBlockingStub.checkStockAvailability(request);
            if (response.getSuccess()) {
                ProductResponse productResponse = response.getData(0);
                if (productResponse.getQuantity() < createOrderRequestDTO.getQuantity()) {
                    throw new BadRequestException("Oops! The quantity requested is less than the quantity available in stock (" + productResponse.getQuantity() + ")");
                } else {
                    return ProductResponseDTO.builder()
                            .uuid(productResponse.getUuid())
                            .name(productResponse.getName())
                            .quantity(productResponse.getQuantity())
                            .unitPrice(productResponse.getUnitPrice())
                            .dateCreated(Utils.toLocalDateTime(productResponse.getDateCreated()))
                            .lastModified(Utils.toLocalDateTime(productResponse.getLastModified()))
                            .build();
                }
            }
            throw new BadRequestException(NO_PRODUCT_FOUND_WITH_ID);
        } catch (StatusRuntimeException e) {
            throw new BadRequestException(COULD_NOT_PROCESS_REQUEST);
        }
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
        if (OrderStatus.SUCCESS.equals(order.getStatus())) {
            throw new BadRequestException(SUCCESSFUL_ORDER_CANNOT_BE_CANCELED);
        }
        if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new BadRequestException(ORDER_ALREADY_CANCELED);
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setLastModified(LocalDateTime.now());
        order = orderRepository.save(order);

        //Add back the quantity of the product in inventory service for the order that was initially reserved
        ViewAllProductsRequest request = ViewAllProductsRequest.newBuilder()
                .setPage(0)
                .setSize(1000)
                .build();
        com.order.processing.system.proto.ApiResponse response = this.productServiceBlockingStub.viewAllProducts(request);
        if (response.getSuccess()) {
            List<ProductResponse> productResponseList = response.getDataList();
            if (!productResponseList.isEmpty()) {
                Order finalOrder = order;
                ProductResponse response1 = productResponseList.stream()
                        .filter(productResponse -> productResponse.getName().equalsIgnoreCase(finalOrder.getProduct()))
                        .findFirst()
                        .get();
                productService.updateStockAvailability(
                        UpdateProductStockRequestDTO.builder()
                                .productUuid(response1.getUuid())
                                .stockAvailable(response1.getQuantity() + finalOrder.getQuantity())
                                .build());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).
                body(new ApiResponse<>(true,
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
        if (Objects.isNull(userId)) {
            throw new BadRequestException(NULL_USER_UUID);
        }
        Sort orders = Sort.by("dateCreated").descending();
        Page<Order> list = orderRepository.findOrderByUserId(
                userId,
                PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size), orders));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_ORDER_FOUND);
        }
        return Mapper.processOrderPageResponse(list);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteOrder(String orderUuid) {
        Optional<Order> optional = validateOrderUuid(orderUuid);
        if (OrderStatus.PENDING.equals(optional.get().getStatus())) {
            throw new BadRequestException(PENDING_ORDER_CANNOT_BE_DELETED);
        }
        orderRepository.deleteOrderByUuid(optional.get().getUuid());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ORDER_DELETED_SUCCESSFULLY
                ));
    }
}
