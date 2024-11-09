package org.tasc.tasc_spring.api_common.ex;




public class InvalidCallException extends Exception {
    public InvalidCallException(String message,int status_code) {
        super(message,status_code);
    }
}

