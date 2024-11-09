package org.tasc.tasc_spring.api_common.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseData {
    public String message;
    public int status_code;
    public Object data;
}
