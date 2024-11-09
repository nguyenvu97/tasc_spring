package org.tasc.tasc_spring.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages ={
        "org.tasc.tasc_spring.api_common",
        "org.tasc.tasc_spring.user_service"
})
@EnableFeignClients(
        basePackages ="org.tasc.tasc_spring.api_common"
)
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
