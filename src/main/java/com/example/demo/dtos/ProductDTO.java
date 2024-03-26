package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String name;

    private int price;

    @JsonProperty("category_id")
    private int categoryId;
}
