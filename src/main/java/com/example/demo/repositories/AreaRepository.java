package com.example.demo.repositories;

import com.example.demo.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Integer> {
    Area findByName(String name);
}
