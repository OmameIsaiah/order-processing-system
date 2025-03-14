package com.order.processing.system.account.service.route;


import com.order.processing.system.account.service.dto.request.Credentials;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.service.SignInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.order.processing.system.account.service.utils.EndpointsURL.ENTRANCE_BASE_URL;
import static com.order.processing.system.account.service.utils.EndpointsURL.ENTRANCE_SIGNIN;

@RestController
@RequestMapping(value = ENTRANCE_BASE_URL, headers = "Accept=application/json")
@Tag(name = "signin/entrance route", description = "Endpoints for user sign in [Accessible to ALL users, authorization NOT required]")
@RequiredArgsConstructor
public class SigninRoute {
    private final SignInService signInService;

    @PostMapping(value = ENTRANCE_SIGNIN, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for user sign inr",
            description = "Endpoint for user sign in")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> signIn(@RequestBody @Valid Credentials credentials) {
        return signInService.signIn(credentials);
    }
}
