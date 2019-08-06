package com.kun.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    Environment environment;

    @GetMapping("/hello")
    public String add(){
        return "<h1>响应来自端口:"+environment.getProperty("server.port")+"标签是:"+environment.getProperty("eureka.instance.metadata-map.routingTag")+"</h1>";
    }
}
