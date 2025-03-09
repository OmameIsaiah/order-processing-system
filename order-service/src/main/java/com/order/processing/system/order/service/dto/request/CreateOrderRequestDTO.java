package com.order.processing.system.order.service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

import static com.order.processing.system.order.service.util.AppMessages.*;

@Data
public class CreateOrderRequestDTO implements Serializable {
    @NotEmpty(message = EMPTY_PRODUCT_UUID)
    @NotNull(message = NULL_PRODUCT_UUID)
    private String productUuid;
    @Min(value = 1, message = MIN_PRODUCT_QUANTITY_EXCEEDED)
    @NotNull(message = NULL_PRODUCT_QUANTITY)
    private Integer quantity;
}
