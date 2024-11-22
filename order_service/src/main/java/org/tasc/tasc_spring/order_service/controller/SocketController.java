package org.tasc.tasc_spring.order_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.tasc.tasc_spring.order_service.service.CartService;

@RestController
@RequiredArgsConstructor
public class SocketController {
    public final CartService cartService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public int countCart(String token){
        return   cartService.countCart(token);

    }


}
