package org.tasc.tasc_spring.api_common.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {
    @JsonProperty("email")
    public String email;
    @JsonProperty("id")
    public String id;
    @JsonProperty("exp")
    public long exp;
    @JsonProperty("iat")
    public long iat;
    @JsonProperty("role")
    private String role;
    @JsonProperty("name")
    private String fullName;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("address")
    private String address;


}
