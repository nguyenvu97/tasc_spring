package org.tasc.tasc_spring.api_common.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Exception extends RuntimeException{

    private String message;

    private int status_code;

}
