package com.order.processing.system.order.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.order.processing.system.order.service.util.AppMessages.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductStockRequestDTO implements Serializable {
    @NotEmpty(message = EMPTY_PRODUCT_UUID)
    @NotNull(message = NULL_PRODUCT_UUID)
    private String productUuid;
    @NotNull(message = NULL_PRODUCT_STOCK_AVAILABLE)
    private Integer stockAvailable;
}
