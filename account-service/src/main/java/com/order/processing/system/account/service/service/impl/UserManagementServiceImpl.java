package com.order.processing.system.account.service.service.impl;


import com.order.processing.system.account.service.dto.response.ApiResponse;
import com.order.processing.system.account.service.enums.UserType;
import com.order.processing.system.account.service.exceptions.BadRequestException;
import com.order.processing.system.account.service.exceptions.RecordNotFoundException;
import com.order.processing.system.account.service.model.Users;
import com.order.processing.system.account.service.repository.UserRepository;
import com.order.processing.system.account.service.repository.UserRoleRepository;
import com.order.processing.system.account.service.service.UserManagementService;
import com.order.processing.system.account.service.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.order.processing.system.account.service.utils.AppMessages.*;


@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllUsers(Integer page, Integer size) {
        Page<Users> list = userRepository.findAll(PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_USER_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserProfileResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> filterUsers(Integer page, Integer size, UserType userType) {
        Page<Users> list = userRepository.findUsersByType(
                userType,
                PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_USER_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserProfileResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> searchUsers(String keyword) {
        if (Objects.isNull(keyword)) {
            throw new BadRequestException(NULL_KEYWORD_PARAM);
        }
        List<Users> list = userRepository.searchUsers(keyword);
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_USER_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserProfileResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteUser(String uuid) {
        if (Objects.isNull(uuid)) {
            throw new BadRequestException(NULL_UUID_PARAM);
        }
        Optional<Users> optional = userRepository.findUserByUUID(uuid);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(NO_USER_FOUND_WITH_UUID);
        }
        userRoleRepository.deleteUserRoleByUserId(optional.get().getId());
        userRepository.deleteById(optional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_DELETED_SUCCESSFULLY
                ));
    }
}
