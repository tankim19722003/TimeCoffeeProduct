package com.example.demo.model;

import com.example.demo.responses.OrderDetailResponse;
import com.example.demo.responses.ProductResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@Builder
@Table(name = "order_details")
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
        ProductResponse productResponse = ProductResponse.builder()
                .price(orderDetail.getProduct().getPrice())
                .name(orderDetail.getProduct().getName())
                .id(orderDetail.getProduct().getId())
                .categoryId(orderDetail.getProduct().getCategory().getId())
                .build();
        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                .orderId(orderDetail.getOrder().getId())
                .quantity(orderDetail.getQuantity())
                .id(orderDetail.getId())
                .productResponse(productResponse)
                .build();
        return orderDetailResponse;
    }

}
