package org.tasc.tesc_spring.api_getway.router;

import lombok.RequiredArgsConstructor;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.tasc.tesc_spring.api_getway.config.AuthenticationFilter;
import org.tasc.tesc_spring.api_getway.config.TokenValidator;
import reactor.core.publisher.Mono;


@Configuration
@RequiredArgsConstructor
public class RouterConfig {
    private final AuthenticationFilter authenticationFilter;
    private final TokenValidator tokenValidator;


    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String MAX_AGE = "3600";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    HttpHeaders headers = response.getHeaders();
                    headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
                    headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                    headers.add("Access-Control-Max-Age", MAX_AGE);
                    headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

    @Bean
    public RouteLocator userRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user_service_route_public", r -> r.path("/api/v1/user/login", "/api/v1/user/register", "/api/v1/otp/**","/api/v1/user/logout")
                        .uri("http://localhost:8083"))
                .route("user_service_route_private", r -> r.path("/api/v1/user/decode")
                        .filters(f ->
                                f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http://localhost:8083"))
                .route("product_service_route_public",
                        r -> r.path("/api/v1/product/get_all", "/api/v1/product/get")
                                .uri("http://localhost:1991"))
                .route("product_service_route_private",
                        r -> r.path("/api/v1/product/add","/api/v1/product/update","/api/v1/product/check")
                                .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                                .uri("http://localhost:1991"))
                .route("order_service_route_public", r -> r
                        .path("/gs-guide-websocket")
                        .uri("ws://localhost:1990"))
                .route("order_service_route_private",
                        r -> r.path("/api/v1/cart/get", "/api/v1/cart/add","/api/v1/cart/delete",
                                        "/api/v1/order/**",
                                        "/api/v1/order_details/**")
                                .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                                .uri("http://localhost:2000"))
                .route("pay_service_route_private",
                        r -> r.path("/api/v1/vnpay/get")
                                .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                                .uri("http://localhost:2001"))
                .build();
    }





}
