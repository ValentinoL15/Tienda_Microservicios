package com.valentino.users_service.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities() != null) {
                // Obtenemos el token que pasamos en el filtro JwtTokenValidator
                if(auth instanceof UsernamePasswordAuthenticationToken) {
                    Object credentials = auth.getCredentials();
                    if (credentials instanceof String token) {
                        template.header("Authorization", "Bearer " + token);
                    }
                }
            }
        };
    }
}

