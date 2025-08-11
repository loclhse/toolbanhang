package com.josh.foodorder.controller;

import com.josh.foodorder.domain.model.FoodItem;

import com.josh.foodorder.dto.ApiResponse;
import com.josh.foodorder.dto.request.FoodItemRequestDTO;
import com.josh.foodorder.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/food-items")
public class FoodItemController {
    @Autowired
    private FoodItemService foodItemService;

    @GetMapping
    public ApiResponse<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> items = foodItemService.getAllFoodItems();
        return new ApiResponse<>(true, "Fetched all food items", items);
    }

    @GetMapping("/{id}")
    public ApiResponse<FoodItem> getFoodItemById(@PathVariable UUID id) {
        Optional<FoodItem> item = foodItemService.getFoodItemById(id);
        if (item.isPresent()) {
            return new ApiResponse<>(true, "Food item found", item.get());
        } else {
            return new ApiResponse<>(false, "Food item not found", null);
        }
    }

    @PostMapping
    public ApiResponse<FoodItem> createFoodItem(@RequestBody FoodItemRequestDTO request) {
        FoodItem item = foodItemService.createFoodItem(request);
        return new ApiResponse<>(true, "Food item created", item);
    }

    @PutMapping("/{id}")
    public ApiResponse<FoodItem> updateFoodItem(@PathVariable UUID id, @RequestBody FoodItemRequestDTO request) {
        FoodItem updated = foodItemService.updateFoodItem(id, request);
        return new ApiResponse<>(true, "Food item updated", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFoodItem(@PathVariable UUID id) {
        foodItemService.deleteFoodItem(id);
        return new ApiResponse<>(true, "Food item deleted", null);
    }

    @DeleteMapping("/all")
    public ApiResponse<Void> deleteAllFoodItems() {
        foodItemService.deleteAllFoodItems();
        return new ApiResponse<>(true, "All food items deleted", null);
    }
} 