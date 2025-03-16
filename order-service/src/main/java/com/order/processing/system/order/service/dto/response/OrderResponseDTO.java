package com.order.processing.system.order.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO implements Serializable {
    private String orderUuid;
    private String userId;
    private String product;
    private Integer quantity;
    private String status;
    private String lastModified;
    private String dateCreated;
}
