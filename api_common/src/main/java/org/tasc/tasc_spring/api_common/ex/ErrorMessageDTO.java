package org.tasc.tasc_spring.api_common.ex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ErrorMessageDTO   {
    private String message;
    private Instant date;
    private int statusCode;

}
