package com.example.demo.responses;

import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListOrderDetailResponse {

    @JsonProperty("order")
    private OrderResponse orderResponse;

    @JsonProperty("order_details")
    List<ItemOrderDetailResponse> itemOrderDetailList;

    public static ListOrderDetailResponse fromListOrderDetail(
            List<OrderDetail> orderDetails,
            Order order
    ){
        List<ItemOrderDetailResponse> itemOrderDetailResponses = new ArrayList<>();
        itemOrderDetailResponses = orderDetails.stream().map(
                orderDetail -> {
                    ProductResponse productResponse = ProductResponse.builder()
                            .name(orderDetail.getProduct().getName())
                            .id(orderDetail.getProduct().getId())
                            .price(orderDetail.getProduct().getPrice())
                            .categoryResponse(
                                    CategoryResponse.builder()
                                            .name(orderDetail.getProduct().getCategory().getName())
                                            .id(orderDetail.getProduct().getCategory().getId())
                                            .build()
                            )
                            .build();
                    return ItemOrderDetailResponse.builder()
                            .quantity(orderDetail.getQuantity())
                            .id(orderDetail.getId())
                            .productResponse(productResponse)
                            .build();
                }).collect(Collectors.toList());
        OrderResponse orderResponse = Order.toOrderResponse(order);

        ListOrderDetailResponse listOrderDetailResponse = new ListOrderDetailResponse();
        listOrderDetailResponse.setItemOrderDetailList(itemOrderDetailResponses);
        listOrderDetailResponse.setOrderResponse(orderResponse);

        return listOrderDetailResponse;
    }
}
