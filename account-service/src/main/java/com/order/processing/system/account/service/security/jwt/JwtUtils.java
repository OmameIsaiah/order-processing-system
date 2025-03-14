package com.order.processing.system.account.service.security.jwt;


import com.order.processing.system.account.service.exceptions.AccessDeniedException;
import com.order.processing.system.account.service.repository.UserRepository;
import com.order.processing.system.account.service.security.user.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.order.processing.system.account.service.security.user.UserDetailsImpl.getListOfPermissionsFromRoles;
import static com.order.processing.system.account.service.utils.AppMessages.INVALID_ACCESS_TOKEN;
import static com.order.processing.system.account.service.utils.Utils.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
public class JwtUtils {
    @Value("${app.jwt.signing.key}")
    private String JWT_SECRET;
    @Value("${app.jwt.expirationDateInMs}")
    private int JWT_EXPIRATION_TIME_IN_MINUTES;
    @Value("${app.jwt.token.validity}")
    public long TOKEN_VALIDITY;
    private String ROLES_KEY = "roles";
    private String TOKEN_ISSUER = "Order Processing System.";
    @Autowired
    private UserRepository userRepository;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("user-type", userPrincipal.getUserType());
        claims.put("sub", userPrincipal.getUsername());
        claims.put("name", userPrincipal.getName());
        claims.put("email", userPrincipal.getUsername());
        claims.put("issuer", TOKEN_ISSUER);
        claims.put(ROLES_KEY, getPermissionsData(userPrincipal));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(TOKEN_VALIDITY, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String generateJwtTokenWithOnlyRoleClaim(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim(ROLES_KEY, getAuthorityData(userPrincipal))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(TOKEN_VALIDITY, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUsernameFromHttpServletRequest(HttpServletRequest httpServletRequest) {
        return getUserNameFromJwtToken(getTokenFromRequestHeader(httpServletRequest));
    }

    private String getTokenFromRequestHeader(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getAuthorityData(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String getAuthorityData(UserDetailsImpl userPrincipal) {
        return userPrincipal.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String getRoles(String username) {
        return userRepository.findUserByEmail(username).get().getUserRoles().stream().filter(Objects::nonNull)
                .map(rs -> rs.getRoleid().getName()).collect(Collectors.joining(","));
    }

    private String getPermissionsData(UserDetailsImpl userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String getPermissions(String username) {
        return userRepository.findUserByEmail(username).get().getUserRoles().stream().filter(Objects::nonNull)
                .map(rs -> getListOfPermissionsFromRoles(rs.getRoleid())).collect(Collectors.joining(","));
    }

    public String getUserNameFromJwtToken(String token) {
        try {
            return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new AccessDeniedException(INVALID_ACCESS_TOKEN);
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        return (getUserNameFromJwtToken(token).equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(final String token, final Authentication existingAuth, final UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(JWT_SECRET);
        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        final Claims claims = claimsJws.getBody();
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ROLES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
