package org.tasc.tasc_spring.order_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.ex.InvalidCallException;
import org.tasc.tasc_spring.api_common.model.response.ProductDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.order_service.service.CartService;

import java.util.List;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addCart(@RequestBody ProductDto cart, @RequestHeader(value = "Authorization") String token) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(cartService.addCart(cart, token));
        } catch (EntityNotFound e) {
            throw e;
        }catch (InvalidCallException e) {
            throw e;
        }
    }


    @GetMapping("/get")
    public ResponseEntity<?> getCart(@RequestHeader(value = "Authorization") String token) {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart( token));
        } catch (EntityNotFound e) {
            throw e;
        }catch (InvalidCallException e) {
            throw e;
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCart(@RequestParam(value ="product_id") List<String> productIds,
                                        @RequestHeader(value = "Authorization") String token) {
        try {
            ResponseData response = cartService.deleteCart(productIds, token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            throw e;
        }catch (InvalidCallException e) {
            throw e;
        }
    }
}
