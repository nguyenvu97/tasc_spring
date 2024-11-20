package org.tasc.tasc_spring.redis_service.service;

import org.tasc.tasc_spring.api_common.model.Otp;
import org.tasc.tasc_spring.api_common.model.request.Cart;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

import java.util.List;

public interface OtpService {
    ResponseData saveInRedis(String key, String user_id, Otp value, long timeout);
    ResponseData getInRedis(String key,String user_id);
    ResponseData deleteProduct( String key, String user_id);
}
