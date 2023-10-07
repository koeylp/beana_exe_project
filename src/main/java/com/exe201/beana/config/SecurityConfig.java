package com.exe201.beana.config;

import com.exe201.beana.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(List.of("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }));
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
//            http.csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers("/contact", "/register")
//        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//        .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
//        .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
//        .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
//        .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
//        .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
//        .authorizeHttpRequests((requests) -> requests
//        .requestMatchers("/api/v1/products").hasRole("USER")
//        .requestMatchers("/api/v1/").hasAnyRole("USER", "MANAGER")
//        .requestMatchers("/api/v1/").hasRole("USER")
//        .requestMatchers("/api/v1/").hasRole("USER")
//        .requestMatchers("/api/v1/").authenticated()
//        .requestMatchers("/api/v1/auth/*", "/api/v1/", "/api/v1/").permitAll())
//        .formLogin(Customizer.withDefaults())
//        .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

