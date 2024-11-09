package org.tasc.tasc_spring.user_service.service;

import org.tasc.tasc_spring.api_common.model.ResponseData;
import org.tasc.tasc_spring.user_service.dto.request.RequestOtp;
import org.tasc.tasc_spring.user_service.model.User;

public interface OtpService {
     ResponseData authenticateUser(String email);

     ResponseData verifyOTP(RequestOtp requestOtp);
}
