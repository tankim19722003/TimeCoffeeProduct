package com.example.demo.controllers;

import com.example.demo.MessageReponse.Message;
import com.example.demo.dtos.OrderDetailDTO;
import com.example.demo.dtos.UpdatingAndAddingItemDTO;
import com.example.demo.model.OrderDetail;
import com.example.demo.responses.ListOrderDetailResponse;
import com.example.demo.responses.OrderDetailResponse;
import com.example.demo.services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable int id
    ) throws Exception {
        orderDetailService.deleteOrderDetail(id);
    }


    @GetMapping("/{order_id}")
    public ResponseEntity<?> findAllByOrderId(
            @PathVariable("order_id") int id
    ) {
        try {
            ListOrderDetailResponse listOrderDetailResponse = orderDetailService.findAllByOrderId(id);
            return ResponseEntity.ok().body(listOrderDetailResponse);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(
                    Message.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/table/{table_id}")
    public ResponseEntity<?> getOrderDetailByTableId(
            @PathVariable("table_id") int tableId
    ) {
        ListOrderDetailResponse listOrderDetailResponse = orderDetailService.getOrderDetailOfTable(tableId);
        return ResponseEntity.ok().body(listOrderDetailResponse);
    }

    @PutMapping("")
    public void UpdatingAndAddingOrderDetail(
            @RequestBody UpdatingAndAddingItemDTO updatingAndAddingItemDTO
            ) {
        try {
            orderDetailService.updatingAndAddingOrder(updatingAndAddingItemDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
