package com.example.demo.controllers;


import com.example.demo.dtos.CategoryDTO;
import com.example.demo.responses.CategoryResponse;
import com.example.demo.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/timecoffee/category")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("")
    public ResponseEntity<?>createCategory(
            @RequestBody CategoryDTO categoryDTO
    ) {
        try {
            CategoryResponse categoryResponse = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok().body(categoryResponse);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCategory() {
        List<CategoryResponse> categoryList = new ArrayList<>();
        try {
            categoryList = categoryService.findAllCategory();
            return ResponseEntity.ok().body(categoryList);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public void deleteCategory(
            @PathVariable int id
    ) {
        categoryService.delete(id);
    }
}
