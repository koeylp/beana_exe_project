package com.exe201.beana.controller;

import com.exe201.beana.service.FileUploadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile multipartFile, @RequestParam int type,
                                             HttpServletRequest request, HttpServletResponse response) throws IOException {
        String imageURL = fileUploadService.uploadFile(multipartFile, type, request, response);
        return ResponseEntity.ok("Uploaded photo: " + imageURL);
    }

}
