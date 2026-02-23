package com.manageinfo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    
    private Boolean approved = false;

    public enum Role {
        USER, ADMIN
    }

    public User() {
    }

    public User(String username, String password, Role role, boolean approved) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isApproved() {
        return approved != null && approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
