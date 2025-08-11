package com.josh.foodorder.service;

import com.josh.foodorder.domain.model.Order;
import com.josh.foodorder.domain.model.OrderItem;
import com.josh.foodorder.domain.model.FoodItem;
import com.josh.foodorder.domain.repository.OrderRepository;
import com.josh.foodorder.domain.repository.FoodItemRepository;
import com.josh.foodorder.dto.request.OrderRequestDTO;
import com.josh.foodorder.dto.request.AddOrderItemRequestDTO;

import com.josh.foodorder.dto.request.AdjustOrderRequestDTO;
import com.josh.foodorder.domain.model.Table;
import com.josh.foodorder.dto.response.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private TableService tableService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

        public List<Order> getAllOrders() {
        // Get orders sorted by creation time in descending order (newest first)
        List<Order> orders = orderRepository.findByIsDeletedFalse();

        // Broadcast all orders via WebSocket for real-time updates
        System.out.println("📡 Broadcasting all orders via WebSocket: " + orders.size() + " orders");
        List<OrderResponseDTO> orderDTOs = orders.stream()
            .map(this::toOrderResponseDTO)
            .collect(java.util.stream.Collectors.toList());
        messagingTemplate.convertAndSend("/topic/orders/all", orderDTOs);

        return orders;
    }
    
    public Optional<Order> findOrderById(UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            System.out.println("Found order: " + id + ", isDeleted: " + order.get().isDeleted());
        } else {
            System.out.println("Order not found: " + id);
        }
        return order;
    }

@Transactional
    public Order createOrderFromRequest(OrderRequestDTO orderRequest) {

        if (orderRequest.getTableIds() == null || orderRequest.getTableIds().isEmpty() || orderRequest.getNumberOfPeople() <= 0 || orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("Invalid order request");
        }

        // Create new order with auto-generated ID and timestamp
        Order order = new Order();
        order.setId(UUID.randomUUID());  // Auto-generate ID
        order.setTableIds(orderRequest.getTableIds());
        order.setNumberOfPeople(orderRequest.getNumberOfPeople());
        order.setStatus("PENDING");  // Set status to PENDING
        order.setCreatedAt(LocalDateTime.now());  // Set creation timestamp

        // Create OrderItems from the request
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequestDTO.OrderItemRequestDTO itemRequest : orderRequest.getItems()) {
            // Fetch the food item from database
            FoodItem foodItem = foodItemRepository.findById(itemRequest.getFoodItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Food item not found: " + itemRequest.getFoodItemId()));
            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID());  // Auto-generate ID
            orderItem.setOrder(order);
            orderItem.setFoodItem(foodItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        System.out.println("Broadcasting new order via WebSocket: " + savedOrder.getId());
        messagingTemplate.convertAndSend("/topic/orders", toOrderResponseDTO(savedOrder));

        return savedOrder;
    }

    public void deleteOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Order not found: " + id)
        );
        order.setDeleted(true);
        orderRepository.save(order);
        System.out.println("🗑️ Soft deleted order: " + id);
        System.out.println("📡 Broadcasting order deletion via WebSocket: " + id);
        messagingTemplate.convertAndSend("/topic/orders/deleted", id.toString());
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
    
    public Order adjustOrder(UUID orderId, AdjustOrderRequestDTO request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        // Validate basic requirements
        if (request.getTableIds() == null || request.getTableIds().isEmpty()) {
            throw new IllegalArgumentException("Table IDs cannot be empty");
        }
        if (request.getNumberOfPeople() <= 0) {
            throw new IllegalArgumentException("Number of people must be greater than 0");
        }

        // Update table assignment
        order.setTableIds(request.getTableIds());

        // Update number of people
        order.setNumberOfPeople(request.getNumberOfPeople());

        // Process food item adjustments
        if (request.getItems() != null) {
            List<OrderItem> currentItems = order.getItems() != null ? new ArrayList<>(order.getItems()) : new ArrayList<>();

            for (AdjustOrderRequestDTO.OrderItemAdjustmentDTO adjustment : request.getItems()) {
                FoodItem foodItem = foodItemRepository.findById(adjustment.getFoodItemId())
                        .orElseThrow(() -> new IllegalArgumentException("Food item not found: " + adjustment.getFoodItemId()));

                // Find existing item with same food item
                OrderItem existingItem = currentItems.stream()
                        .filter(item -> item.getFoodItem().getId().equals(foodItem.getId()))
                        .findFirst()
                        .orElse(null);

                if (adjustment.getQuantity() <= 0) {
                    // Remove item if quantity is 0 or negative
                    if (existingItem != null) {
                        currentItems.remove(existingItem);
                    }
                } else {
                    // Add or update item
                    if (existingItem != null) {
                        // Update existing item quantity
                        existingItem.setQuantity(adjustment.getQuantity());
                    } else {
                        // Add new item
                        OrderItem newItem = new OrderItem();
                        newItem.setId(UUID.randomUUID());
                        newItem.setOrder(order);
                        newItem.setFoodItem(foodItem);
                        newItem.setQuantity(adjustment.getQuantity());
                        currentItems.add(newItem);
                    }
                }
            }

            order.setItems(currentItems);
        }

        // If order was DONE, set back to PENDING
        if ("DONE".equals(order.getStatus())) {
            order.setStatus("PENDING");
        }

        Order savedOrder = orderRepository.save(order);

        // Send WebSocket notification for order adjustment
        System.out.println("📡 Broadcasting order adjustment via WebSocket: " + savedOrder.getId());
        messagingTemplate.convertAndSend("/topic/orders", toOrderResponseDTO(savedOrder));

        return savedOrder;
    }

    /**
     * Chef marks order as DONE (food preparation completed).
     * Only PENDING orders can be marked as DONE.
     */
    public Order markOrderAsDone(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalArgumentException("Only PENDING orders can be marked as DONE. Current status: " + order.getStatus());
        }

        order.setStatus("DONE");
        Order savedOrder = orderRepository.save(order);

        // Send WebSocket notification about order status change
        System.out.println("📡 Broadcasting order mark done via WebSocket: " + savedOrder.getId());
        messagingTemplate.convertAndSend("/topic/orders", toOrderResponseDTO(savedOrder));

        return savedOrder;
    }

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        List<Integer> tableNumbers = order.getTableIds().stream()
                .map(tid -> {
                    Table table = tableService.getTableById(tid);
                    return table != null ? table.getNumber() : null;
                })
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toList());
        return new OrderResponseDTO(order, tableNumbers);
    }
}