package com.student.management.enitity;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Many addresses belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String phone;
    private String landmark;
    private String area;
    private String city;
    private String state;
    private String pincode;

    // ===== CONSTRUCTORS =====

    public Address() {}

    public Address(User user, String phone, String area, String city) {
        this.user = user;
        this.phone = phone;
        this.area = area;
        this.city = city;
    }

    // ===== GETTERS =====

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getPhone() {
        return phone;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    // ===== SETTERS =====

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
