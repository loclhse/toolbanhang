package com.josh.foodorder.service;

import com.josh.foodorder.domain.model.FoodItem;
import com.josh.foodorder.domain.repository.FoodItemRepository;
import com.josh.foodorder.dto.request.FoodItemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FoodItemService {
    @Autowired
    private FoodItemRepository foodItemRepository;

    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    public Optional<FoodItem> getFoodItemById(UUID id) {
        return foodItemRepository.findById(id);
    }

    public FoodItem createFoodItem(FoodItemRequestDTO request) {
        validateFoodItem(request.getName(), request.getPrice());
        FoodItem foodItem = new FoodItem();
        foodItem.setId(UUID.randomUUID());
        foodItem.setName(request.getName());
        foodItem.setPrice(request.getPrice());
        foodItem.setImg(request.getImg());
        return foodItemRepository.save(foodItem);
    }

    public FoodItem updateFoodItem(UUID id, FoodItemRequestDTO request) {
        validateFoodItem(request.getName(), request.getPrice());
        FoodItem foodItem = foodItemRepository.findById(id).orElseThrow();
        foodItem.setName(request.getName());
        foodItem.setPrice(request.getPrice());
        foodItem.setImg(request.getImg());
        return foodItemRepository.save(foodItem);
    }

    public void deleteFoodItem(UUID id) {
        foodItemRepository.deleteById(id);
    }

    public void deleteAllFoodItems() {
        foodItemRepository.deleteAll();
    }

    private void validateFoodItem(String name, double price) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Food item name must not be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Food item price must not be negative");
        }
    }
} 