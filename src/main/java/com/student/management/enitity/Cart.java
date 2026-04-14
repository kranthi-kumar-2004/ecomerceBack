package com.student.management.enitity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId; 

    @Column(name = "product_id")
    private Long productId;

    private Integer quantity = 1;

    // 🔹 Getters & Setters

    public Long getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId.intValue(); 
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}