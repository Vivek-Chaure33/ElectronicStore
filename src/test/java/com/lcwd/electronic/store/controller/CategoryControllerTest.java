package com.lcwd.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dto.CategoryDto;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.entity.Category;
import com.lcwd.electronic.store.service.CategoryService;
import com.lcwd.electronic.store.service.ProductServiceI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
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

    @Test
    public void createCategoryTest() throws Exception
    {
        CategoryDto dto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(categoryServiceI.create(Mockito.any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }


    private String convertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Test
    void updateCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();
        CategoryDto categoryDto=CategoryDto.builder()
                .categoryId(categoryId)
                .title("Mobiles")
                .description("Mobiles available with discounts")
                .coverImage("abc.png")
                .build();

        Mockito.when(categoryServiceI.update(Mockito.any(),Mockito.anyString())).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/categories/" +categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists());

    }

    @Test
    void getSingleCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();

        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        Mockito.when(categoryServiceI.getCategory(categoryId)).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/"+categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists());

    }

    @Test
    void deleteCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/"+categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void getAllCategoriesTest() throws Exception {
        CategoryDto categoryDto1=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Headphones")
                .description("Headphones available with good quality")
                .coverImage("xyz.png")
                .build();
        CategoryDto categoryDto2=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("LED TVS")
                .description("LED TVS available with good quality")
                .coverImage("def.png")
                .build();
        CategoryDto categoryDto3=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Electronics")
                .description("Eletronics products available with good quality")
                .coverImage("hjh.png")
                .build();
        CategoryDto categoryDto4=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("phones")
                .description("phones available with good quality")
                .coverImage("dbw.png")
                .build();

        PageableResponse<CategoryDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(categoryDto1,categoryDto2,categoryDto3,categoryDto4));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalPages(100);
        pageableResponse.setTotalElements(1000l);
        pageableResponse.setLastPage(false);

        Mockito.when(categoryServiceI.getAllCategory(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getCategoryByTitleTest() throws Exception
    {
        CategoryDto categoryDto1=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Accessories")
                .description("Accessories available with good quality")
                .coverImage("asd.png")
                .build();
        CategoryDto categoryDto2=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Laptop")
                .description("Laptop available with good quality")
                .coverImage("abc.png")
                .build();
        CategoryDto categoryDto3=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Gadgets")
                .description("Gadgets products available with good quality")
                .coverImage("pqr.png")
                .build();
        CategoryDto categoryDto4=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Mobile")
                .description("Mobile available with good quality")
                .coverImage("klm.png")
                .build();

        String title="a";

        List<CategoryDto> list = Arrays.asList(categoryDto1,categoryDto2,categoryDto4,categoryDto3);
        Mockito.when(categoryServiceI.searchByTitle(title)).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/search/"+title)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
