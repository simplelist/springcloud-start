package com.kun.message.controller;

import com.kun.message.domain.User;
import com.kun.message.scheduler.UserScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    User user;

    @Autowired
    public MessageController(User user) {
        this.user = user;
    }

    @GetMapping("/user")
    public User getUser(){
        return this.user;
    }
    @Autowired
    UserScheduler userScheduler;
    @Value("${name}")
    String configFiled;
    @GetMapping("/hello")
    public String invokeUser(){
        return userScheduler.hello();
    }
    @GetMapping("/config")
    public String config(){
        return configFiled;
    }
}
