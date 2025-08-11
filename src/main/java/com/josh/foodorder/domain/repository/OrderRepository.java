package com.josh.foodorder.domain.repository;

import com.josh.foodorder.domain.model.Order;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface OrderRepository extends MongoRepository<Order, UUID> {

    List<Order> findByIsDeletedFalse();
    

}