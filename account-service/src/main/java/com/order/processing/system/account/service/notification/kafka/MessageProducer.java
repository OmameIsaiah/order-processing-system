package com.order.processing.system.account.service.notification.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.processing.system.account.service.dto.request.QueueRequest;
import com.order.processing.system.account.service.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.order.processing.system.account.service.utils.Utils.TOPIC_NOTIFICATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageProducer {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProducer<String, String> kafkaProducer;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Async
    public void sendMessage(String type, Object message) {
        QueueRequest request = QueueRequest.builder()
                .type(type)
                .data(message)
                .build();
        String content = "";
        try {
            content = objectMapper.writeValueAsString(request);

        } catch (JsonProcessingException e) {
            throw new BadRequestException("Error writing content: {}" + e.getMessage());
        }
        log.info("######################### Event Content: {}", content);
        kafkaTemplate.send(TOPIC_NOTIFICATION, content);
    }

    public CompletableFuture<Void> sendMessageAsyncWithExecutorService(String type, Object message) {
        QueueRequest request = QueueRequest.builder()
                .type(type)
                .data(message)
                .build();
        String content = "";
        try {
            content = objectMapper.writeValueAsString(request);

        } catch (JsonProcessingException e) {
            throw new BadRequestException("Error writing content: {}" + e.getMessage());
        }
        String finalContent = content;
        log.info("######################### Event Content: {}", finalContent);
        return CompletableFuture.runAsync(() -> {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NOTIFICATION, type, finalContent);
            kafkaProducer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Error sending Kafka message: " + exception.getMessage());
                } else {
                    log.info("Message sent successfully to Kafka");
                }
            });
        }, executorService);
    }

    public void sendMessageAsyncWithCompletableFuture(String type, Object message) {
        QueueRequest request = QueueRequest.builder()
                .type(type)
                .data(message)
                .build();
        String content = "";
        try {
            content = objectMapper.writeValueAsString(request);

        } catch (JsonProcessingException e) {
            throw new BadRequestException("Error writing content: {}" + e.getMessage());
        }
        String finalContent = content;
        log.info("######################### Event Content: {}", finalContent);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NOTIFICATION, type, finalContent);
        kafkaProducer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Error sending Kafka message: " + exception.getMessage());
            } else {
                log.info("Message sent to topic: " + metadata.topic() + " at offset " + metadata.offset());
            }
        });
    }
}