package com.example.springcloud_demo.feign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "feign")
public class FeignProperties {

    private Map<String, ExternalService> externalService;

    @Data
    public static class ExternalService {
        private String url;
        private FeignRetryer retryer;
    }

    @Data
    public static class FeignRetryer {
        private Integer backoff;
        private Integer maxAttempts;
    }
}