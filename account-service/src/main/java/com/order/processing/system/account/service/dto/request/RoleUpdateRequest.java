package com.order.processing.system.account.service.dto.request;


import com.order.processing.system.account.service.enums.Permissions;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

import static com.order.processing.system.account.service.utils.AppMessages.*;

@Data
public class RoleUpdateRequest implements Serializable {
    @NotNull(message = NULL_ROLE_UUID)
    @NotEmpty(message = EMPTY_ROLE_UUID)
    private String uuid;
    @NotNull(message = NULL_ROLE_NAME)
    @NotEmpty(message = EMPTY_ROLE_NAME)
    private String roleName;
    private String description;
    private Set<Permissions> permissions;
}
