package com.order.processing.system.inventory.service.util;

import com.order.processing.system.inventory.service.dto.request.AddProductRequestDTO;
import com.order.processing.system.inventory.service.dto.response.ApiResponse;
import com.order.processing.system.inventory.service.dto.response.ProductResponseDTO;
import com.order.processing.system.inventory.service.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static com.order.processing.system.inventory.service.util.AppMessages.PRODUCT_RETRIEVED_SUCCESSFULLY;


public class Mapper {
    public static final String TOTAL_RECORDS = "totalRecords";
    public static final String TOTAL_PAGES = "totalPage";
    public static final String PAGEABLE = "pageable";

    public static Product mapProductRequestDTOToEntityClass(AddProductRequestDTO request) {
        return Optional.ofNullable(
                        Objects.isNull(request) ? null :
                                Product.builder()
                                        .uuid(UUID.randomUUID().toString())
                                        .name(request.getName())
                                        .quantity(request.getQuantity())
                                        .build())
                .orElse(null);
    }

    public static ProductResponseDTO mapProductToResponseDTO(Product product) {
        return Optional.ofNullable(
                        Objects.isNull(product) ? null :
                                ProductResponseDTO.builder()
                                        .uuid(product.getUuid())
                                        .name(product.getName())
                                        .quantity(product.getQuantity())
                                        .dateCreated(product.getDateCreated())
                                        .lastModified(product.getLastModified())
                                        .build())
                .orElse(null);
    }


    public static ResponseEntity<ApiResponse> processProductPageResponse(Page<Product> list) {
        Map<String, Object> map = new HashMap<>();
        map.put(PAGEABLE, list.getPageable());
        map.put(TOTAL_RECORDS, list.getTotalElements());
        map.put(TOTAL_PAGES, list.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PRODUCT_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapProductToResponseDTO)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()),
                        map
                ));
    }

    public static Page<Product> convertListToPage(List<Product> list, int pageNo, int pageSize) {
        return PaginationUtil.getPage(list, pageNo, pageSize);
    }

    public static ResponseEntity<ApiResponse> processProductListResponse(List<Product> list, int pageNo, int pageSize) {
        return processProductPageResponse(convertListToPage(list, pageNo, pageSize));
    }
}
