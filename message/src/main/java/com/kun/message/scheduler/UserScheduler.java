package com.kun.message.scheduler;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("user")
public interface UserScheduler {
    @GetMapping("/hello")
    String hello();
}
