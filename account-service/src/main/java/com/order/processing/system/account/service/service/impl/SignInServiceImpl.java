package com.order.processing.system.account.service.service.impl;

import com.order.processing.system.account.service.dto.request.Credentials;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.dto.response.JwtData;
import com.order.processing.system.account.service.dto.response.SignInResponse;
import com.order.processing.system.account.service.dto.response.UserData;
import com.order.processing.system.account.service.exceptions.BadRequestException;
import com.order.processing.system.account.service.exceptions.RecordNotFoundException;
import com.order.processing.system.account.service.model.Users;
import com.order.processing.system.account.service.repository.UserRepository;
import com.order.processing.system.account.service.security.jwt.JwtResponse;
import com.order.processing.system.account.service.security.user.JwtTokenService;
import com.order.processing.system.account.service.service.SignInService;
import com.order.processing.system.account.service.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.order.processing.system.account.service.utils.AppMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignInServiceImpl implements SignInService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    private Users validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    @Override
    public ResponseEntity<ApiResponse> signIn(Credentials credentials) {
        Users users = validateSignInParamAndPassword(credentials);
        UserData userData = getUserData(users);
        JwtResponse jwtResponse = jwtTokenService.getAccessToken(credentials.getEmail(), credentials.getPassword());
        log.info("SIGN IN SUCCESSFULLY FOR: {}", credentials.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        SIGN_IN_SUCCESSFULLY,
                        SignInResponse.builder()
                                .userAccountData(userData)
                                .jwtTokenData(JwtData.builder()
                                        .authorizationToken(jwtResponse.getAuthorizationToken())
                                        .type(jwtResponse.getType())
                                        .roles(jwtResponse.getRoles())
                                        .permissions(jwtResponse.getPermissions())
                                        .build())
                                .build()
                ));
    }

    private UserData getUserData(Users users) {
        users.setIsOnline(true);
        users.setLastLogin(LocalDateTime.now());
        userRepository.save(users);
        return UserData.builder()
                .uuid(users.getUuid())
                .name(users.getName())
                .email(users.getEmail())
                .userType(users.getUserType())
                .userToken(users.getUserToken())
                .isOnline(users.getIsOnline())
                .lastLogin(Utils.convertLocalDateTimeToString(users.getLastLogin()))
                .build();
    }

    private Users validateSignInParamAndPassword(Credentials credentials) {
        if (Objects.isNull(credentials)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(credentials.getEmail()) || "".equals(credentials.getEmail())) {
            throw new BadRequestException(NULL_EMAIL);
        }
        if (Objects.isNull(credentials.getPassword()) || "".equals(credentials.getPassword())) {
            throw new BadRequestException(NULL_PASSWORD);
        }
        Users users = validateUserByEmail(credentials.getEmail());
        if (Objects.isNull(users.getVerified()) || !users.getVerified()) {
            throw new BadRequestException(ACCOUNT_NOT_VERIFIED);
        }
        if (!passwordEncoder.matches(credentials.getPassword(), users.getPassword())) {
            throw new BadRequestException(WRONG_ACCOUNT_PASSWORD);
        }
        return users;
    }
}
