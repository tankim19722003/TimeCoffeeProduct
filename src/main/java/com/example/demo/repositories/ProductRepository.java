package com.example.demo.repositories;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);
    List<Product> findByCategoryId(int categoryId);
}
