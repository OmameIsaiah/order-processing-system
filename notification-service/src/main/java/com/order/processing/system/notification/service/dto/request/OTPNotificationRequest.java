package com.order.processing.system.notification.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPNotificationRequest implements Serializable {
    private String name;
    private String email;
    private String otp;
}
