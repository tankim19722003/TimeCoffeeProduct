package com.example.demo.repositories;

import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.table.id = :tableId ORDER BY o.id DESC LIMIT 1")
    Order findByTableId(@Param("tableId") int tableId);
}
