package com.accountbook.config;

import com.accountbook.domain.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173");


        Test test = Test.builder()
                        .id(1L)
                        .address("seoul")
                        .name("jh2")
                        .build();
    }
}
