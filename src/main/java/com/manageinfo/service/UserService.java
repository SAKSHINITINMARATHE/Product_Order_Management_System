package com.manageinfo.service;

import com.manageinfo.model.User;
import com.manageinfo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Optional<User> adminOpt = userRepository.findByUsername("admin");
        if (adminOpt.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(User.Role.ADMIN);
            admin.setApproved(true);
            userRepository.save(admin);
        } else {
            User admin = adminOpt.get();
            if (!admin.isApproved()) {
                admin.setApproved(true);
                userRepository.save(admin);
            }
        }
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setApproved(false);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isApproved()) {
            throw new RuntimeException("Account pending approval");
        }

        return user;
    }

    public List<User> getPendingUsers() {
        List<User> allUsers = userRepository.findAll();
        java.util.ArrayList<User> pending = new java.util.ArrayList<>();
        for (User u : allUsers) {
            if (!u.isApproved()) {
                pending.add(u);
            }
        }
        return pending;
    }

    public void approveUser(Long id) {
        User user = getUserById(id);
        user.setApproved(true);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
