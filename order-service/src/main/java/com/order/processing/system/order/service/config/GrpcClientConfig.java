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

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public ManagedChannel grpcChannel() {
        String host = "localhost";
        int port = 2002;
        if ("test".equalsIgnoreCase(profile)) {
            host = "localhost";
            port = 3002;
        } else if ("prod".equalsIgnoreCase(profile)) {
            host = "localhost";
            port = 4002;
        }
        log.info("Connecting to gRPC server at " + host + ":" + port);
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // Use TLS in production
                .build();
    }

    @Bean
    public ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub(ManagedChannel channel) {
        return ProductServiceGrpc.newBlockingStub(channel);
    }
}
