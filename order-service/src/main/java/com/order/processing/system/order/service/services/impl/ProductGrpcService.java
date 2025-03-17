package com.order.processing.system.order.service.services.impl;

import com.order.processing.system.order.service.dto.request.AddProductRequestDTO;
import com.order.processing.system.order.service.dto.request.UpdateProductRequestDTO;
import com.order.processing.system.order.service.dto.request.UpdateProductStockRequestDTO;
import com.order.processing.system.order.service.util.SecurityUtils;
import com.order.processing.system.proto.*;
import io.grpc.StatusRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.order.processing.system.order.service.util.AppMessages.PRODUCT_STOCK_AVAILABLE;
import static com.order.processing.system.order.service.util.AppMessages.PRODUCT_STOCK_UNAVAILABLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductGrpcService {
    private final ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    public ResponseEntity<ApiResponse> addProduct(AddProductRequestDTO requestDTO,
                                                  HttpServletRequest httpServletRequest) {
        SecurityUtils.validateBearerToken(httpServletRequest);
        AddProductRequest request = AddProductRequest.newBuilder()
                .setName(requestDTO.getName())
                .setQuantity(requestDTO.getQuantity())
                .setUnitPrice(requestDTO.getUnitPrice())
                .build();
        try {
            ApiResponse response = this.productServiceBlockingStub.addProduct(request);
            if (response.getSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (StatusRuntimeException e) {
            return processBadRequestResponse(e);
        }
    }

    public ResponseEntity<ApiResponse> updateProduct(UpdateProductRequestDTO requestDTO,
                                                     HttpServletRequest httpServletRequest) {
        SecurityUtils.validateBearerToken(httpServletRequest);
        UpdateProductRequest request = UpdateProductRequest.newBuilder()
                .setProductUuid(requestDTO.getProductUuid())
                .setName(requestDTO.getName())
                .setQuantity(requestDTO.getQuantity())
                .setUnitPrice(requestDTO.getUnitPrice())
                .build();
        try {
            ApiResponse response = this.productServiceBlockingStub.updateProduct(request);
            if (response.getSuccess()) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (StatusRuntimeException e) {
            return processBadRequestResponse(e);
        }
    }

    public ResponseEntity<ApiResponse> updateStockAvailabilityEndpoint(UpdateProductStockRequestDTO requestDTO,
                                                                       HttpServletRequest httpServletRequest) {
        SecurityUtils.validateBearerToken(httpServletRequest);
        return updateStockAvailability(requestDTO);
    }

    public ResponseEntity<ApiResponse> updateStockAvailability(UpdateProductStockRequestDTO requestDTO) {
        UpdateProductStockRequest request = UpdateProductStockRequest.newBuilder()
                .setProductUuid(requestDTO.getProductUuid())
                .setStockAvailable(requestDTO.getStockAvailable())
                .build();
        try {
            ApiResponse response = this.productServiceBlockingStub.updateStockAvailability(request);
            if (response.getSuccess()) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (StatusRuntimeException e) {
            return processBadRequestResponse(e);
        }
    }

    public ResponseEntity<ApiResponse> checkStockAvailability(String productUuid,
                                                              HttpServletRequest httpServletRequest) {
        SecurityUtils.validateBearerToken(httpServletRequest);
        CheckStockRequest request = CheckStockRequest.newBuilder()
                .setProductUuid(productUuid)
                .build();
        try {
            ApiResponse response = this.productServiceBlockingStub.checkStockAvailability(request);
            if (response.getSuccess()) {
                ProductResponse productResponse = response.getData(0);
                if (productResponse.getQuantity() <= 0) {
                    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.newBuilder()
                            .setSuccess(true)
                            .setCode(HttpStatus.OK.value())
                            .setStatus(HttpStatus.OK.toString())
                            .setMessage(PRODUCT_STOCK_UNAVAILABLE)
                            .build());
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.newBuilder()
                            .setSuccess(true)
                            .setCode(HttpStatus.OK.value())
                            .setStatus(HttpStatus.OK.toString())
                            .setMessage(PRODUCT_STOCK_AVAILABLE)
                            .addData(response.getData(0))
                            .build());
                }
            }
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (StatusRuntimeException e) {
            return processBadRequestResponse(e);
        }
    }

    public ResponseEntity<ApiResponse> getProductById(String productUuid,
                                                      HttpServletRequest httpServletRequest) {
        SecurityUtils.validateBearerToken(httpServletRequest);
        CheckStockRequest request = CheckStockRequest.newBuilder()
                .setProductUuid(productUuid)
                .build();
        try {
            ApiResponse response = this.productServiceBlockingStub.checkStockAvailability(request);
            if (response.getSuccess()) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (StatusRuntimeException e) {
            return processBadRequestResponse(e);
        }
    }

    public ResponseEntity<ApiResponse> viewAllProducts(Integer page, Integer size,
                                                       HttpServletRequest httpServletRequest) {
        SecurityUtils.validateBearerToken(httpServletRequest);
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
            return processBadRequestResponse(e);
        }
    }

    public ResponseEntity<ApiResponse> deleteProduct(String productUuid,
                                                     HttpServletRequest httpServletRequest) {
        SecurityUtils.validateBearerToken(httpServletRequest);
        DeleteProductRequest request = DeleteProductRequest.newBuilder()
                .setProductUuid(productUuid)
                .build();
        try {
            ApiResponse response = this.productServiceBlockingStub.deleteProduct(request);
            if (response.getSuccess()) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (StatusRuntimeException e) {
            return processBadRequestResponse(e);
        }
    }

    private static ResponseEntity<ApiResponse> processBadRequestResponse(StatusRuntimeException e) {
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
