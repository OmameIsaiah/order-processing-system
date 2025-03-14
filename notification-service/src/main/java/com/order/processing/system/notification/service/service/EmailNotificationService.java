package com.order.processing.system.notification.service.service;

import com.order.processing.system.notification.service.dto.request.OTPNotificationRequest;

public interface EmailNotificationService {
    void sendOTPNotification(OTPNotificationRequest request);

}
