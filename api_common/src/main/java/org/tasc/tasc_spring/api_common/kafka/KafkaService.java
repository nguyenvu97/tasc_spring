package org.tasc.tasc_spring.api_common.kafka;

import lombok.RequiredArgsConstructor;


import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaService {
    public final KafkaTemplate<String,Object> kafkaTemplate;
    public final static String key_email = "sendEmail";

    public void send_ticket(String email) {
        kafkaTemplate.send(key_email, email);
    }


}
