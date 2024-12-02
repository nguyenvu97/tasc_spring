package org.tasc.tasc_spring.api_common.javaUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ServerWebExchange;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.user_api.UserApi;


import java.util.Map;

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

    public static CustomerDto get_customer(String token, UserApi userApi , ObjectMapper objectMapper) {
        ResponseData responseData =  userApi.decode(token);
        CustomerDto customerDto  = decodeToken(responseData,objectMapper);
        System.out.println(customerDto.getRole());
        if (customerDto.getId() == null){
            throw new EntityNotFound("token not found",404);
        }
        return  customerDto;
    }
    public static String performAction(HttpServletRequest request) {
        String userRole = request.getHeader("role");

        if ("ADMIN".equals(userRole)) {
            return "ADMIN";
        } else {
            return "USER";
        }
    }

}
