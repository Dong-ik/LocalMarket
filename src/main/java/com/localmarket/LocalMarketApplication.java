package com.localmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class LocalMarketApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LocalMarketApplication.class, args);
    }

    @Override   
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LocalMarketApplication.class);
    }
}