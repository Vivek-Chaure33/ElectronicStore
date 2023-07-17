package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.ProductDto;

import java.util.List;

public interface ProductServiceI {

    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto , String productId);

    //delete
    void deleteProduct(String productId);

    //get All
    PageableResponse<ProductDto> getAllProducts(int pageSize , int pageNumber , String sortBy , String sortDir);

    //get all live
    PageableResponse<ProductDto> getAllLive(int pageSize , int pageNumber , String sortBy , String sortDir);
    //get single
    ProductDto getProduct(String productId);

    //search
    PageableResponse<ProductDto> searchByTitle(String subString ,int pageSize , int pageNumber , String sortBy , String sortDir);

}
