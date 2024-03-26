package com.example.demo.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private int id;
    private int quantity;

    private ProductResponse productResponse;

    @JsonProperty("order_id")
    private int orderId;
}
