package com.example.demo.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

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
}
