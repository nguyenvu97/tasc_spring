package org.tasc.tasc_spring.user_service.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.ResponseData;
import org.tasc.tasc_spring.user_service.auth.AuthenticationRequest;
import org.tasc.tasc_spring.user_service.auth.RegisterRequest;
import org.tasc.tasc_spring.user_service.dto.CustomerDto;


import java.io.IOException;


public interface AuthenticationService {
    ResponseData register (RegisterRequest request) ;
    ResponseData login(AuthenticationRequest request, HttpServletResponse response) throws EntityNotFound;
    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) ;
    ResponseData decode_token(String token) throws EntityNotFound;

}
