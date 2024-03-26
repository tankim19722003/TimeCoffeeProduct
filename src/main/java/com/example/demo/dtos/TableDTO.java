package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableDTO {
    private String name;

    @JsonProperty("area_id")
    private int areaId;
}
