package com.example.springcloud_demo.feign.auto;

import com.example.springcloud_demo.feign.config.JsonResponseFeignConf;
import com.example.springcloud_demo.feign.dto.ProcessRequestPost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "autoIntegrationProcess", url = "${feign.externalService.service1.url}", configuration = JsonResponseFeignConf.class)
public interface AutoIntegrationProcess {

    @PostMapping
    void runProcess(@RequestBody ProcessRequestPost processRequestPost);
}