package com.order.processing.system.order.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

import static com.order.processing.system.order.service.util.AppMessages.EMPTY_ORDER_UUID;
import static com.order.processing.system.order.service.util.AppMessages.NULL_ORDER_UUID;


@Data
public class CancelOrderRequestDTO implements Serializable {
    @NotEmpty(message = EMPTY_ORDER_UUID)
    @NotNull(message = NULL_ORDER_UUID)
    private String orderUuid;
}
