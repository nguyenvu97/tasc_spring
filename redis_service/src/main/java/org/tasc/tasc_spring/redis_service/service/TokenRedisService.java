package org.tasc.tasc_spring.redis_service.service;

import org.tasc.tasc_spring.api_common.model.response.ResponseData;

public interface TokenRedisService {
    ResponseData saveInRedis(String key, String user_id, String value, long timeout);
    ResponseData getInRedis(String key, String user_id);
    ResponseData deleteInRedis(String key, String user_id);
}
