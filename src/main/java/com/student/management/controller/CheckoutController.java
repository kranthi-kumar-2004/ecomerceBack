package com.student.management.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.management.enitity.Address;
import com.student.management.enitity.OrderItem;
import com.student.management.enitity.Orders;
import com.student.management.enitity.Products;
import com.student.management.enitity.User;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.student.management.Repository.AddressRepository;
import com.student.management.Repository.OrderItemRepository;
import com.student.management.Repository.OrderRepository;
import com.student.management.Repository.ProductRepository;
import com.student.management.Repository.UserRepository;
import com.student.management.dto.CartItem;
import com.student.management.dto.OrderRequest;
import com.student.management.security.JwtUtil;
import com.student.management.services.OrderService;
import org.springframework.beans.factory.annotation.Value;
@RestController
@RequestMapping("/api/checkout")
@CrossOrigin
public class CheckoutController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemRepository itemRepo;

    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private AddressRepository addressRepo;
    @PutMapping("/address")
    public ResponseEntity<?> saveAddress(
            @RequestBody Address address,
            @RequestHeader("Authorization") String token) {

        Long userId = JwtUtil.extractUserId(token.replace("Bearer ", ""));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔗 Link address to user
        address.setUser(user);

        addressRepo.save(address);

        return ResponseEntity.ok("Address saved successfully");
    }
    @GetMapping("/address")
public ResponseEntity<?> getAddresses(
        @RequestHeader("Authorization") String token
) {

    Long userId = JwtUtil.extractUserId(token.replace("Bearer ", ""));

    List<Address> addresses = addressRepo.findByUser_Id(userId);

    return ResponseEntity.ok(addresses);
}
@Value("${razorpay.key}")
private String key;

@Value("${razorpay.secret}")
private String secret;
@PostMapping("/payment/create-order")
public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> data) throws Exception {

    // ✅ FIXED amount parsing
    double amt = Double.parseDouble(data.get("amount").toString());
    int amount = (int) amt;

    RazorpayClient client = new RazorpayClient(key, secret);

    JSONObject options = new JSONObject();
    options.put("amount", amount * 100);
    options.put("currency", "INR");
    options.put("receipt", "order_" + System.currentTimeMillis());

    Order order = client.orders.create(options);

    Map<String, Object> response = new HashMap<>();
    response.put("id", order.get("id"));
    response.put("amount", order.get("amount"));
    response.put("currency", order.get("currency"));

    return ResponseEntity.ok(response);
}
@PostMapping("/payment/verify")
public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> data) throws Exception {

    String orderId = data.get("razorpay_order_id");
    String paymentId = data.get("razorpay_payment_id");
    String signature = data.get("razorpay_signature");

    String payload = orderId + "|" + paymentId;

    String generatedSignature = hmacSHA256(payload, secret);

    if (!generatedSignature.equals(signature)) {
        return ResponseEntity.status(400).body("Invalid payment");
    }

    return ResponseEntity.ok("Payment verified");
}
public String hmacSHA256(String data, String key) throws Exception {
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));

    byte[] rawHmac = mac.doFinal(data.getBytes());

    StringBuilder hex = new StringBuilder(2 * rawHmac.length);
    for (byte b : rawHmac) {
        String s = Integer.toHexString(0xff & b);
        if (s.length() == 1) hex.append('0');
        hex.append(s);
    }

    return hex.toString();
}
    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(
            @RequestBody OrderRequest request,
            @RequestHeader("Authorization") String token) {
        try {

            Long userId = JwtUtil.extractUserId(token.replace("Bearer ", ""));

            Orders order = new Orders();
            order.setUserId(userId);
            order.setTotalPrice(request.getTotalPrice());
            order.setPaymentMode(request.getPaymentMethod());

            Orders savedOrder = orderRepo.save(order);

            for (CartItem item : request.getItems()) {

    System.out.println("Incoming productId: " + item.getProductId());

    Products product = productRepo.findById(item.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

    // ✅ DECREASE QUANTITY HERE
    if (product.getStock() < item.getQuantity()) {
        throw new RuntimeException("Not enough stock");
    }

    product.setStock(product.getStock() - item.getQuantity());
    productRepo.save(product);

    // ✅ THEN CREATE ORDER ITEM
    OrderItem oi = new OrderItem();
    oi.setOrderId(savedOrder.getId());
    oi.setProductId(product.getId());
    oi.setQuantity(item.getQuantity());
    oi.setPrice(product.getPrice());

    itemRepo.save(oi);
}

            return ResponseEntity.ok("Order placed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Order failed");
        }
    }

    @Autowired
    private OrderService orderService;

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@RequestHeader("Authorization") String token) {

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        Long userId = JwtUtil.extractUserId(jwt);

        List<Map<String, Object>> result = orderService.getUserOrders(userId);

        return ResponseEntity.ok(result);
    }
}
