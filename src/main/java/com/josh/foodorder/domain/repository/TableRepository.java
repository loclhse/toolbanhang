package com.josh.foodorder.domain.repository;

import com.josh.foodorder.domain.model.Table;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TableRepository extends MongoRepository<Table, UUID> {
} 