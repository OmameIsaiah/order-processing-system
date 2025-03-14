package com.order.processing.system.account.service.route;


import com.order.processing.system.account.service.dto.request.RoleRequest;
import com.order.processing.system.account.service.dto.request.RoleUpdateRequest;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.service.RoleAndPermissionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.order.processing.system.account.service.utils.EndpointsURL.*;


@RestController
@RequestMapping(value = ROLES_BASE_URL, headers = "Accept=application/json")
@Tag(name = "roles and permissions route", description = "Endpoints for creating, updating and viewing roles and permissions [Accessible only to ADMIN users with valid authorization]")
@RequiredArgsConstructor
public class RoleAndPermissionsRoute {
    private final RoleAndPermissionsService roleAndPermissionsService;

    @GetMapping(value = VIEW_PERMISSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for viewing list of permissions",
            description = "Endpoint for viewing list of permissions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewPermissions() {
        return roleAndPermissionsService.viewPermissions();
    }

    @PostMapping(value = ADD_NEW_ROLE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for adding new custom role",
            description = "Endpoint for adding new custom role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> addRole(@RequestBody @Valid RoleRequest request) {
        return roleAndPermissionsService.addRole(request);
    }

    @PutMapping(value = UPDATE_ROLE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for updating existing role via the role uuid",
            description = "Endpoint for updating existing role via the role uuid")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updateRole(@RequestBody @Valid RoleUpdateRequest request) {
        return roleAndPermissionsService.updateRole(request);
    }

    @GetMapping(value = VIEW_ALL_ROLES, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for viewing all roles",
            description = "Endpoint for viewing all roles")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewAllRoles() {
        return roleAndPermissionsService.viewAllRoles();
    }

    @GetMapping(value = VIEW_ROLE_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for viewing role by uuid",
            description = "Endpoint for viewing role by uuid")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewRoleByID(@PathVariable("uuid") String uuid) {
        return roleAndPermissionsService.viewRoleByID(uuid);
    }
}
