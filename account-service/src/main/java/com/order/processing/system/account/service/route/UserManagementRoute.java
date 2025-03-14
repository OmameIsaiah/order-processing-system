package com.order.processing.system.account.service.route;


import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.enums.UserType;
import com.order.processing.system.account.service.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.order.processing.system.account.service.utils.EndpointsURL.*;


@RestController
@RequestMapping(value = USER_MANAGEMENT_BASE_URL, headers = "Accept=application/json")
@Tag(name = "user management route", description = "Endpoints for searching, filtering and deleting users [Accessible only to ADMIN users with valid authorization]")
@RequiredArgsConstructor
public class UserManagementRoute {
    private final UserManagementService userManagementService;

    @GetMapping(value = USER_MANAGEMENT_FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for fetching all users",
            description = "Endpoint for fetching all users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return userManagementService.getAllUsers(page, size);
    }

    @GetMapping(value = USER_MANAGEMENT_FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for filtering users",
            description = "Endpoint for filtering users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> filterUsers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "50") Integer size,
                                                   @RequestParam(value = "userType", defaultValue = "ADMIN") UserType userType) {
        return userManagementService.filterUsers(page, size, userType);
    }

    @GetMapping(value = USER_MANAGEMENT_SEARCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for searching users by name, email or user type",
            description = "Endpoint for searching users by name, email or user type")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> searchUsers(@RequestParam("keyword") String keyword) {
        return userManagementService.searchUsers(keyword);
    }

    @DeleteMapping(value = USER_MANAGEMENT_DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for deleting a user by uuid",
            description = "Endpoint for deleting a user by uuid")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("uuid") String uuid) {
        return userManagementService.deleteUser(uuid);
    }
}
