package com.company.indieboxd.config;

import com.company.indieboxd.properties.StorageProperties;
import com.company.indieboxd.service.StorageService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfig {

    @Bean
    public StorageService storageService(StorageProperties properties) {
        StorageService storageService = new StorageService(properties);
        storageService.init();
        return storageService;
    }
}