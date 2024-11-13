package org.tasc.tesc_spring.notification_service.email;

import lombok.Data;

@Data
public class EmailDto {
    public String to;
    public String email;
    public String body;
}
