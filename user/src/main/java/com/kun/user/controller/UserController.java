package com.kun.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Value("${server.port}")
    private String port;

    @GetMapping("/hello")
    public String add(){
        return "this come from service port:"+port;
    }
}
