package org.tasc.tasc_spring.redis_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages ={
        "org.tasc.tasc_spring.api_common",
        "org.tasc.tasc_spring.redis_service"
})
@EnableFeignClients(
        basePackages ="org.tasc.tasc_spring.api_common"
)
@EnableScheduling
public class RedisServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisServiceApplication.class, args);
    }

}
