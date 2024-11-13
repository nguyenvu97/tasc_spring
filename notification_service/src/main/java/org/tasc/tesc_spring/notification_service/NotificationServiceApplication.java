package org.tasc.tesc_spring.notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages ={
		"org.tasc.tasc_spring.api_common",
		"org.tasc.tesc_spring.notification_service"
})
@EnableFeignClients(
		basePackages ="org.tasc.tasc_spring.api_common"
)
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);

	}

}
