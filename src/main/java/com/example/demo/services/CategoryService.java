package com.example.demo.services;

import com.example.demo.dtos.CategoryDTO;
import com.example.demo.model.Category;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.responses.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    @Override
    public CategoryResponse createCategory(CategoryDTO categoryDTO) throws Exception {
        boolean isCategoryExisting = categoryRepository.existsByName(categoryDTO.getName());
        if (isCategoryExisting) throw new Exception("Category existed");

        Category category = categoryRepository.save(
                Category.builder()
                        .name(categoryDTO.getName())
                        .build()
        );

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @Override
    public void delete(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryResponse> findAllCategory() throws Exception {
        List<Category> categories = new ArrayList<>();

        categories = categoryRepository.findAll();
        if (categories.isEmpty()) throw new Exception("Data not found");
        List <CategoryResponse> categoryReponseList = categories.stream()
                .map(category -> {
                CategoryResponse categoryResponse = CategoryResponse.builder()
                        .name(category.getName())
                        .id(category.getId())
                        .build();
                return categoryResponse;
        }).collect(Collectors.toList());
        return categoryReponseList;
    }
}
