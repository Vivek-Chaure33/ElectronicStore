package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dto.CategoryDto;
import com.lcwd.electronic.store.dto.PageableResponse;

import java.util.List;

public interface CategoryService {

    //create
    CategoryDto create(CategoryDto categoryDto);

    //update
    CategoryDto update(CategoryDto categoryDto , String categoryId);

    //delete

    void delete(String categoryId);

    //get all

    PageableResponse<CategoryDto> getAllCategory(int pageNumber , int pageSize , String sortBy,String sortDir);

    // get single category

    CategoryDto getCategory(String categoryId);

    //search
    List<CategoryDto> searchByTitle(String keyword);


}
