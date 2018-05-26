package com.kun.message.controller;

import com.kun.message.scheduler.UserScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @Autowired
    UserScheduler userScheduler;
    @GetMapping("/hello")
    public String invokeUser(){
        return userScheduler.hello();
    }
}
