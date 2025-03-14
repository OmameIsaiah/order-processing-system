package com.order.processing.system.account.service.service;

import com.order.processing.system.account.service.dto.request.RoleRequest;
import com.order.processing.system.account.service.dto.request.RoleUpdateRequest;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface RoleAndPermissionsService {
    ResponseEntity<ApiResponse> viewPermissions();

    ResponseEntity<ApiResponse> addRole(RoleRequest request);

    ResponseEntity<ApiResponse> updateRole(RoleUpdateRequest request);

    ResponseEntity<ApiResponse> viewAllRoles();

    ResponseEntity<ApiResponse> viewRoleByID(String uuid);
}