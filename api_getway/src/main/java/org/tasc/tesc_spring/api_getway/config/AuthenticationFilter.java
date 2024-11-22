package org.tasc.tesc_spring.api_getway.config;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.stereotype.Component;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.ex.Unauthorized;


@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final   TokenValidator tokenValidator;





    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (token == null || token.isEmpty()) {
                throw new Unauthorized(401, "Authorization token is missing");
            }
            Claims claims = tokenValidator.isValidToken(token);
            if(claims == null || claims.getSubject() == null) {
                throw new EntityNotFound("Unauthorization",401);
            }
            String userRole = claims.get("role", String.class);
            exchange = exchange.mutate()
                    .request(r -> r.header("role",userRole))
                    .build();
            return chain.filter(exchange);
        };
    }

    public static class Config {

    }
}