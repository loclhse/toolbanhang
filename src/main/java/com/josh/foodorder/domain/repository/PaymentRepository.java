package com.josh.foodorder.domain.repository;

import com.josh.foodorder.domain.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends MongoRepository<Payment, UUID> {
    
    // Find all payments sorted by paidAt in descending order (most recent first)
    @Query(value = "{}", sort = "{'paidAt': -1}")
    List<Payment> findAllOrderByPaidAtDesc();
    
    // Find payments by order ID
    List<Payment> findByOrderId(UUID orderId);
    
    // Find payments by table ID
    List<Payment> findByTableIdsContaining(UUID tableId);
    
    // Find pending payments by order ID
    List<Payment> findByOrderIdAndPaymentStatus(UUID orderId, Payment.PaymentStatus paymentStatus);
} 