package com.exe201.beana.service.impl;

import com.exe201.beana.dto.ChangeAvatarRequestDto;
import com.exe201.beana.dto.UserDto;
import com.exe201.beana.entity.User;
import com.exe201.beana.exception.ResourceNotFoundException;
import com.exe201.beana.mapper.UserMapper;
import com.exe201.beana.repository.UserRepository;
import com.exe201.beana.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::toUserDto).collect(Collectors.toList());
    }

    @Override
    public String changeAvatarUser(ChangeAvatarRequestDto avatar) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> foundUser = userRepository.findUserByStatusAndUsername((byte) 1, username);
        if (foundUser.isEmpty())
            throw new ResourceNotFoundException("User Not found with username: " + username);
        foundUser.get().setAvatar(avatar.getUrl());
        userRepository.save(foundUser.get());
        return "Avatar was changed successfully!";
    }
}
