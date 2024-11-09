package org.tasc.tasc_spring.user_service.model;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Otp {
    private String createAt;
    private int number;


}
