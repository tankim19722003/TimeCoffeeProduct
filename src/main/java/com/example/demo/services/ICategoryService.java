package com.example.demo.services;

import com.example.demo.dtos.CategoryDTO;
import com.example.demo.responses.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse createCategory(CategoryDTO categoryDTO) throws Exception;
    void delete(int categoryId);
    List<CategoryResponse> findAllCategory() throws Exception;
}
