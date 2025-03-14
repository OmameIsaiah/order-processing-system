package com.order.processing.system.account.service.route;


import com.order.processing.system.account.service.dto.request.UpdatePasswordRequest;
import com.order.processing.system.account.service.dto.request.UpdateProfileRequest;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.order.processing.system.account.service.utils.EndpointsURL.*;


@RestController
@RequestMapping(value = PROFILE_BASE_URL, headers = "Accept=application/json")
@Tag(name = "profile route", description = "Endpoints for fetching and updating user profile info, updating password and signing out [Accessible to ALL users with valid authorization]")
@RequiredArgsConstructor
public class UserProfileRoute {
    private final UserProfileService userProfileService;

    @GetMapping(value = PROFILE_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for fetching user profile info",
            description = "Endpoint for fetching user profile info")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> getProfileInfo(HttpServletRequest httpServletRequest) {
        return userProfileService.getProfileInfo(httpServletRequest);
    }

    @PutMapping(value = PROFILE_UPDATE_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for updating user profile info",
            description = "Endpoint for updating user profile info")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updateProfileInfo(@RequestBody @Valid UpdateProfileRequest request, HttpServletRequest httpServletRequest) {
        return userProfileService.updateProfileInfo(httpServletRequest, request);
    }

    @PutMapping(value = PROFILE_UPDATE_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for updating password",
            description = "Endpoint for updating password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody @Valid UpdatePasswordRequest request, HttpServletRequest httpServletRequest) {
        return userProfileService.updatePassword(httpServletRequest, request);
    }

    @PostMapping(value = PROFILE_SIGNOUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for signing out",
            description = "Endpoint for signing out")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> signOut(HttpServletRequest httpServletRequest) {
        return userProfileService.signOut(httpServletRequest);
    }
}
