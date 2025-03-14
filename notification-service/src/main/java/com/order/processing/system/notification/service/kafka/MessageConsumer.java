
package com.order.processing.system.notification.service.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.processing.system.notification.service.dto.request.OTPNotificationRequest;
import com.order.processing.system.notification.service.dto.request.QueueRequest;
import com.order.processing.system.notification.service.service.EmailNotificationService;
import com.order.processing.system.notification.service.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.order.processing.system.notification.service.utils.Utils.EMAIL_SIGNUP_OTP;
import static com.order.processing.system.notification.service.utils.Utils.TOPIC_NOTIFICATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {
    private final ObjectMapper objectMapper;
    private final EmailNotificationService notificationService;

    @KafkaListener(topics = "" + TOPIC_NOTIFICATION + "", groupId = "notification-group-id")
    public void messageConsumer(String message) {
        log.info("######################### Event Received: {}", message);
        if (Objects.nonNull(message)) {
            try {
                QueueRequest request = Utils.getGson().fromJson(message, QueueRequest.class);
                switch (request.getType()) {

                    case EMAIL_SIGNUP_OTP -> {
                        notificationService.sendOTPNotification(Utils.getGson().fromJson(objectMapper.writeValueAsString(request.getData()),
                                OTPNotificationRequest.class));
                    }
                    default -> {
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error converting data");
            }
        }
    }
}