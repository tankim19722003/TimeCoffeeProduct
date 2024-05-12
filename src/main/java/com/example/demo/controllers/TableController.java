package com.example.demo.controllers;

import com.example.demo.dtos.TableDTO;
import com.example.demo.model.Tables;
import com.example.demo.responses.TableResponse;
import com.example.demo.responses.TableResponseByArea;
import com.example.demo.services.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/v1/timecoffee/table")
@RequiredArgsConstructor
public class TableController {
    private final TableService tableService;

    @PostMapping("")
    public ResponseEntity<?> createTable(
            @RequestBody TableDTO tableDTO) {
        try {
            Tables tableCreated = tableService.createTable(tableDTO);
            return ResponseEntity.ok().body(tableCreated);
        } catch(Exception e){
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable int id
    ) {
        tableService.deleteTable(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllByAreaId(
            @PathVariable int id
    ) {
        List<TableResponseByArea> tables = new ArrayList<>();
        try {
            tables = tableService.findAllByAreaId(id);
            return ResponseEntity.ok().body(tables);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
