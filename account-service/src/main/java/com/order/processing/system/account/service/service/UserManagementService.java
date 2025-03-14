package com.order.processing.system.account.service.service;

import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.enums.UserType;
import org.springframework.http.ResponseEntity;

public interface UserManagementService {
    ResponseEntity<ApiResponse> getAllUsers(Integer page, Integer size);

    ResponseEntity<ApiResponse> filterUsers(Integer page, Integer size, UserType userType);

    ResponseEntity<ApiResponse> searchUsers(String keyword);

    ResponseEntity<ApiResponse> deleteUser(String uuid);
}
