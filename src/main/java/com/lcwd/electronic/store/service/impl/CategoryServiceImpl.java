package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dto.ApiConstant;
import com.lcwd.electronic.store.dto.CategoryDto;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.entity.Category;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repository.CategoryRepository;
import com.lcwd.electronic.store.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepo;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;
    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        logger.info("generating random id");
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        logger.info("category id:{}",categoryId);
        Category category = mapper.map(categoryDto, Category.class);
        Category newCategory = categoryRepo.save(category);
        CategoryDto newCategoryDto = mapper.map(newCategory, CategoryDto.class);

        logger.info("completed request for create()");
        return newCategoryDto;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId)
    {

        logger.info("Fetching request for update()");
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.CATEGORY_NOT_FOUND + categoryId));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getTitle());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepo.save(category);
        CategoryDto updatedDto = mapper.map(updatedCategory, CategoryDto.class);

        logger.info("Completed request for update()");
        return updatedDto;
    }

    @Override
    public void delete(String categoryId) {

        logger.info("Fetching request for delete()");
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.CATEGORY_NOT_FOUND + categoryId));

        String fullPath = imageUploadPath + File.separator + category.getCoverImage();

        try {
            Path path = Paths.get(fullPath);

            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("No such file found in folder");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        categoryRepo.delete(category);

        logger.info("Complete request for delete()");
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber , int pageSize , String sortBy,String sortDir)
    {


        logger.info("Fetching request for getAllCategory()");
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Page<Category> page = categoryRepo.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);

        logger.info("Completed request for getAllCategory()");
        return pageableResponse;
    }

    @Override
    public CategoryDto getCategory(String categoryId)
    {

        logger.info("Fetching request for getCategory");
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.CATEGORY_NOT_FOUND + categoryId));
        CategoryDto newCategoryDto = mapper.map(category, CategoryDto.class);
        logger.info("Completed request for getCategory()");
        return newCategoryDto;
    }

    @Override
    public List<CategoryDto> searchByTitle(String keyword) {

        logger.info("Fetching request for searchByTitle()");
        List<Category> allTitle = categoryRepo.findByTitle(keyword);
        List<CategoryDto> allCategoryDtos = allTitle.stream().map(category -> mapper.map(category, CategoryDto.class)).collect(Collectors.toList());
        logger.info("Completed request for searchByTitle");
        return allCategoryDtos;
    }


}
