package com.exe201.beana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.DefaultCookieSerializer;


@SpringBootApplication
public class BeanaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeanaApplication.class, args);
    }

    @Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setUseSecureCookie(true); // This will ensure the secure flag is set for cookies
        return serializer;
    }

}
