package org.tasc.tasc_spring.api_common.ex;





public class EntityNotFound extends Exception {
    public EntityNotFound(String message, int status_code) {
        super(message,status_code);
    }
}

