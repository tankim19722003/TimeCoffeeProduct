package com.example.demo.dtos;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOrder {
    private int product;
    private int quantity;
}
