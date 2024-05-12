package com.example.demo.responses;

import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Tables;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHasDetailResponse {
    @JsonProperty("order_id")
    private int id;

    @JsonProperty("total_money")
    private int totalMoney;

    @JsonProperty("created_at")
    private Date createAt;

    @JsonProperty("table")
    private TableResponse tableResponse;

    @JsonProperty("order_details")
    List<OrderDetailInOrderResponse> orderDetailResponseList;

    public static OrderHasDetailResponse fromOrderDetail( Order order) {

        List<OrderDetailInOrderResponse> orderDetailInOrderResponse = order.getOrderDetails().stream().map(
                orderDetail -> {
                    return OrderDetail.toOrderDetailInOrderResponse(orderDetail);
                }
        ).collect(Collectors.toList());

        OrderHasDetailResponse orderCreatingResponse = OrderHasDetailResponse.builder()
                .tableResponse(Tables.toTableResponse(order.getTable()))
                .id(order.getId())
                .createAt(order.getOrderDate())
                .totalMoney(order.getTotalMoney())
                .orderDetailResponseList(orderDetailInOrderResponse)
                .build();
        return orderCreatingResponse;
    }
}
