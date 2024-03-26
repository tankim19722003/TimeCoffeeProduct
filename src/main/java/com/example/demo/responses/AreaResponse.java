package com.example.demo.responses;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaResponse {
    private int id;
    private String name;
}
