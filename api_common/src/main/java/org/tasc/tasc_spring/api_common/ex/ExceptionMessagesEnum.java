package org.tasc.tasc_spring.api_common.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessagesEnum {

    CLIENT_NOT_FOUND("Client not found"),

    EMAIL_BAD_FORMAT("Email has bad format"),

    CLIENT_ALREADY_EXISTS("Client already exists"),

    ASSETS_ALREADY_EXISTS("Assets already exists"),
    REQUEST_NOT_GOOD("Request not good"),
    LOGIN_FAILS("Login_fails");

    private final String description;

}
