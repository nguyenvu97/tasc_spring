package org.tasc.tasc_spring.api_common.model.response;

import lombok.Data;

@Data
public class EmailDto {
    public String to;
    public String email;
    public Object body;
}
