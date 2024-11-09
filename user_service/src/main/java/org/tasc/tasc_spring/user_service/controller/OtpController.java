package org.tasc.tasc_spring.user_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.user_service.dto.request.RequestOtp;
import org.tasc.tasc_spring.user_service.service.OtpService;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;
    @PostMapping("/generate")
    public ResponseEntity<?>generateOtp(@RequestParam String email) {
        return  ResponseEntity.ok().body(otpService.authenticateUser(email));
    }
    @PostMapping("/verify")
    public ResponseEntity<?>verifyOTP (@RequestBody RequestOtp requestOtp) {
        return  ResponseEntity.ok().body(otpService.verifyOTP(requestOtp));
    }
}
