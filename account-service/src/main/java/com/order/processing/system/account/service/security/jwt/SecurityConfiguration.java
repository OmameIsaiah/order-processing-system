package com.order.processing.system.account.service.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import static com.order.processing.system.account.service.enums.Permissions.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] AUTH_WHITELIST = {
            "/api-docs/**",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html/**",
            "/swagger-ui/**",
            "/management/health",
            "/management/health/**",
            "/management/info",
            "/management/prometheus",
            "/console/**",
            "/h2-console/**",
            "/api/authenticate",
            "/api/v1/auth/**",
            "/api/v1/users/onboarding/**",
            "/api/v1/users/entrance/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                //.requestMatchers("/api/v1/users/management/find-all").hasAnyAuthority(CAN_VIEW_USERS.name(), CAN_DELETE_USER.name())
                                .requestMatchers("/api/v1/users/management/find-all").hasAnyRole(CAN_VIEW_USERS.name(), CAN_DELETE_USER.name())
                                .requestMatchers("/api/v1/users/management/find-all").hasAnyRole(CAN_VIEW_USERS.name(), CAN_DELETE_USER.name())
                                .requestMatchers("/api/v1/users/management/filter").hasAnyRole(CAN_VIEW_USERS.name(), CAN_DELETE_USER.name())
                                .requestMatchers("/api/v1/users/management/search").hasAnyRole(CAN_VIEW_USERS.name(), CAN_DELETE_USER.name())
                                .requestMatchers("/api/v1/users/management/delete").hasRole(CAN_DELETE_USER.name())
                                .requestMatchers("/api/v1/roles/**").hasAnyRole(CAN_ADD_ROLE.name(), CAN_UPDATE_ROLE.name(), CAN_DELETE_ROLE.name())

                                /*
                                .requestMatchers(GET, "/**").permitAll()
                                .requestMatchers(POST, "/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/**").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/**").permitAll()
                                */

                                //FOR NOW THIS WILL BE OVERRIDDEN
                                .requestMatchers("/api/**").authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions ->
                        exceptions
                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());
        return http.build();
    }
}
