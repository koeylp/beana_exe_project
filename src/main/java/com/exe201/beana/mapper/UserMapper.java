package com.exe201.beana.mapper;

import com.exe201.beana.dto.UserDto;
import com.exe201.beana.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);
}
