package com.example.springcloud_demo.feign.manual;

import com.example.springcloud_demo.feign.dto.ProcessRequestPost;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ManualIntegrationProcessOne {

    @PostMapping(consumes = "application/json")
    void runProcess(@RequestBody ProcessRequestPost processRequestPost);
}