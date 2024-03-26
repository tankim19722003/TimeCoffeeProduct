package com.example.demo.services;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.responses.ProductResponse;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    void deleteProduct(int productId);
    Product updateProduct(Product product) throws Exception;

    List<ProductResponse> getAllProducts() throws Exception;

    List<ProductResponse> getProductsByCategoryId(int categoryId) throws Exception;

//    List<Product> findProductByKeyWord(String keyword);
}
