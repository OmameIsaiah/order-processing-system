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
public class OrderResponseDTO implements Serializable {
    private String uuid;
    private String product;
    private Integer quantity;
    private String status;
    private LocalDateTime lastModified;
    private LocalDateTime dateCreated;
}
