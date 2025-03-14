package com.order.processing.system.account.service.dto.response;

import com.order.processing.system.account.service.enums.Permissions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse implements Serializable {
    private String uuid;
    private String name;
    private String description;
    private Set<Permissions> permissions;
}
