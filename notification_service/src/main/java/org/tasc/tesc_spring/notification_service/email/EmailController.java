package org.tasc.tesc_spring.notification_service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.kafka.KafkaService;


@RestController
@RequestMapping("api/v1/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
//    private final KafkaService kafkaService;

//    @PostMapping
//    public void sendEmail(@RequestBody EmailDto emailDto) {
//        kafkaService.send_ticket(emailDto.getEmail());
//    }



}
