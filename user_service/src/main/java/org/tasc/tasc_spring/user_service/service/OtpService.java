package org.tasc.tasc_spring.user_service.service;

import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.user_service.dto.request.RequestOtp;

public interface OtpService {
     ResponseData authenticateUser(String email);

     ResponseData verifyOTP(RequestOtp requestOtp);
}
