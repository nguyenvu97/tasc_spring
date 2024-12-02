package org.tasc.tesc_spring.api_getway.config;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final   TokenValidator tokenValidator;





    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            Claims claims = null;
            if (token == null || token.isEmpty()) {
                return sendUnauthorizedResponse(exchange,"Authorization token is missing");
            }
                 claims = tokenValidator.isValidToken(token);
                if(claims == null || claims.getSubject() == null) {
                    return sendUnauthorizedResponse(exchange,"Invalid token");
                }
            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
            claims.forEach((key, value) -> {
                if (value != null) {
                    requestBuilder.header(key, value.toString());
                }
            });
            ServerHttpRequest modifiedRequest = requestBuilder.build();
            System.out.println(modifiedRequest);

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }
    private Mono<Void> sendUnauthorizedResponse(ServerWebExchange exchange, String message ) {
        // Tạo phản hồi với mã 401 (Unauthorized)
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Tạo thông điệp lỗi dạng JSON
        String responseMessage = "{\"message\": \"" + message + "\", \"status\": 401}";

        // Gửi phản hồi với nội dung JSON
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseMessage.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    public static class Config {

    }
}