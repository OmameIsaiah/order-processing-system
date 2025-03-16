package com.order.processing.system.inventory.service;

import com.order.processing.system.proto.AddProductRequest;
import com.order.processing.system.proto.ApiResponse;
import com.order.processing.system.proto.ProductResponse;
import com.order.processing.system.proto.ProductServiceGrpc;
import io.grpc.ManagedChannel;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.client.GrpcClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class InventoryServiceApplicationTests {

    @Bean
    public ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub(ManagedChannel channel) {
        return ProductServiceGrpc.newBlockingStub(channel);
    }

    @Mock
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    @Test
    void test() {
        AddProductRequest request = AddProductRequest.newBuilder()
                .setName("John Doe")
                .setQuantity(100)
                .setUnitPrice(50)
                .build();
        ApiResponse response = productServiceBlockingStub.addProduct(request);
        ProductResponse productResponse = response.getData(0);
        assertEquals(request.getName(), productResponse.getName());
        assertNotNull(productResponse.getUuid());
    }


}
