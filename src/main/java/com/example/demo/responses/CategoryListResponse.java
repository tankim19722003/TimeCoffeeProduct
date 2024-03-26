package com.example.demo.responses;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryListResponse {
    private List<CategoryResponse> categoryResponse;
}
