package com.order.processing.system.account.service.route;

import com.order.processing.system.account.service.dto.request.Credentials;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.order.processing.system.account.service.utils.EndpointsURL.AUTH_BASE_URL;
import static com.order.processing.system.account.service.utils.EndpointsURL.AUTH_TOKEN_URL;

@RestController
@RequestMapping(value = AUTH_BASE_URL, headers = "Accept=application/json")
@Tag(name = "authentication route", description = "Endpoint for user authentication [Accessible to ALL users, authorization NOT required]")
@RequiredArgsConstructor
public class AuthRoute {
    private final AuthenticationService authenticationService;

    @PostMapping(value = AUTH_TOKEN_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Endpoint for authenticating a user",
            description = "Endpoint for authenticating a user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> authenticate(@RequestBody @Valid Credentials credentials) {
        return authenticationService.authenticate(credentials);
    }
}
