package com.example.demo.dtos;

import com.example.demo.model.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {

    private int quantity;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("order_id")
    private int orderId;
}
