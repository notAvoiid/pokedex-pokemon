package com.crud.pokemon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;

    public SecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final String ADMIN_ROLE = "ADMIN";
        final String USER_ROLE = "USER";
        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(https -> https.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/pokemon/{id}", "/api/v1/pokemon/findByAll", "/api/v1/pokemon").hasRole(USER_ROLE)
                        .requestMatchers(HttpMethod.POST, "/api/v1/pokemon/save").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.POST, "/api/v1/pokemon/favorite", "/api/v1/pokemon/unfavorite").hasRole(USER_ROLE)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/pokemon/**").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/pokemon/{id}").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, "/api/v1/user").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, "api/v1/user").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, "api/v1/user/${username}").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "api/v1/user/${username}").hasRole(ADMIN_ROLE)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
