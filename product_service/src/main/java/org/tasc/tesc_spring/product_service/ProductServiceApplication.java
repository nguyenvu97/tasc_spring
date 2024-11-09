package org.tasc.tesc_spring.product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages ={
        "org.tasc.tasc_spring.api_common",
        "org.tasc.tesc_spring.product_service"
})
@EnableFeignClients(
        basePackages ="org.tasc.tasc_spring.api_common"
)
@EnableDiscoveryClient
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
