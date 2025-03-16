package com.order.processing.system.order.service.route;

import com.order.processing.system.order.service.dto.request.CancelOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.ConfirmOrderRequestDTO;
import com.order.processing.system.order.service.dto.request.CreateOrderRequestDTO;
import com.order.processing.system.order.service.dto.response.ApiResponse;
import com.order.processing.system.order.service.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.order.processing.system.order.service.util.EndpointsURL.*;

@RestController
@RequestMapping(value = ORDER_BASE_URL, headers = "Accept=application/json")
@Tag(name = "order route", description = "Endpoints for created, confirming, canceling and view orders")
@RequiredArgsConstructor
public class OrderRoute {
    private final OrderService orderService;

    @PostMapping(value = CREATE_ORDER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for creating orders",
            description = "Endpoint for creating orders")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody @Valid CreateOrderRequestDTO payload,
                                                   HttpServletRequest httpServletRequest) {
        return orderService.createOrder(payload, httpServletRequest);
    }

    @PostMapping(value = CONFIRM_ORDER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for confirming an order",
            description = "Endpoint for confirming an order")
    public ResponseEntity<ApiResponse> confirmOrder(@RequestBody @Valid ConfirmOrderRequestDTO payload) {
        return orderService.confirmOrder(payload);
    }

    @PostMapping(value = CANCEL_ORDER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for canceling an order",
            description = "Endpoint for canceling an order")
    public ResponseEntity<ApiResponse> cancelOrder(@RequestBody @Valid CancelOrderRequestDTO payload) {
        return orderService.cancelOrder(payload);
    }

    @GetMapping(value = VIEW_ORDER_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for view order by id",
            description = "Endpoint for view order by id")
    public ResponseEntity<ApiResponse> viewOrderById(@RequestParam("orderUuid") String orderUuid) {
        return orderService.viewOrderById(orderUuid);
    }

    @GetMapping(value = VIEW_USERS_ORDERS, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for viewing all orders made by a user",
            description = "Endpoint for viewing all orders made by a user")
    public ResponseEntity<ApiResponse> viewUsersOrders(
            @RequestParam(value = "userId", required = true) String userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return orderService.viewUsersOrders(userId, page, size);
    }

    @GetMapping(value = VIEW_ALL_ORDERS, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for viewing all orders",
            description = "Endpoint for viewing all orders")
    public ResponseEntity<ApiResponse> viewAllOrders(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return orderService.viewAllOrders(page, size);
    }

    @DeleteMapping(value = DELETE_ORDER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for deleting an order",
            description = "Endpoint for deleting an order")
    public ResponseEntity<ApiResponse> deleteOrder(@RequestParam("orderUuid") String orderUuid) {
        return orderService.deleteOrder(orderUuid);
    }
}
