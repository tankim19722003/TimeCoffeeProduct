package com.example.demo.responses;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemOrderDetailResponse {
    private int id;
    private int quantity;

    private ProductResponse productResponse;

}
