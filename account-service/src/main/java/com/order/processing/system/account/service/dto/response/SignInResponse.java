package com.order.processing.system.account.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse implements Serializable {
    private UserData userAccountData;
    private JwtData jwtTokenData;
}
