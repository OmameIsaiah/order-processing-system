package com.order.processing.system.order.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO implements Serializable {
    private String uuid;
    private String name;
    private Integer quantity;
    private Double unitPrice;
    private LocalDateTime lastModified;
    private LocalDateTime dateCreated;
}
