package com.student.management.Repository;

import com.student.management.enitity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndProductId(Integer userId, Long productId);

    List<Cart> findByUserId(Integer userId);
}