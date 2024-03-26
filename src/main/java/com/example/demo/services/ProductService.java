package com.example.demo.services;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.responses.CategoryResponse;
import com.example.demo.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws Exception {
        boolean isProductExisting = productRepository.existsByName(productDTO.getName());
        if (isProductExisting) throw new Exception("Product existed!");

        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                () -> new Exception("Category does not exist")
        );
        Product product =  Product.builder()
                .createdAt(new Date(System.currentTimeMillis()))
                .updatedAt(new Date(System.currentTimeMillis()))
                .category(category)
                .price(productDTO.getPrice())
                .name(productDTO.getName())
                .build();
        productRepository.save(product);
        return product;
    }

    @Override
    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(Product product) throws Exception {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(
                () -> new Exception("Data does not exist")
        );

        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(
                () -> new Exception("Category does not exist")
        );
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(category);

        return productRepository.save(existingProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() throws Exception {
        List<Product> products = productRepository.findAll();

        List<ProductResponse> productResponses = products.stream().map(
                product -> {
                    return ProductResponse.builder()
                            .id(product.getId())
                            .price(product.getPrice())
                            .categoryResponse(
                                    CategoryResponse.builder()
                                            .name(product.getCategory().getName())
                                            .id(product.getCategory().getId())
                                            .build()
                            )
                            .name(product.getName())
                            .build();
                }).collect(Collectors.toList());
        if (products.isEmpty()) throw new Exception("Empty!");
        return productResponses;
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(int categoryId) throws Exception {
        List<Product> productList = new ArrayList<>();
        productList = productRepository.findByCategoryId(categoryId);
        if (productList.isEmpty()) throw new Exception("Products with category does not exist");

        List<ProductResponse> productResponses= new ArrayList<>();

        productResponses = productList.stream().map(
                product -> {
                    return ProductResponse.builder()
                            .id(product.getId())
                            .price(product.getPrice())
                            .categoryResponse(
                                    CategoryResponse.builder()
                                            .name(product.getCategory().getName())
                                            .id(product.getCategory().getId())
                                            .build()
                            )
                            .name(product.getName())
                            .build();
                }).collect(Collectors.toList());

        return productResponses;
    }
}
