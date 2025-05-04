package com.yichen.config;

import io.github.linpeilie.ConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    
    @Bean
    public ConverterFactory converterFactory() {
        return new ConverterFactory();
    }
} 