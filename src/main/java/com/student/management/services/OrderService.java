package com.student.management.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.management.Repository.OrderItemRepository;
import com.student.management.Repository.OrderRepository;
import com.student.management.Repository.ProductRepository;
import com.student.management.enitity.OrderItem;
import com.student.management.enitity.Orders;
import com.student.management.enitity.Products;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemRepository itemRepo;

    @Autowired
    private ProductRepository productRepo;

    public List<Map<String, Object>> getUserOrders(Long userId) {

        List<Orders> orders = orderRepo.findByUserId(userId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Orders order : orders) {

            List<OrderItem> items = itemRepo.findByOrderId(order.getId());

            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("orderId", order.getId());
            orderMap.put("totalPrice", order.getTotalPrice());
            orderMap.put("paymentMode", order.getPaymentMode());
            orderMap.put("status", order.getDeliveryStatus());

            List<Map<String, Object>> itemList = new ArrayList<>();

            for (OrderItem item : items) {
                Products p = productRepo.findById(item.getProductId()).orElse(null);

                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("productName", p.getName());
                itemMap.put("price", item.getPrice());
                itemMap.put("quantity", item.getQuantity());

                itemList.add(itemMap);
            }

            orderMap.put("items", itemList);
            response.add(orderMap);
        }

        return response;
    }
}
