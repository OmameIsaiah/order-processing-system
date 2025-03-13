package com.order.processing.system.inventory.service.util;

import com.order.processing.system.inventory.service.model.Product;
import com.order.processing.system.proto.AddProductRequest;
import com.order.processing.system.proto.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class Mapper {
    public static Product mapProductRequestDTOToEntityClass(AddProductRequest request) {
        return Optional.ofNullable(
                        Objects.isNull(request) ? null :
                                Product.builder()
                                        .uuid(UUID.randomUUID().toString())
                                        .name(request.getName())
                                        .quantity(request.getQuantity())
                                        .unitPrice(request.getUnitPrice())
                                        .build())
                .orElse(null);
    }

    public static List<ProductResponse> getListProductResponse(Page<Product> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(Mapper::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    public static ProductResponse mapProductToProductResponse(Product product) {
        return Optional.ofNullable(
                        Objects.isNull(product) ? null :
                                ProductResponse.newBuilder()
                                        .setUuid(product.getUuid())
                                        .setName(product.getName())
                                        .setQuantity(product.getQuantity())
                                        .setUnitPrice(product.getUnitPrice())
                                        .setDateCreated(Utils.toProtoTimestamp(product.getDateCreated()))
                                        .setLastModified(Utils.toProtoTimestamp(product.getLastModified()))
                                        .build())
                .orElse(null);
    }
}
