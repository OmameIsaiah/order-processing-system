package com.order.processing.system.order.service.config;

import com.order.processing.system.proto.ProductServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GrpcClientConfig {

    @Value("${app.grpc.inventory-service.host}")
    private String inventoryServiceGprsHost;
    @Value("${app.grpc.inventory-service.port}")
    private Integer inventoryServiceGprsPort;

    @Bean
    public ManagedChannel grpcChannel() {
        log.info("Connecting to gRPC server at " + inventoryServiceGprsHost + ":" + inventoryServiceGprsPort);
        return ManagedChannelBuilder.forAddress(inventoryServiceGprsHost, inventoryServiceGprsPort)
                .usePlaintext() // Use TLS in production
                .build();
    }

    @Bean
    public ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub(ManagedChannel channel) {
        return ProductServiceGrpc.newBlockingStub(channel);
    }
}
