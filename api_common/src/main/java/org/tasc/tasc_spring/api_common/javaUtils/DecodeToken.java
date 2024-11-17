package org.tasc.tasc_spring.api_common.javaUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.CustomerDto;
import org.tasc.tasc_spring.api_common.model.ResponseData;

import java.util.Map;

@RequiredArgsConstructor
public class DecodeToken {

    public static CustomerDto decodeToken(ResponseData responseData,ObjectMapper objectMapper) {
        if (responseData.status_code != 200 || responseData.data == null) {
            throw new EntityNotFound("token ex", 401);
        }
        CustomerDto customerDto = null;
        if (responseData.data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) responseData.data;
            customerDto = objectMapper.convertValue(dataMap, CustomerDto.class);
        }
        return customerDto;
    }


}
