package org.tasc.tasc_spring.api_common.kafka;

import lombok.RequiredArgsConstructor;


import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.model.EmailDto;

import static org.tasc.tasc_spring.api_common.kafka.KafkaConfig.key_email;


@Service
@RequiredArgsConstructor
public class KafkaService {
    public final KafkaTemplate<String,Object> kafkaTemplate;

    public void send_ticket(EmailDto email) {
        kafkaTemplate.send(key_email, email);
    }


}
