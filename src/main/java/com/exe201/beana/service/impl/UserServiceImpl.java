package com.exe201.beana.service.impl;

import com.exe201.beana.dto.UserDto;
import com.exe201.beana.mapper.UserMapper;
import com.exe201.beana.repository.UserRepository;
import com.exe201.beana.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::toUserDto).collect(Collectors.toList());
    }
}
