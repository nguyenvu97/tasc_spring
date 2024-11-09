package org.tasc.tasc_spring.user_service.dto.request;

import lombok.Data;

@Data
public class RequestOtp {
   private int otp;
    private String newPassword;
    private String email;
}
