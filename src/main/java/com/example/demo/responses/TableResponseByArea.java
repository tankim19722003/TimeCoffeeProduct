package com.example.demo.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableResponseByArea {
    private int id;
    private String name;

    @JsonProperty("status")
    private boolean status;
}
