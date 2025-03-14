package com.order.processing.system.account.service.service;

import com.order.processing.system.account.service.dto.request.Credentials;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SignInService {

    ResponseEntity<ApiResponse> signIn(Credentials credentials);
}
