package org.tasc.tesc_spring.notification_service.email;


public interface EmailSender {
    void send_Email(EmailDto emailDto);
}
