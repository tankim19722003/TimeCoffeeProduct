package com.example.demo.controllers;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.responses.ProductResponse;
import com.example.demo.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/timecoffee/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping()
    public ResponseEntity<?> createProduct(
            @RequestBody ProductDTO productDTO
    ) {
        try {
            Product product = productService.createProduct(productDTO);
            return ResponseEntity.ok().body(product);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts() {
       try {
           return ResponseEntity.ok().body(productService.getAllProducts());
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PutMapping()
    public ResponseEntity<?>updateProduct(
            @RequestBody Product product
    ) {
        try {
            Product productSaved = productService.updateProduct(product);
            return ResponseEntity.ok().body(productSaved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable int id
    ) {
        productService.deleteProduct(id);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> findProductsByCategoryId(
            @PathVariable int categoryId
    ) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        try {
            productResponseList = productService.getProductsByCategoryId(categoryId);
            return ResponseEntity.ok().body(productResponseList);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

