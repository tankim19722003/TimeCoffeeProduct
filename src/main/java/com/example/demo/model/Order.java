package com.example.demo.model;

import com.example.demo.responses.OrderResponse;
import com.example.demo.responses.TableResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@Builder
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "total_money")
    private int totalMoney;


    @ManyToOne
    @JoinColumn(name = "table_id")
    @JsonBackReference
    private Tables table;

    @Column(name = "is_paid")
    private boolean isPaid;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
//    @JsonIgnore
    List<OrderDetail> orderDetails = new ArrayList<>();

    public static OrderResponse toOrderResponse(Order order) {
        TableResponse tableResponse = Tables.toTableResponse(order.getTable());

        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .totalMoney(order.getTotalMoney())
                .createAt(order.getOrderDate())
                .tableResponse(tableResponse)
                .build();
        return orderResponse;
    }

}
