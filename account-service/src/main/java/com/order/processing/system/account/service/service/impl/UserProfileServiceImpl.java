package com.order.processing.system.account.service.service.impl;

import com.order.processing.system.account.service.dto.request.UpdatePasswordRequest;
import com.order.processing.system.account.service.dto.request.UpdateProfileRequest;
import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.exceptions.BadRequestException;
import com.order.processing.system.account.service.exceptions.RecordNotFoundException;
import com.order.processing.system.account.service.exceptions.UnauthorizedException;
import com.order.processing.system.account.service.model.Users;
import com.order.processing.system.account.service.repository.UserRepository;
import com.order.processing.system.account.service.security.jwt.JwtUtils;
import com.order.processing.system.account.service.service.UserProfileService;
import com.order.processing.system.account.service.utils.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.order.processing.system.account.service.utils.AppMessages.*;


@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private Users validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    private String validateAuthorizedUser(HttpServletRequest httpServletRequest) {
        if (Objects.isNull(httpServletRequest)) {
            throw new UnauthorizedException(INVALID_AUTHORIZATION_TOKEN);
        }
        String username = jwtUtils.getUsernameFromHttpServletRequest(httpServletRequest);
        if (Objects.isNull(username)) {
            throw new UnauthorizedException(INVALID_AUTHORIZATION_TOKEN);
        }
        return username;
    }

    @Override
    public ResponseEntity<ApiResponse> getProfileInfo(HttpServletRequest httpServletRequest) {
        Users users = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PROFILE_RETRIEVED_SUCCESSFULLY,
                        Mapper.mapUserProfileResponse(users)
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> updateProfileInfo(HttpServletRequest httpServletRequest, UpdateProfileRequest request) {
        validateProfileUpdateRequest(request);
        Users users = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        users.setName(request.getName());
        users = userRepository.save(users);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PROFILE_UPDATED_SUCCESSFULLY,
                        Mapper.mapUserProfileResponse(users)
                ));
    }

    private static void validateProfileUpdateRequest(UpdateProfileRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(request.getName()) || "".equals(request.getName())) {
            throw new BadRequestException(NULL_NAME_PARAM);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updatePassword(HttpServletRequest httpServletRequest, UpdatePasswordRequest request) {
        validatePasswordUpdateRequest(request);
        Users users = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        if (!passwordEncoder.matches(request.getCurrentPassword(), users.getPassword())) {
            throw new BadRequestException(WRONG_CURRENT_PASSWORD);
        }
        users.setPassword(passwordEncoder.encode(request.getNewPassword()));
        users = userRepository.save(users);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PASSWORD_UPDATED_SUCCESSFULLY,
                        Mapper.mapUserProfileResponse(users)
                ));
    }

    private static void validatePasswordUpdateRequest(UpdatePasswordRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(request.getCurrentPassword()) || "".equals(request.getCurrentPassword())) {
            throw new BadRequestException(NULL_PASSWORD);
        }
        if (Objects.isNull(request.getNewPassword()) || "".equals(request.getNewPassword())) {
            throw new BadRequestException(NULL_PASSWORD);
        }
        if (Objects.isNull(request.getConfirmNewPassword()) || "".equals(request.getConfirmNewPassword())) {
            throw new BadRequestException(NULL_PASSWORD);
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BadRequestException(PASSWORD_MISMATCH);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> signOut(HttpServletRequest httpServletRequest) {
        Users users = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        users.setIsOnline(false);
        userRepository.save(users);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        SIGN_OUT_SUCCESSFULLY
                ));
    }
}
