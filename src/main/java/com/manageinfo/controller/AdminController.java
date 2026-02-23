package com.manageinfo.controller;

import com.manageinfo.model.User;
import com.manageinfo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/pending")
    public List<User> getPendingUsers() {
        return userService.getPendingUsers();
    }

    @PostMapping("/users/{id}/approve")
    public void approveUser(@PathVariable Long id) {
        userService.approveUser(id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
