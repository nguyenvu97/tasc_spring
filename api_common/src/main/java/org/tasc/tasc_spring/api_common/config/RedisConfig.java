package org.tasc.tasc_spring.api_common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class RedisConfig {

    public static String Cart_Key = "cart";
    public static String Token_Key = "jwt";
    public static String Otp_Key = "otp";

}
