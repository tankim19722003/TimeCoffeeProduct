package com.example.demo.controllers;


import com.example.demo.dtos.OrderDTO;
import com.example.demo.dtos.OrderUpdatingDTO;
import com.example.demo.responses.OrderCreatingResponse;
import com.example.demo.responses.OrderResponse;
import com.example.demo.services.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/timecoffee/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderDTO orderDTO
    ){
        try {
            OrderCreatingResponse orderCreatingResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok().body(orderCreatingResponse);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/changeTableInOrder/{order_id}")
    public ResponseEntity<?> updateTableInOrder(
            @PathVariable("order_id") int orderId,
            @RequestBody OrderUpdatingDTO orderUpdatingDTO
    ) {
        try {
            OrderResponse orderResponse = orderService.updateOrder(orderUpdatingDTO, orderId);
            return ResponseEntity.ok().body(orderResponse);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(
            @PathVariable int id
    ) {
        orderService.deleteOrder(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @PathVariable int id
    ) {
        try {
            OrderResponse orderResponse = orderService.getOrder(id);
            return ResponseEntity.ok().body(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
