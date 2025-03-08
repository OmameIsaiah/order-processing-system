package com.order.processing.system.inventory.service.route;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestRoute {
    @GetMapping("/products")
    public ResponseEntity getAllProducts() {
        return ResponseEntity.ok("Ok");
    }
}
