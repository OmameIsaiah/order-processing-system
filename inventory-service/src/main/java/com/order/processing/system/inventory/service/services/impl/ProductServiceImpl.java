package com.order.processing.system.inventory.service.services.impl;


import com.order.processing.system.inventory.service.dto.request.AddProductRequestDTO;
import com.order.processing.system.inventory.service.dto.request.UpdateProductRequestDTO;
import com.order.processing.system.inventory.service.dto.request.UpdateProductStockRequestDTO;
import com.order.processing.system.inventory.service.dto.response.ApiResponse;
import com.order.processing.system.inventory.service.exceptions.DuplicateRecordException;
import com.order.processing.system.inventory.service.exceptions.RecordNotFoundException;
import com.order.processing.system.inventory.service.model.Product;
import com.order.processing.system.inventory.service.repository.ProductRepository;
import com.order.processing.system.inventory.service.services.ProductService;
import com.order.processing.system.inventory.service.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.order.processing.system.inventory.service.util.AppMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<ApiResponse> addProduct(AddProductRequestDTO productRequestDTO) {
        checkForProductNameIfExists(productRequestDTO.getName());
        Product mappedProduct = Mapper.mapProductRequestDTOToEntityClass(productRequestDTO);
        mappedProduct.setUuid(UUID.randomUUID().toString());
        Product product = productRepository.save(mappedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        PRODUCT_ADDED_SUCCESSFULLY,
                        Mapper.mapProductToResponseDTO(product)
                ));
    }

    private void checkForProductNameIfExists(String productName) {
        Optional<Product> optional = productRepository.findProductByName(productName);
        if (optional.isPresent()) {
            throw new DuplicateRecordException(PRODUCT_ALREADY_EXISTS);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateProduct(UpdateProductRequestDTO updateProductRequestDTO) {
        Optional<Product> optional = validateProductUuid(updateProductRequestDTO.getProductUuid());
        Product product = mapAndSaveUpdatedProduct(updateProductRequestDTO, optional);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PRODUCT_UPDATED_SUCCESSFULLY,
                        Mapper.mapProductToResponseDTO(product)
                ));
    }

    private Product mapAndSaveUpdatedProduct(UpdateProductRequestDTO requestDTO, Optional<Product> optional) {
        Product product = optional.get();
        product.setName(requestDTO.getName());
        product.setQuantity(requestDTO.getQuantity());
        product.setLastModified(LocalDateTime.now());
        return productRepository.save(product);
    }

    private Optional<Product> validateProductUuid(String productUuid) {
        Optional<Product> optional = productRepository.findProductByUuid(productUuid);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(NO_PRODUCT_FOUND_WITH_ID);
        }
        return optional;
    }

    @Override
    public ResponseEntity<ApiResponse> updateStockAvailability(UpdateProductStockRequestDTO updateProductStockRequestDTO) {
        Optional<Product> optional = validateProductUuid(updateProductStockRequestDTO.getProductUuid());
        Product product = optional.get();
        product.setQuantity(updateProductStockRequestDTO.getStockAvailable());
        product.setLastModified(LocalDateTime.now());
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PRODUCT_AVAILABILITY_SUCCESSFULLY,
                        Mapper.mapProductToResponseDTO(product)
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> checkStockAvailability(String productUuid) {
        Optional<Product> optional = validateProductUuid(productUuid);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PRODUCT_RETRIEVED_SUCCESSFULLY,
                        Mapper.mapProductToResponseDTO(optional.get())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> viewAllProducts(Integer page, Integer size) {
        Sort orders = Sort.by("dateCreated").descending();
        Page<Product> list = productRepository.findAllProducts(PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size), orders));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_PRODUCT_FOUND);
        }
        return Mapper.processProductPageResponse(list);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteProduct(String productUuid) {
        Optional<Product> optional = validateProductUuid(productUuid);
        productRepository.deleteProductByUuid(optional.get().getUuid());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PRODUCT_DELETED_SUCCESSFULLY
                ));
    }
}
