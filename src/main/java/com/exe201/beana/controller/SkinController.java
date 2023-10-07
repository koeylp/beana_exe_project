package com.exe201.beana.controller;

import com.exe201.beana.dto.SkinDto;
import com.exe201.beana.dto.SkinRequestDto;
import com.exe201.beana.service.SkinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/skins")
public class SkinController {

    private final SkinService skinService;

    @PostMapping("")
    public ResponseEntity<SkinDto> addSkin(@RequestBody @Valid SkinRequestDto skinRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(skinService.addSkin(skinRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<SkinDto>> getAllSkins() {
        return ResponseEntity.status(HttpStatus.OK).body(skinService.getAllSkins());
    }

}
