package com.exe201.beana.service;

import com.exe201.beana.dto.AuthenticationRequestDto;
import com.exe201.beana.dto.AuthenticationResponseDto;
import com.exe201.beana.dto.RegisterRequestDto;
import com.exe201.beana.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    UserDto register(RegisterRequestDto request);

    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest);
}
