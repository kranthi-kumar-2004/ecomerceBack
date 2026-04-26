package com.student.management.dto;

public class RegisterRequest {

    private String name;      // ✅ added
    private String username;  // email will come here
    private String password;

    // ===== GETTERS =====
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // ===== SETTERS =====
    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
