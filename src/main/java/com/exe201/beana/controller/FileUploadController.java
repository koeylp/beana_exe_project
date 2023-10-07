package com.exe201.beana.controller;

import com.exe201.beana.service.FileUpload;
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

    private final FileUpload fileUpload;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile multipartFile,
                                             Model model) throws IOException {
        String imageURL = fileUpload.uploadFile(multipartFile);
        model.addAttribute("imageURL", imageURL);
        return ResponseEntity.ok("Uploaded photo: " + imageURL);
    }
}
