package com.order.processing.system.inventory.service.route;

import com.order.processing.system.inventory.service.dto.request.AddProductRequestDTO;
import com.order.processing.system.inventory.service.dto.request.UpdateProductRequestDTO;
import com.order.processing.system.inventory.service.dto.request.UpdateProductStockRequestDTO;
import com.order.processing.system.inventory.service.dto.response.ApiResponse;
import com.order.processing.system.inventory.service.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.order.processing.system.inventory.service.util.EndpointsURL.*;

@RestController
@RequestMapping(value = PRODUCT_BASE_URL, headers = "Accept=application/json")
@Tag(name = "product route", description = "Endpoints for adding, updating, deleting, viewing and checking stock availability of products")
@RequiredArgsConstructor
public class ProductRoute {
    private final ProductService productService;

    @PostMapping(value = ADD_PRODUCT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for adding new product to inventory",
            description = "Endpoint for adding new product to inventory")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody @Valid AddProductRequestDTO payload) {
        return productService.addProduct(payload);
    }

    @PutMapping(value = UPDATE_PRODUCT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for updating product name and quantity",
            description = "Endpoint for updating product name and quantity")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody @Valid UpdateProductRequestDTO payload) {
        return productService.updateProduct(payload);
    }

    @PatchMapping(value = UPDATE_STOCK_AVAILABILITY, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for updating stock availability of a product",
            description = "Endpoint for updating stock availability of a product")
    public ResponseEntity<ApiResponse> updateStockAvailability(@RequestBody @Valid UpdateProductStockRequestDTO payload) {
        return productService.updateStockAvailability(payload);
    }

    @GetMapping(value = CHECK_PRODUCT_AVAILABILITY, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for checking product availability",
            description = "Endpoint for checking product availability")
    public ResponseEntity<ApiResponse> checkStockAvailability(@RequestParam("productUuid") String productUuid) {
        return productService.checkStockAvailability(productUuid);
    }

    @GetMapping(value = VIEW_ALL_PRODUCTS, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for viewing all products in the inventory",
            description = "Endpoint for viewing all products in the inventory")
    public ResponseEntity<ApiResponse> viewAllProducts(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return productService.viewAllProducts(page, size);
    }

    @DeleteMapping(value = DELETE_PRODUCT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for deleting a product",
            description = "Endpoint for deleting a product")
    public ResponseEntity<ApiResponse> deleteProduct(@RequestParam("productUuid") String productUuid) {
        return productService.deleteProduct(productUuid);
    }
}
