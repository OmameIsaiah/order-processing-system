package com.order.processing.system.inventory.service.route;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.order.processing.system.inventory.service.util.EndpointsURL.PRODUCT_BASE_URL;

@RestController
@RequestMapping(value = PRODUCT_BASE_URL, headers = "Accept=application/json")
@RequiredArgsConstructor
public class ProductRoute {
    @GetMapping("/products")
    public ResponseEntity getAllProducts() {
        return ResponseEntity.ok("Ok");
    }
}
