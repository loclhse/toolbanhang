package com.josh.foodorder.controller;

import com.josh.foodorder.domain.model.Table;
import com.josh.foodorder.dto.request.TableCreateRequestDTO;
import com.josh.foodorder.service.TableService;
import com.josh.foodorder.domain.repository.TableRepository;
import com.josh.foodorder.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tables")
public class TableController {
    @Autowired
    private TableService tableService;
    @Autowired
    private TableRepository tableRepository;

    @GetMapping
    public ApiResponse<List<Table>> getAllTables() {
        List<Table> tables = tableService.getAllTables();
        return new ApiResponse<>(true, "Fetched all tables", tables);
    }

    @GetMapping("/{id}")
    public ApiResponse<Table> getTableById(@PathVariable UUID id) {
        Optional<Table> table = tableRepository.findById(id);
        if (table.isPresent()) {
            return new ApiResponse<>(true, "Table found", table.get());
        } else {
            return new ApiResponse<>(false, "Table not found", null);
        }
    }

    @PostMapping
    public ApiResponse<Table> createTable(@RequestBody TableCreateRequestDTO request) {
        Table table = tableService.createTable(request);
        return new ApiResponse<>(true, "Table created", table);
    }

    @PutMapping("/{id}")
    public ApiResponse<Table> updateTable(@PathVariable UUID id, @RequestBody Table table) {
        table.setId(id);
        Table updated = tableRepository.save(table);
        return new ApiResponse<>(true, "Table updated", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTable(@PathVariable UUID id) {
        tableRepository.deleteById(id);
        return new ApiResponse<>(true, "Table deleted", null);
    }

    @DeleteMapping("/all")
    public ApiResponse<Void> deleteAllTables() {
        tableService.deleteAllTables();
        return new ApiResponse<>(true, "All tables deleted", null);
    }
} 