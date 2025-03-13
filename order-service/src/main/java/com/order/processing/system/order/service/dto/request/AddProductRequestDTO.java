package com.order.processing.system.order.service.dto.request;

import com.order.processing.system.order.service.util.AppMessages;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

import static com.order.processing.system.order.service.util.AppMessages.*;

@Data
public class AddProductRequestDTO implements Serializable {
    @NotEmpty(message = AppMessages.EMPTY_PRODUCT_NAME)
    @NotNull(message = AppMessages.NULL_PRODUCT_NAME)
    private String name;
    @Min(value = 1, message = MIN_PRODUCT_QUANTITY_EXCEEDED_REQUEST)
    @NotNull(message = NULL_PRODUCT_QUANTITY)
    private Integer quantity;
    @Min(value = 0, message = MIN_PRODUCT_UNIT_PRICE_EXCEEDED)
    @NotNull(message = NULL_PRODUCT_UNIT_PRICE)
    private Double unitPrice;
}
