package com.ecommerce.Order.controller;

import com.ecommerce.Order.dto.CartItemRequest;
import com.ecommerce.Order.models.CartItem;
import com.ecommerce.Order.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId,
                                            @RequestBody CartItemRequest request){
        if(!cartService.addToCart(userId, request)){
            return ResponseEntity.badRequest().body("Product Out of stock or user not found or Product not found");
        }
        //cartService.addToCart(userId,request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId,@PathVariable String productId){
        boolean result=cartService.deleteItemFromCart(userId,productId);
        return result? ResponseEntity.noContent().build(): ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(cartService.getCart(userId));

    }
}
