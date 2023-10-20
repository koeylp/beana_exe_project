//package com.exe201.beana.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//
//import java.io.IOException;
//
//@Configuration
//public class CookieFilterConfig {
//
//    @Bean
//    public OncePerRequestFilter addSameSiteCookieFilter() {
//        return new OncePerRequestFilter() {
//            @Override
//            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {
//                filterChain.doFilter(request, response);
//                addSameSiteCookieAttribute(response, "CART"); // Replace with your cookie name
//            }
//        };
//    }
//
//    private void addSameSiteCookieAttribute(HttpServletResponse response, String cookieName) {
//        if (response != null && response.containsHeader("Set-Cookie")) {
//            for (String header : response.getHeaders("Set-Cookie")) {
//                if (header.contains(cookieName)) {
//                    response.setHeader("Set-Cookie", String.format("%s; %s", header, "SameSite=None; Secure"));
//                }
//            }
//        }
//    }
//}
//
