package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatingAndAddingItemDTO {
    @JsonProperty("table_id")
    private int tableId;
    private List<AddingOrderDetailDTO>  addingOrderDetails;
    private List<UpdatingOrderDetailItem> updatingOrderDetails;
}
