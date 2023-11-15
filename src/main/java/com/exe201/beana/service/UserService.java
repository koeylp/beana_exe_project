package com.exe201.beana.service;

import com.exe201.beana.dto.ChangeAvatarRequestDto;
import com.exe201.beana.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    String changeAvatarUser(ChangeAvatarRequestDto avatar);
}
