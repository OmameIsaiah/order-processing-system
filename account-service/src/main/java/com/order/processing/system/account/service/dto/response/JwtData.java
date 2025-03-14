package com.order.processing.system.account.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import static com.order.processing.system.account.service.utils.Utils.TOKEN_PREFIX;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtData implements Serializable {
    private String authorizationToken;
    private String type = TOKEN_PREFIX;
    private List<String> roles;
    private List<String> permissions;
}
