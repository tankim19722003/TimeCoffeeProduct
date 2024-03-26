package com.example.demo.repositories;

import com.example.demo.model.Tables;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<Tables, Integer> {
    boolean existsByName(String name);
    List<Tables> findAllByAreaId(int AreaId);
}
