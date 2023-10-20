package com.exe201.beana.config;

import com.exe201.beana.filter.AuthoritiesLoggingAfterFilter;
import com.exe201.beana.filter.AuthoritiesLoggingAtFilter;
import com.exe201.beana.filter.JWTAuthenticationFilter;
import com.exe201.beana.filter.RequestValidationBeforeFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthoritiesLoggingAfterFilter authoritiesLoggingAfterFilter;
    //    private final CsrfCookieFilter csrfCookieFilter;
    private final AuthoritiesLoggingAtFilter authoritiesLoggingAtFilter;
    private final AuthenticationProvider authenticationProvider;
    private final RequestValidationBeforeFilter requestValidationBeforeFilter;
//    private final CsrfTokenResponseHeaderBindingFilter csrfTokenResponseHeaderBindingFilter;
    private final static String ROLE_MANAGER = "MANAGER";
    private final static String ROLE_CUSTOMER = "CUSTOMER";
    private final AuthEntryPoint authenticationEntryPoint;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
//        requestHandler.setCsrfRequestAttributeName("_csrf");
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
//                        config.setAllowedOriginPatterns(List.of("*"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }));
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider)
//                .addFilterAfter(csrfCookieFilter, BasicAuthenticationFilter.class)
//                .addFilterAfter(csrfTokenResponseHeaderBindingFilter, CsrfFilter.class)
                .addFilterBefore(requestValidationBeforeFilter, BasicAuthenticationFilter.class)
                .addFilterAt(authoritiesLoggingAtFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(authoritiesLoggingAfterFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // product
                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.POST, "api/v1/upload").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        // category
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories").permitAll()
                        // reputation
                        .requestMatchers(HttpMethod.POST, "api/v1/reputations").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.PUT, "api/v1/reputations").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "api/v1/reputations").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.GET, "api/v1/reputations").permitAll()
                        // skin
                        .requestMatchers(HttpMethod.POST, "api/v1/skins").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.PUT, "api/v1/skins").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "api/v1/skins").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.GET, "api/v1/skins").permitAll()
                        // child category
                        .requestMatchers(HttpMethod.POST, "api/v1/childcategories").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.PUT, "api/v1/childcategories").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "api/v1/childcategories").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.GET, "api/v1/childcategories").permitAll()
                        // payment
                        .requestMatchers(HttpMethod.POST, "api/v1/payments").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.PUT, "api/v1/payments").hasRole(ROLE_MANAGER)
                        .requestMatchers(HttpMethod.GET, "api/v1/payments").permitAll()
                        // cart
                        .requestMatchers("api/v1/cart/**").permitAll()
                        // order
                        .requestMatchers(HttpMethod.POST, "api/v1/orders").hasAnyRole(ROLE_CUSTOMER, ROLE_MANAGER)
                        .requestMatchers(HttpMethod.GET, "api/v1/orders").hasAnyRole(ROLE_CUSTOMER, ROLE_MANAGER)
                        // address
                        .requestMatchers(HttpMethod.POST, "api/v1/addresses").hasAnyRole(ROLE_CUSTOMER, ROLE_MANAGER)
                        .requestMatchers(HttpMethod.PUT, "api/v1/addresses").hasAnyRole(ROLE_CUSTOMER, ROLE_MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "api/v1/addresses").hasAnyRole(ROLE_CUSTOMER, ROLE_MANAGER)
                        .requestMatchers(HttpMethod.GET, "api/v1/addresses/**").permitAll());
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint));


        return http.build();
    }


}

