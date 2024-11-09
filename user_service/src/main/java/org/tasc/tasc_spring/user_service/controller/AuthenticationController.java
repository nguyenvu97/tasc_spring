package org.tasc.tasc_spring.user_service.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.user_service.auth.AuthenticationRequest;
import org.tasc.tasc_spring.user_service.auth.RegisterRequest;
import org.tasc.tasc_spring.user_service.service.AuthenticationService;


import java.io.IOException;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request)  {
        try{
            return ResponseEntity.ok(service.register(request));
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();

    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        try {
            return ResponseEntity.ok().body(service.login(request, response));
        } catch (EntityNotFound e) {
           return ResponseEntity.ok().body(e.getMessage());
        }


    }
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }
    @GetMapping("/logout")
    public void logoutUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        service.logout(request, response, authentication);
    }
    @GetMapping("/decode")
    public ResponseEntity<?> decode(@RequestHeader(value = "Authorization") String token) {
        try {
            return ResponseEntity.ok().body( service.decode_token(token));
        } catch (EntityNotFound e) {
            return ResponseEntity.ok().body(e.getMessage());
        }

    }



}
