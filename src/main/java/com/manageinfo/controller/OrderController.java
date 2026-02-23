package com.manageinfo.controller;

import com.manageinfo.model.Order;
import com.manageinfo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@RequestParam Long userId) {
        return orderService.placeOrder(userId);
    }

    @GetMapping
    public List<Order> getUserOrders(@RequestParam Long userId) {
        return orderService.getUserOrders(userId);
    }
}
