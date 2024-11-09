package org.tasc.tasc_spring.user_service.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "Thiếu password")
    private String password;
    @NotEmpty(message = "Thiếu email")
    private String email;
    @NotEmpty(message = "Thiếu phone")
    private String phone;
    @NotEmpty(message = "Thiếu address")
    private String address;
    @NotEmpty(message = "Thiếu name")
    private String fullName;
}
