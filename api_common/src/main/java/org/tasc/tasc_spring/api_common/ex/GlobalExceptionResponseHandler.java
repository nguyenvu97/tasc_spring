package org.tasc.tasc_spring.api_common.ex;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

import static org.tasc.tasc_spring.api_common.ex.ExceptionMessagesEnum.REQUEST_NOT_GOOD;


@RestControllerAdvice
public class GlobalExceptionResponseHandler {
    @ExceptionHandler(value = {InvalidCallException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageDTO handleInvalidCallException(InvalidCallException e, WebRequest request) {
        return ErrorMessageDTO.builder()
                .message(e.getMessage())
                .date(Instant.now())
                .statusCode(e.getStatus_code())
                .build();

    }
    @ExceptionHandler(value = {EntityNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageDTO handleNotFound( EntityNotFound e, WebRequest request) {
        return ErrorMessageDTO.builder()
                .message(e.getMessage())
                .date(Instant.now())
                .statusCode(e.getStatus_code())
                .build();

    }




}
