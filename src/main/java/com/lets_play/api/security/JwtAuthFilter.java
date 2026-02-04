package com.lets_play.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = auth.substring(7);

        try {
            var claims = jwtService.parseClaims(token);

            String userId = claims.getSubject();
            String username = claims.get("username", String.class);
            String roles = claims.get("roles", String.class);

            List<SimpleGrantedAuthority> authorities = List.of();
            if (roles != null && !roles.isBlank()) {
                authorities = Arrays.stream(roles.split(","))
                        .filter(s -> !s.isBlank())
                        .map(SimpleGrantedAuthority::new)
                        .toList();
            }

            var principal = new AuthPrincipal(userId, username);
            var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // invalid token -> ignore and continue without auth
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
