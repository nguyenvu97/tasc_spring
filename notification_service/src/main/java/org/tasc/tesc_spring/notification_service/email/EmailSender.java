package org.tasc.tesc_spring.notification_service.email;


import org.tasc.tasc_spring.api_common.model.response.EmailDto;

public interface EmailSender {
    void send_Email(EmailDto emailDto);
}
