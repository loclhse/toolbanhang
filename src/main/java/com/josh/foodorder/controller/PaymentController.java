package com.josh.foodorder.controller;

import com.josh.foodorder.domain.model.Order;
import com.josh.foodorder.domain.model.Payment;
import com.josh.foodorder.domain.model.Payment.PaymentMethod;
import com.josh.foodorder.domain.model.Payment.PaymentStatus;
import com.josh.foodorder.service.OrderService;
import com.josh.foodorder.service.PaymentService;
import com.josh.foodorder.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;
    

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPaymentsOrderByDateDesc();
    }

    @PostMapping("/initiate/{orderId}")
    public ResponseEntity<?> initiatePayment(@PathVariable UUID orderId, @RequestParam PaymentMethod method) {
        try {
            Optional<Order> order = orderService.findOrderById(orderId);
            if(order.isEmpty()){
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Order not found", null));
            }
            Order ord = order.get();
            Payment payment = paymentService.initiatePayment(ord, method);
            return ResponseEntity.ok(payment);
        } catch (IllegalStateException e) {
            // This will catch the exception when order status is not DONE
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "An error occurred: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Payment> confirmPayment(@PathVariable UUID id) {
        Payment payment = paymentService.confirmPayment(id);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllPayments() {
        try {
            paymentService.deleteAllPayments();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable UUID id) {
        try {
            Optional<Payment> payment = paymentService.findByID(id);
            if (payment.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Payment found", payment.get()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Payment not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "An error occurred: " + e.getMessage(), null));
        }
    }
}