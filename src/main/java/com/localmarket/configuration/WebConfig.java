package com.localmarket.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 프로젝트 루트 경로 가져오기
        String projectPath = System.getProperty("user.dir");
        String uploadPath = "file:///" + projectPath.replace("\\", "/") + "/src/main/static/images/";

        // src/main/static 경로를 정적 리소스로 등록 (절대 경로 사용)
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath)
                .setCachePeriod(3600);

        // 기본 정적 리소스 경로도 유지
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/")
                .setCachePeriod(3600);
    }
}
