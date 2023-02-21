package com.example.springcloud_demo.feign;

import feign.FeignException;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeignClientFailedRequest {
    private String feignClientName;
    private FeignException feignException;
    private String requestBody;
    private String method;
    private int httpStatus;
}