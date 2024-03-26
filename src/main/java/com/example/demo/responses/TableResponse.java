package com.example.demo.responses;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableResponse {
    private int id;
    private String name;
}
