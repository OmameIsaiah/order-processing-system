package com.order.processing.system.inventory.service.services;


import com.order.processing.system.inventory.service.dto.request.AddProductRequestDTO;
import com.order.processing.system.inventory.service.dto.request.UpdateProductRequestDTO;
import com.order.processing.system.inventory.service.dto.request.UpdateProductStockRequestDTO;
import com.order.processing.system.inventory.service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<ApiResponse> addProduct(AddProductRequestDTO productRequestDTO);

    ResponseEntity<ApiResponse> updateProduct(UpdateProductRequestDTO updateProductRequestDTO);

    ResponseEntity<ApiResponse> updateStockAvailability(UpdateProductStockRequestDTO updateProductStockRequestDTO);

    ResponseEntity<ApiResponse> checkStockAvailability(String productUuid);

    ResponseEntity<ApiResponse> viewAllProducts(Integer page, Integer size);

    ResponseEntity<ApiResponse> deleteProduct(String productUuid);
}
