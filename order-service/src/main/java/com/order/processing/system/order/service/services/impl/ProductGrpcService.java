package com.order.processing.system.order.service.services.impl;

import com.order.processing.system.order.service.dto.request.AddProductRequestDTO;
import com.order.processing.system.order.service.dto.request.UpdateProductRequestDTO;
import com.order.processing.system.order.service.dto.request.UpdateProductStockRequestDTO;
import com.order.processing.system.proto.AddProductRequest;
import com.order.processing.system.proto.ApiResponse;
import com.order.processing.system.proto.ProductServiceGrpc;
import com.order.processing.system.proto.ViewAllProductsRequest;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductGrpcService {
    private final ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    public ResponseEntity<com.order.processing.system.proto.ApiResponse> addProduct(AddProductRequestDTO productRequestDTO) {
        AddProductRequest request = AddProductRequest.newBuilder()
                .setName(productRequestDTO.getName())
                .setQuantity(productRequestDTO.getQuantity())
                .setUnitPrice(productRequestDTO.getUnitPrice())
                .build();
        try {
            ApiResponse response = this.productServiceBlockingStub.addProduct(request);
            if (response.getSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (StatusRuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.newBuilder()
                            .setSuccess(false)
                            .setCode(HttpStatus.BAD_REQUEST.value())
                            .setStatus(HttpStatus.BAD_REQUEST.toString())
                            .setMessage(e.getMessage())
                            .addAllData(new ArrayList<>())
                            .build());
        }
    }

    public ResponseEntity<ApiResponse> updateProduct(UpdateProductRequestDTO updateProductRequestDTO) {
        return null;
    }

    public ResponseEntity<ApiResponse> updateStockAvailability(UpdateProductStockRequestDTO updateProductStockRequestDTO) {
        return null;
    }

    public ResponseEntity<ApiResponse> checkStockAvailability(String productUuid) {
        return null;
    }

    public ResponseEntity<ApiResponse> viewAllProducts(Integer page, Integer size) {
        ViewAllProductsRequest request = ViewAllProductsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();
        try {
            ApiResponse response = this.productServiceBlockingStub.viewAllProducts(request);
            if (response.getSuccess()) {
                return ResponseEntity.ok().body(response);
            }
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (StatusRuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.newBuilder()
                            .setSuccess(false)
                            .setCode(HttpStatus.BAD_REQUEST.value())
                            .setStatus(HttpStatus.BAD_REQUEST.toString())
                            .setMessage(e.getMessage())
                            .addAllData(new ArrayList<>())
                            .build());
        }
    }

    public ResponseEntity<ApiResponse> deleteProduct(String productUuid) {
        return null;
    }
}
