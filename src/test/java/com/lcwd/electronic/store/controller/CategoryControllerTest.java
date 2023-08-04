package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.entity.Category;
import com.lcwd.electronic.store.service.CategoryService;
import com.lcwd.electronic.store.service.ProductServiceI;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@SpringBootTest
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryServiceI;

    @MockBean
    private ProductServiceI productServiceI;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    private Category category;

        @BeforeEach
        public void init(){
            String categoryId1 = UUID.randomUUID().toString();
            category= Category.builder()
                    .categoryId(categoryId1)
                    .title("Laptops")
                    .description("Laptops available with discount")
                    .coverImage("abc.png")
                    .build();
        }




}
