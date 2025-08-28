package com.josh.foodorder.controller;

import com.josh.foodorder.dto.ErrorResponse;
import com.josh.foodorder.dto.request.ItemMarkEvent;
import com.josh.foodorder.dto.request.MarkItemRequestDTO;
import com.josh.foodorder.dto.response.MarkItemResponseDTO;

import com.josh.foodorder.service.MarkService;
import com.josh.foodorder.domain.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class MarkController {

    @Autowired
    private MarkService markService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public MarkController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/{orderId}/items/{itemId}/mark")
    public ResponseEntity<?> markItem(
            @PathVariable String orderId,
            @PathVariable String itemId,
            @RequestBody MarkItemRequestDTO request) {
        try {
            markService.markItem(orderId, itemId, request.isMarked());
            MarkItemResponseDTO response = new MarkItemResponseDTO(
                    orderId,
                    itemId,
                    request.isMarked()
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error processing mark request: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to mark item: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{orderId}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable String orderId) {
        try {
            List<OrderItem> orderItems = markService.getOrderItems(orderId);
            return ResponseEntity.ok(orderItems);
        } catch (Exception e) {
            System.err.println("Error getting order items: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to get order items: " + e.getMessage()));
        }
    }

    @MessageMapping("/order-item-marks")
    public void onMark(ItemMarkEvent evt) {
        simpMessagingTemplate.convertAndSend("/topic/order-item-marks", evt);
    }
}