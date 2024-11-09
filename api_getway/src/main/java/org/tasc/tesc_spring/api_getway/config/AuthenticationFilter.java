package org.tasc.tesc_spring.api_getway.config;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.stereotype.Component;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;

@Component

@RequiredArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final  TokenValidator tokenValidator;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            Claims claims = tokenValidator.isValidToken(token);
            if(chain == null){
                throw new EntityNotFound("Unauthorization",401);
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {

    }
}