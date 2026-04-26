package com.student.management.dto;

public class RegisterRequest {

    private String name;      // ✅ added
    private String email;  // email will come here
    private String password;

    // ===== GETTERS =====
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // ===== SETTERS =====
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
