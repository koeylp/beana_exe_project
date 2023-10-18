package com.exe201.beana.service.impl;

import com.cloudinary.Cloudinary;
import com.exe201.beana.dto.ProductImageListDto;
import com.exe201.beana.entity.ProductImage;
import com.exe201.beana.repository.ProductImageRepository;
import com.exe201.beana.service.FileUploadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final Cloudinary cloudinary;
    private final ProductImageRepository productImageRepository;
    private static final String IMAGE_COOKIE_NAME = "IMAGE_COOKIE";

    @Override
    public String uploadFile(MultipartFile multipartFile, int type, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String urlImage = cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();

        ProductImage productImage = productImageRepository.save(new ProductImage(null, urlImage, (byte) 1, null, type));
        ProductImageListDto productImageList = getImageFromCookie(request);
        productImageList.addItem(productImage);
        saveImageToCookie(productImageList, response);
        return urlImage;
    }

    private void saveImageToCookie(ProductImageListDto imageUrls, HttpServletResponse response) {
        Cookie imageCookie = new Cookie(IMAGE_COOKIE_NAME, serializeImageList(imageUrls));
        imageCookie.setMaxAge(10 * 60);
        response.addCookie(imageCookie);
    }

    private String serializeImageList(ProductImageListDto imageUrls) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return URLEncoder.encode(objectMapper.writeValueAsString(imageUrls), StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ProductImageListDto getImageFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(IMAGE_COOKIE_NAME)) {
                    return deserializeImages(cookie.getValue());
                }
            }
        }
        return new ProductImageListDto();
    }

    private ProductImageListDto deserializeImages(String imageJson) {
        try {
            String decodedImageData = URLDecoder.decode(imageJson, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decodedImageData, ProductImageListDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
