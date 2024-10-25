package com.burntbean.burntbean.config.properties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebCorsConfig implements WebMvcConfigurer {
    private final CorsPropertiesConfig corsConfig;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsConfig.getAllowedOrigins())
                //.allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);  //쿠키와 HTTP 인증 헤더를 포함하는 요청 승인
        log.info("cors 경로: {}"+corsConfig.getAllowedOrigins().toString());
        System.out.println("cors 경로: {}"+corsConfig.getAllowedOrigins().toString());
        log.info("cors 경로: {}", Arrays.toString(corsConfig.getAllowedOrigins()));


    }
}
