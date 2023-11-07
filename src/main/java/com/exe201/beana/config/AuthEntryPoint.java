package com.exe201.beana.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = "Unauthorized: ";
        if (exception != null) {
            errorMessage += exception.getMessage();
        } else {
            errorMessage += "Authentication failed due to an unknown reason.";
        }

        response.getOutputStream().println("{ \"error\": \"" + errorMessage + "\" }");
    }
}
