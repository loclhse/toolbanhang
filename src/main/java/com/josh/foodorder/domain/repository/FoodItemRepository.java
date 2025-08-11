package com.josh.foodorder.domain.repository;

import com.josh.foodorder.domain.model.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

public interface FoodItemRepository extends MongoRepository<FoodItem, UUID> {
} 