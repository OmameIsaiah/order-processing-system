package com.order.processing.system.account.service.service.impl;

import com.order.processing.system.account.service.dto.request.Credentials;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.exceptions.BadRequestException;
import com.order.processing.system.account.service.exceptions.RecordNotFoundException;
import com.order.processing.system.account.service.model.Users;
import com.order.processing.system.account.service.repository.UserRepository;
import com.order.processing.system.account.service.security.user.JwtTokenService;
import com.order.processing.system.account.service.service.AuthenticationService;
import com.order.processing.system.account.service.utils.AppMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

import static com.order.processing.system.account.service.utils.AppMessages.*;
import static com.order.processing.system.account.service.utils.Utils.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    private Users validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    @Override
    public ResponseEntity authenticate(Credentials credentials) {
        validateUsernameAndPassword(credentials.getEmail(), credentials.getPassword());
        Users users = validateUserByEmail(credentials.getEmail());
        checkAccountVerificationStatusAndPassword(credentials.getPassword(), users);
        String jwt = jwtTokenService.getAccessToken(credentials.getEmail(), credentials.getPassword()).getAuthorizationToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        HashMap<String, String> data = new HashMap<>();
        data.put("authorizationToken", TOKEN_PREFIX + " " + jwt);
        data.put("type", TOKEN_PREFIX);
        log.info("SIGN IN SUCCESSFULLY FOR: {}", credentials.getEmail());
        return new ResponseEntity<>(
                new ApiResponse(true, HttpStatus.OK.value(), HttpStatus.OK,
                        AUTHORIZATION_PROCESSED_SUCCESSFULLY,
                        data),
                httpHeaders, HttpStatus.OK);
    }

    private static void validateUsernameAndPassword(String username, String password) {
        if (Objects.isNull(username) || Objects.isNull(password)) {
            throw new BadRequestException(AppMessages.INVALID_REQUEST_PARAMETERS);
        }
        if ("".equals(username) || "".equals(password)) {
            throw new BadRequestException(AppMessages.INVALID_REQUEST_PARAMETERS);
        }
    }

    private void checkAccountVerificationStatusAndPassword(String password, Users users) {
        if (Objects.isNull(users.getVerified()) || !users.getVerified()) {
            throw new BadRequestException(ACCOUNT_NOT_VERIFIED);
        }
        if (!passwordEncoder.matches(password, users.getPassword())) {
            throw new BadRequestException(WRONG_ACCOUNT_PASSWORD);
        }
    }
}
