package org.tasc.tasc_spring.user_service.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tasc.tasc_spring.user_service.model.role.Role;

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
    @NotNull(message = "Thiếu Role")
    private Role role;
}
