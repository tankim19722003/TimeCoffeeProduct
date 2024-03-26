package com.example.demo.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    @JsonProperty("order_id")
    private int id;

    @JsonProperty("total_money")
    private int totalMoney;

    @JsonProperty("created_at")
    private Date createAt;

    @JsonProperty("table")
    private TableResponse tableResponse;
}
