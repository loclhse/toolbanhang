package com.josh.foodorder.service;

import com.josh.foodorder.domain.model.Payment;
import com.josh.foodorder.domain.model.Order;
import com.josh.foodorder.domain.model.OrderItem;
import com.josh.foodorder.domain.repository.OrderRepository;
import com.josh.foodorder.domain.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import com.josh.foodorder.domain.model.Payment.PaymentStatus;
import com.josh.foodorder.domain.model.Payment.PaymentMethod;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Payment initiatePayment(Order order, PaymentMethod method) {
        if (order == null || !"DONE".equals(order.getStatus())) {
            throw new IllegalStateException("Payment can only be initiated when the order status is DONE. Current status: " +
                    (order != null ? order.getStatus() : "null"));
        }
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setTableIds(order.getTableIds());
        payment.setOrderCreatedAt(order.getCreatedAt());
        payment.setPaymentMethod(method);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        // Set img field only for BANK_TRANSFER payment method
        if (method == PaymentMethod.BANK_TRANSFER) {
            payment.setImg("https://homepage.momocdn.net/blogscontents/momo-upload-api-220808102122-637955508824191258.png"); // You can customize this image path or generate QR code
        }

        // Convert order items to payment items
        List<Payment.PaymentItem> paymentItems = new ArrayList<>();
        double totalAmount = 0.0;
        if (order.getItems() != null) {
            for (OrderItem orderItem : order.getItems()) {
                double subtotal = orderItem.getFoodItem().getPrice() * orderItem.getQuantity();
                totalAmount += subtotal;
                Payment.PaymentItem paymentItem = new Payment.PaymentItem(
                        orderItem.getFoodItem().getName(),
                        orderItem.getFoodItem().getPrice(),
                        orderItem.getQuantity(),
                        subtotal
                );
                paymentItems.add(paymentItem);
            }
        }
        payment.setItems(paymentItems);
        payment.setTotalAmount(totalAmount);

        Payment savedPayment = paymentRepository.save(payment);

        // Send WebSocket notification about payment initiation
        System.out.println("Broadcasting payment update (initiation): " + savedPayment);
        messagingTemplate.convertAndSend("/topic/payments", savedPayment);

        return savedPayment;
    }

    public Payment confirmPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaidAt(java.time.LocalDateTime.now());
        List<Payment> pendingPayments = paymentRepository.findByOrderIdAndPaymentStatus(
                payment.getOrderId(), PaymentStatus.PENDING);

        for (Payment pendingPayment : pendingPayments) {
            if (!pendingPayment.getId().equals(paymentId)) {
                paymentRepository.delete(pendingPayment);
            }
        }

        // Get the order and soft delete it
        Order ord = orderRepository.findById(payment.getOrderId()).orElseThrow();


        // Soft delete the order instead of hard delete
        ord.setDeleted(true);
        orderRepository.save(ord);

        Payment savedPayment = paymentRepository.save(payment);

        // Send WebSocket notification about payment confirmation

        messagingTemplate.convertAndSend("/topic/payments", savedPayment);
        messagingTemplate.convertAndSend("/topic/orders/deleted", ord.getId());

        return savedPayment;
    }

    public List<Payment> getAllPaymentsOrderByDateDesc() {
        return paymentRepository.findAllOrderByPaidAtDesc();
    }


    public void deleteAllPayments() {
        paymentRepository.deleteAll();
    }

    public Optional<Payment> findByID(UUID id){
        return paymentRepository.findById(id);
    }
}