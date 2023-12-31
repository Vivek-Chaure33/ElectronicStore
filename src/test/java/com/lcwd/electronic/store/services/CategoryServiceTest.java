package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dto.CategoryDto;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.entity.Category;
import com.lcwd.electronic.store.repository.CategoryRepository;
import com.lcwd.electronic.store.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper mapper;

    private CategoryDto categoryDto;
    private Category category, category1;
    @BeforeEach
    public void init(){
         category = Category.builder()
                .title("Laptop")
                .coverImage("laptop.png")
                .description("This category contains laptops")
                .build();

         category1=Category.builder()
                 .title("Mobile")
                 .description("This category contains mobile")
                 .coverImage("mobile.jpeg")
                 .build();

         categoryDto=CategoryDto.builder()
                 .title("Mobile")
                 .coverImage("mobile.jpg")
                 .description("this is all about mobile")
                 .build();
    }
    @Test
    public void createTest(){
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        CategoryDto category1= categoryService.create(mapper.map(category, CategoryDto.class));
        System.out.println(category1.getTitle());
        Assertions.assertNotNull(category1);
        Assertions.assertEquals("Laptop",category1.getTitle(),"Title not matched !!");

    }

    @Test
    public void updateCategoryTest(){
        String catgoryId="asdjas";
        Mockito.when(categoryRepository.findById(catgoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        CategoryDto updateCategory = categoryService.update(categoryDto,catgoryId);
        System.out.println(updateCategory.getTitle());
        Assertions.assertNotNull(updateCategory);
        Assertions.assertEquals("Mobile",updateCategory.getTitle(),"Title not matched !!");
    }

    @Test
    public void deleteCategoryTest(){
        String categoryId= UUID.randomUUID().toString();
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        categoryService.delete(categoryId);
        Mockito.verify(categoryRepository,Mockito.times(1)).delete(category);

    }

    @Test
    public void getAllCategoryTest(){

        List<Category> categoryList = Arrays.asList(category1, category);
        Page<Category> page = new PageImpl<>(categoryList);
        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<CategoryDto> allCategory=categoryService.getAllCategory(1,2,"name","asc");
        Assertions.assertEquals(2,allCategory.getContent().size());

    }

    @Test
    public void getCategoryByIdTest()
    {
        String categoryId = "d7da596d-2a1c-4292-be7d-4f3a71e65f60";

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));

        // actual call of service method
        CategoryDto categoryDto = categoryService.getCategory(categoryId);

        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(category.getTitle(),categoryDto.getTitle(),"Title not matched");
    }

    @Test
    public void searchByTitleTest(){
        String keyword = "Laptop";
        Mockito.when(categoryRepository.findByTitle(keyword)).thenReturn(Arrays.asList(category,category1));
        List<CategoryDto> categoryDtos = categoryService.searchByTitle(keyword);
        Assertions.assertEquals(2,categoryDtos.size(),"size not matched");
    }


}
