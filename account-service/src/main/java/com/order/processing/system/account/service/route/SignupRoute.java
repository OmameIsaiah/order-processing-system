package com.order.processing.system.account.service.route;


import com.order.processing.system.account.service.dto.request.SendOTPRequest;
import com.order.processing.system.account.service.dto.request.SignUpRequest;
import com.order.processing.system.account.service.dto.request.VerifyOTPRequest;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.service.SignUpService;
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
@RequestMapping(value = ONBOARDING_BASE_URL, headers = "Accept=application/json")
@Tag(name = "signup/onboarding route", description = "Endpoints for creating new user account and verifying the account [Accessible to PUBLIC, authorization NOT required]")
@RequiredArgsConstructor
public class SignupRoute {
    private final SignUpService signUpService;

    @PostMapping(value = ONBOARDING_SIGNUP, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for creating new user account",
            description = "Endpoint for creating new user account")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        return signUpService.signUp(request);
    }

    @PostMapping(value = ONBOARDING_SEND_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for sending or resending signup otp for email verification",
            description = "Endpoint for sending or resending signup otp for email verification")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> sendOTP(@RequestBody @Valid SendOTPRequest request) {
        return signUpService.sendOTP(request);
    }

    @PostMapping(value = ONBOARDING_VERIFY_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for verifying account email via otp",
            description = "Endpoint for verifying account email via otp")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> verifyOTP(@RequestBody @Valid VerifyOTPRequest request) {
        return signUpService.verifyOTP(request);
    }
}
