package com.order.processing.system.account.service.service;

import com.order.processing.system.account.service.dto.request.SendOTPRequest;
import com.order.processing.system.account.service.dto.request.SignUpRequest;
import com.order.processing.system.account.service.dto.request.VerifyOTPRequest;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SignUpService {
    ResponseEntity<ApiResponse> signUp(SignUpRequest request);

    ResponseEntity<ApiResponse> sendOTP(SendOTPRequest request);

    ResponseEntity<ApiResponse> verifyOTP(VerifyOTPRequest request);
}
