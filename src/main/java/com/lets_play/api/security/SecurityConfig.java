package com.lets_play.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
     private final JsonAccessDeniedHandler accessDeniedHandler;
    private final JsonAuthEntryPoint authEntryPoint;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)      // 403
                        .authenticationEntryPoint(authEntryPoint)      // 401
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/products/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/products/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/products/**").authenticated()

                        .requestMatchers("/users/me").authenticated()
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
