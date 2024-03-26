package com.example.demo.controllers;

import com.example.demo.dtos.OrderDetailDTO;
import com.example.demo.model.OrderDetail;
import com.example.demo.responses.ListOrderDetailResponse;
import com.example.demo.responses.OrderDetailResponse;
import com.example.demo.responses.ProductResponse;
import com.example.demo.services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/timecoffee/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @RequestBody OrderDetailDTO orderDetailDTO
    ) {
        try {
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            OrderDetailResponse orderDetailResponse = orderDetail.toOrderDetailResponse(orderDetail);
            return ResponseEntity.ok().body(orderDetailResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrderDetail(
            @RequestBody int id
    ) {
        orderDetailService.deleteOrderDetail(id);
    }

    @PutMapping("/{orderDetailId}")
    public ResponseEntity<?> updateOrderDetail(
            @RequestBody OrderDetailDTO orderDetailDTO,
            @PathVariable("orderDetailId") int orderDetailId
    ) {
        try {
            OrderDetail orderDetailUpdated = orderDetailService.updateOrderDetail(orderDetailDTO, orderDetailId);
            OrderDetailResponse orderDetailResponse = orderDetailUpdated.toOrderDetailResponse(orderDetailUpdated);
            return ResponseEntity.ok().body(orderDetailResponse);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<?> findAllByOrderId(
            @PathVariable("order_id") int id
    ) {
        try {
            ListOrderDetailResponse listOrderDetailResponse = orderDetailService.findAllByOrderId(id);
            return ResponseEntity.ok().body(listOrderDetailResponse);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
