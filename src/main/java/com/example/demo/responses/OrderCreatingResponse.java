package com.example.demo.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreatingResponse {
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
}
