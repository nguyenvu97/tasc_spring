//package org.tasc.tasc_spring.api_common.config;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Component
//public class FeignRequest implements RequestInterceptor {
//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//
//        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//                .getRequest()
//                .getHeader("Authorization");
//        String role = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//                .getRequest()
//                .getHeader("role");
//
//
//        if (token != null && role != null) {
//            requestTemplate.header("Authorization", token);  // Thêm token vào header
//            requestTemplate.header("role", role);            // Thêm role vào header
//        }
//    }
//}
