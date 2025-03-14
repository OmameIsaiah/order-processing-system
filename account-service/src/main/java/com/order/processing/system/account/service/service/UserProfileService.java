package com.order.processing.system.account.service.service;


import com.order.processing.system.account.service.dto.request.UpdatePasswordRequest;
import com.order.processing.system.account.service.dto.request.UpdateProfileRequest;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserProfileService {
    ResponseEntity<ApiResponse> getProfileInfo(HttpServletRequest httpServletRequest);

    ResponseEntity<ApiResponse> updateProfileInfo(HttpServletRequest httpServletRequest, UpdateProfileRequest request);

    ResponseEntity<ApiResponse> updatePassword(HttpServletRequest httpServletRequest, UpdatePasswordRequest request);

    ResponseEntity<ApiResponse> signOut(HttpServletRequest httpServletRequest);
}
