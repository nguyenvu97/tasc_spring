package org.tasc.tasc_spring.user_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CustomerDto {
    public String email;
    public long exp;
    public long iat;
}
