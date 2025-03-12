package com.order.processing.system.inventory.service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.order.processing.system.inventory.service.util.AppMessages.*;


@Entity
@Table(name = "products")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Basic(optional = false)
    private Long id;
    @Column(name = "uuid")
    private String uuid = UUID.randomUUID().toString();
    @Column(name = "name", nullable = false)
    @NotEmpty(message = EMPTY_PRODUCT_NAME)
    @NotNull(message = NULL_PRODUCT_NAME)
    private String name;
    @Column(name = "quantity")
    @Min(value = 0, message = MIN_PRODUCT_QUANTITY_EXCEEDED)
    @NotNull(message = NULL_PRODUCT_QUANTITY)
    private Integer quantity;
    @Column(name = "unit_price")
    @NotNull(message = NULL_PRODUCT_UNIT_PRICE)
    private Double unitPrice;
    @Column(name = "last_modified")
    @UpdateTimestamp
    private LocalDateTime lastModified;
    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
