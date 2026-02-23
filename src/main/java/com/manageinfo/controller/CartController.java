package com.manageinfo.controller;

import com.manageinfo.dto.CartDTO;
import com.manageinfo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public CartDTO getCart(@RequestParam Long userId) {
        return cartService.getCartDetails(userId);
    }

    @PostMapping("/add")
    public void addToCart(@RequestParam Long userId, @RequestParam int productId, @RequestParam int quantity) {
        cartService.addItemToCart(userId, productId, quantity);
    }

    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam Long userId, @RequestParam int productId) {
        cartService.removeItemFromCart(userId, productId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
    }
}
