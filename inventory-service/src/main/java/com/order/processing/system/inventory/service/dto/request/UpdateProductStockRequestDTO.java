package com.order.processing.system.inventory.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

import static com.order.processing.system.inventory.service.util.AppMessages.*;


@Data
public class UpdateProductStockRequestDTO implements Serializable {
    @NotEmpty(message = EMPTY_PRODUCT_UUID)
    @NotNull(message = NULL_PRODUCT_UUID)
    private String productUuid;
    @NotNull(message = NULL_PRODUCT_STOCK_AVAILABLE)
    private Integer stockAvailable;
}
