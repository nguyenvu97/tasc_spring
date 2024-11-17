package org.tasc.tesc_spring.notification_service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.kafka.KafkaService;
import org.tasc.tasc_spring.api_common.model.EmailDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/email")
public class EmailController {
    private final KafkaService kafkaService;
    @PostMapping
    public void send_email(@RequestBody EmailDto emailDto){
        kafkaService.send_ticket(emailDto);

    }
}
