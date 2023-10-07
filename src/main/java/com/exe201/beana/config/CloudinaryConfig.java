package com.exe201.beana.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        String CLOUD_NAME = "beana";
        config.put("cloud_name", CLOUD_NAME);
        String API_KEY = "138198251641188";
        config.put("api_key", API_KEY);
        String API_SECRET = "9q1jY-3tDMp_fD2a3pfkV__0G_A";
        config.put("api_secret", API_SECRET);

        return new Cloudinary(config);
    }
}
