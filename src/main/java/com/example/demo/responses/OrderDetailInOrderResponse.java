package com.example.demo.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailInOrderResponse {
    private int id;
    private int quantity;

    @JsonProperty("Product")
    private ProductResponse productResponse;
}
