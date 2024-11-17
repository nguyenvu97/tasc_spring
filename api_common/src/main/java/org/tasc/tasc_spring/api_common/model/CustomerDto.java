package org.tasc.tasc_spring.api_common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
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

}
