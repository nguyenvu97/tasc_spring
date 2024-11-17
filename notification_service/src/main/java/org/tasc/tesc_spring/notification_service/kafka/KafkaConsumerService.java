package org.tasc.tesc_spring.notification_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.model.EmailDto;
import org.tasc.tesc_spring.notification_service.email.EmailSender;

import java.time.Duration;

import static org.tasc.tasc_spring.api_common.kafka.KafkaConfig.consumer;
import static org.tasc.tasc_spring.api_common.kafka.KafkaConfig.key_email;


@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaConsumerService {
    private final ObjectMapper objectMapper;
    private final EmailSender emailSender;
@KafkaListener(topics = key_email, groupId = consumer)
public void test_data_kafka(ConsumerRecord<String, String> record) {
    try {
        EmailDto emailDto = objectMapper.readValue(record.value(), EmailDto.class);
        emailSender.send_Email(emailDto);
    } catch (Exception e) {
        log.error("Error consuming message", e);
    }
}



}
