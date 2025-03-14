package com.order.processing.system.account.service.security.user;

import com.order.processing.system.account.service.security.jwt.JwtResponse;
import com.order.processing.system.account.service.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse getAccessToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new JwtResponse(
                jwtUtils.generateJwtToken(authentication),
                userDetails.getUserType(),
                userDetails.getUsername(),
                userDetails.getRoles().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList()),
                formatPermissions(userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList())));
    }

    private List<String> formatPermissions(List<String> permissions) {
        StringBuilder stringBuilder = new StringBuilder();
        permissions.forEach(permission -> {
            stringBuilder.append(permission.replaceAll("ROLE_", "")).append(",");
        });
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return Arrays.stream(stringBuilder.toString().split(",")).toList();
    }
}
