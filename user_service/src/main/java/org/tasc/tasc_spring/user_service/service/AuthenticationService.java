package org.tasc.tasc_spring.user_service.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.model.request.AuthenticationRequest;
import org.tasc.tasc_spring.user_service.auth.RegisterRequest;


public interface AuthenticationService {
    ResponseData register (RegisterRequest request) ;
    ResponseData login(AuthenticationRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) ;
    ResponseData decode_token(String token);


}
