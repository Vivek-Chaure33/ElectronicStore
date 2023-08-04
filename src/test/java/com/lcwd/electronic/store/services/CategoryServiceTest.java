package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.entity.Category;
import com.lcwd.electronic.store.repository.CategoryRepository;
import com.lcwd.electronic.store.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper mapper;

    private Category category;
    @BeforeEach
    public void init(){
         category = Category.builder()
                .title("Laptop")
                .coverImage("laptop.png")
                .description("This category contains laptops")
                .build();
    }

}
