package com.example.demo.responses;

import com.example.demo.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private int id;

    private String name;

    private int price;

    @JsonProperty("category")
    private CategoryResponse categoryResponse;
}
