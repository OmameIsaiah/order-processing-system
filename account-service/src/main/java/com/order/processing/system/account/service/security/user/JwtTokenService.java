package com.order.processing.system.account.service.security.user;


import com.order.processing.system.account.service.security.jwt.JwtResponse;

public interface JwtTokenService {
    JwtResponse getAccessToken(String username, String password);
}
