package com.order.processing.system.order.service.util;

import com.order.processing.system.order.service.exceptions.AccessDeniedException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.order.processing.system.order.service.util.AppMessages.INVALID_ACCESS_TOKEN;

@Component
@Slf4j
public class SecurityUtils {
    private static String userId = "user-id";
    private static String JWT_SECRET = "20725b85bb97d75a04c275a18f5f1d873c4b7b4cd9b73042508082f968488f94";

    public static String getUserIdFromJwtToken(String token) {
        try {
            Object user = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().get(userId);
            if (Objects.nonNull(user)) {
                return user.toString();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AccessDeniedException(INVALID_ACCESS_TOKEN);
        }
    }

    public static String getTokenFromRequestHeader(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static String getUseridFromRequestHeader(HttpServletRequest httpServletRequest) {
        return getUserIdFromJwtToken(getTokenFromRequestHeader(httpServletRequest));
    }
}
