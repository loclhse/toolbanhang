package com.josh.foodorder.config;

import com.josh.foodorder.domain.model.Table;
import com.josh.foodorder.domain.repository.TableRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class TableSeatInitializer {
    @Value("${foodorder.default.tableCount:10}")
    private int tableCount;

    

    private final TableRepository tableRepository;

    public TableSeatInitializer(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @PostConstruct
    public void initializeTables() {
        if (tableRepository.count() == 0) { // Only create if no tables exist
            for (int t = 1; t <= tableCount; t++) {
                Table table = new Table();
                table.setNumber(t);
                tableRepository.save(table);
            }
        }
    }
} 