package com.exe201.beana.controller;


import com.exe201.beana.dto.ChangeAvatarRequestDto;
import com.exe201.beana.dto.UserDto;
import com.exe201.beana.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/change-avatar")
    public ResponseEntity<String> changeAvatarUser(@RequestBody @Valid ChangeAvatarRequestDto avatar) {
        return ResponseEntity.ok(userService.changeAvatarUser(avatar));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }
}
