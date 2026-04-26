package com.student.management.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.student.management.enitity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserId(Long userId); 
}
