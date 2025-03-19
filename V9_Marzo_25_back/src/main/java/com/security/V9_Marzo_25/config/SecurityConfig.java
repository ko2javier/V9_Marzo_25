package com.security.V9_Marzo_25.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ISSUER_URI = "https://dev-hn6ws7o3ayfpoqpa.eu.auth0.com/";
    private static final String EXPECTED_AUDIENCE = "https://dev-hn6ws7o3ayfpoqpa.eu.auth0.com/api/v2/";

    //  Declaramos un @Bean para JwtDecoder para evitar problemas de inyección
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(ISSUER_URI + ".well-known/jwks.json").build();
        
        jwtDecoder.setJwtValidator(jwt -> {
            if (!jwt.getAudience().contains(EXPECTED_AUDIENCE)) {
                return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid audience", null));
            }
            return OAuth2TokenValidatorResult.success();
        });

        return jwtDecoder;
    }

    //  Creamos el filtro de cookies y le pasamos el JwtDecoder inyectado correctamente
    @Bean
    public JwtCookieFilter jwtCookieFilter(JwtDecoder jwtDecoder) {
        return new JwtCookieFilter(jwtDecoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtCookieFilter jwtCookieFilter) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfiguration = new CorsConfiguration();
                corsConfiguration.addAllowedOrigin("http://localhost:4200");
                corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                corsConfiguration.addAllowedHeader("*");
                corsConfiguration.setAllowCredentials(true);
                return corsConfiguration;
            }))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/login", "/api/auth/register","/api/auth/protected").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF
            .oauth2ResourceServer(oauth2 -> oauth2.jwt())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //  Evita el problema de inyección pasando el filtro correctamente
        http.addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
