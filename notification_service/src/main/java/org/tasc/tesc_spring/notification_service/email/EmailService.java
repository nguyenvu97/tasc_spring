package org.tasc.tesc_spring.notification_service.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.kafka.KafkaService;

import java.time.Duration;


@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService implements EmailSender {
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send_Email(EmailDto emailDto) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"utf-8");
            mimeMessageHelper.setTo(emailDto.getTo());
            mimeMessageHelper.setText(emailDto.getEmail(),true);
            mimeMessageHelper.setFrom("nguyenkhacvu1997@gmail.com");
            mimeMessageHelper.setSubject("thanh cong");
            mimeMessageHelper.setText(emailDto.getBody(),true);
            mailSender.send(mimeMessage);
        }catch (Exception e){
            throw new IllegalStateException("failed to send email" );
        }
    }




}
