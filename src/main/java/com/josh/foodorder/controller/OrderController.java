package com.josh.foodorder.controller;

import com.josh.foodorder.domain.model.Order;

import com.josh.foodorder.dto.request.OrderRequestDTO;
import com.josh.foodorder.dto.request.AddOrderItemRequestDTO;

import com.josh.foodorder.dto.request.AdjustOrderRequestDTO;
import com.josh.foodorder.dto.response.OrderResponseDTO;
import com.josh.foodorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.josh.foodorder.dto.ApiResponse;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orderList = orderService.getAllOrders();

        List<OrderResponseDTO> orders = orderList.stream()
                .map(orderService::toOrderResponseDTO)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Fetched all orders", orders);
    }
    
    /**
     * Find order by ID (including soft-deleted orders)
     */
    @GetMapping("/{id}")
    public ApiResponse<OrderResponseDTO> findOrderById(@PathVariable UUID id) {
        try {
            Optional<Order> order = orderService.findOrderById(id);
            if (order.isPresent()) {
                OrderResponseDTO orderDTO = orderService.toOrderResponseDTO(order.get());
                return new ApiResponse<>(true, "Order found", orderDTO);
            } else {
                return new ApiResponse<>(false, "Order not found", null);
            }
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error finding order: " + e.getMessage(), null);
        }
    }

    @PostMapping("/create")
    public ApiResponse<OrderResponseDTO> createOrderFromRequest(@RequestBody OrderRequestDTO orderRequest) {
        try {
            Order createdOrder = orderService.createOrderFromRequest(orderRequest);
            return new ApiResponse<>(true, "Order created", orderService.toOrderResponseDTO(createdOrder));
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable UUID id) {
        try {
            orderService.deleteOrder(id);
            System.out.println("Successfully soft deleted order: " + id);
            return new ApiResponse<>(true, "Order soft deleted", null);
        } catch (Exception e) {
            System.out.println("Failed to soft delete order: " + id + " - " + e.getMessage());
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @DeleteMapping("/all")
    public ApiResponse<Void> deleteAllOrders() {
        try {
            orderService.deleteAllOrders();
            return new ApiResponse<>(true, "All orders deleted", null);
        } catch (Exception e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @PutMapping("/{id}/mark-done")
    public ApiResponse<OrderResponseDTO> markOrderAsDone(@PathVariable UUID id) {
        try {
            Order updatedOrder = orderService.markOrderAsDone(id);
            return new ApiResponse<>(true, "Order marked as done", orderService.toOrderResponseDTO(updatedOrder));
        } catch (Exception e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @PutMapping("/{id}/adjust")
    public ApiResponse<OrderResponseDTO> adjustOrder(@PathVariable UUID id, @RequestBody AdjustOrderRequestDTO request) {
        try {
            Order updatedOrder = orderService.adjustOrder(id, request);
            return new ApiResponse<>(true, "Order adjusted successfully", orderService.toOrderResponseDTO(updatedOrder));
        } catch (Exception e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }
}