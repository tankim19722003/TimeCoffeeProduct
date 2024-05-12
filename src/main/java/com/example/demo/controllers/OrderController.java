package com.example.demo.controllers;


import com.example.demo.MessageReponse.Message;
import com.example.demo.dtos.OrderDTO;
import com.example.demo.dtos.OrderUpdatingDTO;
import com.example.demo.responses.OrderHasDetailResponse;
import com.example.demo.responses.OrderResponse;
import com.example.demo.services.OrderService;
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
            OrderHasDetailResponse orderCreatingResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok().body(orderCreatingResponse);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Message.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/changeTableInOrder")
    public ResponseEntity<?> updateTableInOrder(
            @RequestBody OrderUpdatingDTO orderUpdatingDTO
    ) {
        try {
            OrderResponse orderResponse = orderService.updateOrder(orderUpdatingDTO);
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
            OrderHasDetailResponse orderResponse = orderService.getOrder(id);
            return ResponseEntity.ok().body(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Message.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<?> findOrderByTableId(
            @PathVariable("tableId") int tableId
    ) throws Exception {
        return ResponseEntity.ok().body(orderService.findOrderByTableId(tableId));
    }

    @PutMapping("/pay_order")
    public void payOrder(
            @RequestParam("table_id") int tableId
    ) {
        try {
            orderService.payOrder(tableId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
