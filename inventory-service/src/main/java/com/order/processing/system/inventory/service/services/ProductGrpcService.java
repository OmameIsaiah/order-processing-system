package com.order.processing.system.inventory.service.services;

import com.order.processing.system.inventory.service.model.Product;
import com.order.processing.system.inventory.service.repository.ProductRepository;
import com.order.processing.system.inventory.service.util.Mapper;
import com.order.processing.system.proto.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

import static com.order.processing.system.inventory.service.util.AppMessages.*;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductRepository productRepository;

    @Override
    public void addProduct(AddProductRequest request, StreamObserver<ApiResponse> responseObserver) {
        Optional<Product> optional = productRepository.findProductByName(request.getName());
        if (optional.isPresent()) {
            ApiResponse response = ApiResponse.newBuilder()
                    .setSuccess(false)
                    .setCode(HttpStatus.BAD_REQUEST.value())
                    .setStatus(HttpStatus.BAD_REQUEST.toString())
                    .setMessage(PRODUCT_ALREADY_EXISTS)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            Product mappedProduct = Mapper.mapProductRequestDTOToEntityClass(request);
            mappedProduct.setUuid(UUID.randomUUID().toString());
            Product product = productRepository.save(mappedProduct);
            ApiResponse response = ApiResponse.newBuilder()
                    .setSuccess(true)
                    .setCode(HttpStatus.CREATED.value())
                    .setStatus(HttpStatus.CREATED.toString())
                    .setMessage(PRODUCT_ADDED_SUCCESSFULLY)
                    .addData(Mapper.mapProductToProductResponse(product))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateProduct(UpdateProductRequest request, StreamObserver<ApiResponse> responseObserver) {
        Optional<Product> optional = productRepository.findProductByUuid(request.getProductUuid());
        if (optional.isEmpty()) {
            processInvalidProductIdResponse(responseObserver);
        } else {
            Product product = mapAndSaveUpdatedProduct(request, optional);
            ApiResponse response = ApiResponse.newBuilder()
                    .setSuccess(true)
                    .setCode(HttpStatus.OK.value())
                    .setStatus(HttpStatus.OK.toString())
                    .setMessage(PRODUCT_UPDATED_SUCCESSFULLY)
                    .addData(Mapper.mapProductToProductResponse(product))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    private Product mapAndSaveUpdatedProduct(UpdateProductRequest request, Optional<Product> optional) {
        Product product = optional.get();
        product.setName(request.getName());
        product.setQuantity(request.getQuantity());
        product.setUnitPrice(request.getUnitPrice());
        product.setLastModified(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public void updateStockAvailability(UpdateProductStockRequest request, StreamObserver<ApiResponse> responseObserver) {
        Optional<Product> optional = productRepository.findProductByUuid(request.getProductUuid());
        if (optional.isEmpty()) {
            processInvalidProductIdResponse(responseObserver);
        } else {
            Product product = optional.get();
            product.setQuantity(request.getStockAvailable());
            product.setLastModified(LocalDateTime.now());
            productRepository.save(product);
            ApiResponse response = ApiResponse.newBuilder()
                    .setSuccess(true)
                    .setCode(HttpStatus.OK.value())
                    .setStatus(HttpStatus.OK.toString())
                    .setMessage(PRODUCT_STOCK_UPDATED_SUCCESSFULLY)
                    .addData(Mapper.mapProductToProductResponse(product))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void checkStockAvailability(CheckStockRequest request, StreamObserver<ApiResponse> responseObserver) {
        Optional<Product> optional = productRepository.findProductByUuid(request.getProductUuid());
        if (optional.isEmpty()) {
            processInvalidProductIdResponse(responseObserver);
        } else {
            Product product = optional.get();
            ApiResponse response = ApiResponse.newBuilder()
                    .setSuccess(true)
                    .setCode(HttpStatus.OK.value())
                    .setStatus(HttpStatus.OK.toString())
                    .setMessage(PRODUCT_RETRIEVED_SUCCESSFULLY)
                    .addData(Mapper.mapProductToProductResponse(product))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void viewAllProducts(ViewAllProductsRequest request, StreamObserver<ApiResponse> responseObserver) {
        Sort orders = Sort.by("name").descending();
        Page<Product> list = productRepository.findAllProducts(PageRequest.of((Objects.isNull(request.getPage()) ? 0
                : request.getPage()), (Objects.isNull(request.getSize()) ? 50 : request.getSize()), orders));
        if (list.isEmpty() || Objects.isNull(list)) {
            responseObserver.onNext(
                    ApiResponse.newBuilder()
                            .setSuccess(true)
                            .setCode(HttpStatus.OK.value())
                            .setStatus(HttpStatus.OK.toString())
                            .setMessage(NO_PRODUCT_FOUND)
                            .addAllData(new ArrayList<>())
                            .build());
            responseObserver.onCompleted();
        } else {
            List<ProductResponse> productResponseList = Mapper.getListProductResponse(list);
            ApiResponse response = ApiResponse.newBuilder()
                    .setSuccess(true)
                    .setCode(HttpStatus.OK.value())
                    .setStatus(HttpStatus.OK.toString())
                    .setMessage(PRODUCT_RETRIEVED_SUCCESSFULLY)
                    .addAllData(productResponseList)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteProduct(DeleteProductRequest request, StreamObserver<ApiResponse> responseObserver) {
        Optional<Product> optional = productRepository.findProductByUuid(request.getProductUuid());
        if (optional.isEmpty()) {
            processInvalidProductIdResponse(responseObserver);
        } else {
            productRepository.deleteProductByUuid(optional.get().getUuid());
            ApiResponse response = ApiResponse.newBuilder()
                    .setSuccess(true)
                    .setCode(HttpStatus.OK.value())
                    .setStatus(HttpStatus.OK.toString())
                    .setMessage(PRODUCT_DELETED_SUCCESSFULLY)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    private void processInvalidProductIdResponse(StreamObserver<ApiResponse> responseObserver) {
        ApiResponse response = ApiResponse.newBuilder()
                .setSuccess(false)
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setStatus(HttpStatus.BAD_REQUEST.toString())
                .setMessage(NO_PRODUCT_FOUND_WITH_ID)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
