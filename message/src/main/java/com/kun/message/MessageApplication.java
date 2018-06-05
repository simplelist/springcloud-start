package com.kun.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

    ContextRefresher contextRefresher;

    @Autowired
    public MessageApplication(ContextRefresher contextRefresher) {
        this.contextRefresher = contextRefresher;
    }

    /**
     * 自动刷新
     * @return
     */
//    @Scheduled(fixedRate = 10000L)
    public Set<String> update(){
        return contextRefresher.refresh();
    }

}
