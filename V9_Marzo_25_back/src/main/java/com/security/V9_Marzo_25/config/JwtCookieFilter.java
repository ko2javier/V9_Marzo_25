package com.security.V9_Marzo_25.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;


public class JwtCookieFilter extends OncePerRequestFilter implements ApplicationContextAware {

    private static ApplicationContext context;
    private final JwtDecoder jwtDecoder;

    public JwtCookieFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(cookie -> "access_token".equals(cookie.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        try {
                            String token = cookie.getValue();
                            Jwt jwt = jwtDecoder.decode(token);
                            System.out.println("Cookie encontrada: " + cookie.getName() + " = " + cookie.getValue());
                            
                            // Obtener el servicio dinámicamente para evitar dependencias cíclicas
                            var userDetailsService = context.getBean("userDetailsService", org.springframework.security.core.userdetails.UserDetailsService.class);
                            String username = jwt.getSubject();
                            var userDetails = userDetailsService.loadUserByUsername(username);
                            
                            JwtAuthenticationToken authToken = new JwtAuthenticationToken(jwt, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        } catch (Exception e) {
                            logger.error("JWT inválido: " + e.getMessage());
                        }
                    });
        }
        
        filterChain.doFilter(request, response);
    }
}
