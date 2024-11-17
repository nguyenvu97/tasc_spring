package org.tasc.tesc_spring.redis_service.service;


import org.tasc.tasc_spring.api_common.model.ResponseData;

public interface RedisService {
    ResponseData saveInRedis(String key, Object value,long timeout);
    ResponseData getInRedis(String key);
    void deleteInRedis(String key);
}
