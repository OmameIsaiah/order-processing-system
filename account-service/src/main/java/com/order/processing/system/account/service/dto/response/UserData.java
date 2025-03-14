package com.order.processing.system.account.service.dto.response;

import com.order.processing.system.account.service.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserData implements Serializable {
    private String uuid;
    private String name;
    private String email;
    private UserType userType;
    private String userToken;
    private Boolean isOnline;
    private String lastLogin;
}
