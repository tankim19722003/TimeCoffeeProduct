package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdatingDTO {
    @JsonProperty("table_id")
    private int tableId;
}
