package com.josh.foodorder.service;

import com.josh.foodorder.domain.model.Table;
import com.josh.foodorder.dto.request.TableCreateRequestDTO;
import com.josh.foodorder.domain.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    public Table createTable(TableCreateRequestDTO request) {
        Table table = new Table(request.getNumber());
        return tableRepository.save(table);
    }

    public Table saveTable(Table table) {
        return tableRepository.save(table);
    }

    public void deleteAllTables() {
        tableRepository.deleteAll();
    }

    public Table getTableById(UUID id) {
        return tableRepository.findById(id).orElse(null);
    }
}