package com.order.processing.system.account.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

import static com.order.processing.system.account.service.utils.AppMessages.*;

@Data
public class UpdatePasswordRequest implements Serializable {
    @NotNull(message = NULL_CURRENT_PASSWORD_PARAM)
    @NotEmpty(message = EMPTY_CURRENT_PASSWORD_PARAM)
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String currentPassword;
    @NotNull(message = NULL_PASSWORD_PARAM)
    @NotEmpty(message = EMPTY_PASSWORD_PARAM)
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String newPassword;
    @NotNull(message = NULL_PASSWORD_PARAM)
    @NotEmpty(message = EMPTY_PASSWORD_PARAM)
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String confirmNewPassword;
}
