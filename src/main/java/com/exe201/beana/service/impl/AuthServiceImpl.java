package com.exe201.beana.service.impl;

import com.exe201.beana.dto.AuthenticationRequestDto;
import com.exe201.beana.dto.AuthenticationResponseDto;
import com.exe201.beana.dto.RegisterRequestDto;
import com.exe201.beana.dto.UserDto;
import com.exe201.beana.entity.Role;
import com.exe201.beana.entity.User;
import com.exe201.beana.exception.BadCredentialException;
import com.exe201.beana.exception.ResourceAlreadyExistsException;
import com.exe201.beana.exception.ResourceNotFoundException;
import com.exe201.beana.mapper.UserMapper;
import com.exe201.beana.repository.UserRepository;
import com.exe201.beana.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public UserDto register(RegisterRequestDto request) {
        Optional<User> foundUsername = userRepository.findUserByStatusAndUsername((byte) 1, request.getUsername());
        if (foundUsername.isPresent())
            throw new ResourceAlreadyExistsException("Username already exists with user's id: " + foundUsername.get().getId());

        Optional<User> foundEmail = userRepository.findUserByStatusAndEmail((byte) 1, request.getEmail());
        if (foundEmail.isPresent())
            throw new ResourceAlreadyExistsException("Email already exists with user's id: " + foundEmail.get().getId());

        Optional<User> foundPhone = userRepository.findUserByStatusAndPhone((byte) 1, request.getPhone());
        if (foundPhone.isPresent())
            throw new ResourceAlreadyExistsException("Phone already exists with user's id: " + foundPhone.get().getId());
        var user = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .email(request.getEmail())
                .gender(request.getGender())
                .phone(request.getPhone())
                .dob(request.getDob())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.isAdmin() ? Role.ROLE_MANAGER : Role.ROLE_CUSTOMER)
                .status((byte) 1)
                .build();
        return UserMapper.INSTANCE.toUserDto(userRepository.save(user));
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        try {
            Optional<User> foundUser = userRepository.findUserByStatusAndUsername((byte) 1, request.getUsername());
            if (foundUser.isEmpty())
                throw new ResourceNotFoundException("Wrong username");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            var jwtToken = jwtService.generateToken(foundUser.get());
            return AuthenticationResponseDto.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            throw new BadCredentialException("Wrong username or password");
        }
    }

}
