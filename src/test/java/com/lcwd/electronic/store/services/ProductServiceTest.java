package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dto.ProductDto;
import com.lcwd.electronic.store.entity.Product;
import com.lcwd.electronic.store.repository.ProductRepository;
import com.lcwd.electronic.store.service.ProductServiceI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductServiceI productServiceI;
    @Autowired
    private ModelMapper mapper;

    private Product product1,product2;
    private ProductDto productDto;
    @BeforeEach
    public void init(){
        product1 = Product.builder()
                .addedDate(new Date())
                .title("Lenovo")
                .price(22000)
                .live(true)
                .stock(true)
                .productImage("lenovo.png")
                .description("This is best in Market")
                .quantity(5)
                .discountedPrice(5000)
                .build();

        product2 = Product.builder()
                .addedDate(new Date())
                .title("Dell")
                .price(44000)
                .live(true)
                .stock(true)
                .productImage("dell.png")
                .description("This is best in Market")
                .quantity(5)
                .discountedPrice(500)
                .build();
        productDto = ProductDto.builder()
                .addedDate(new Date())
                .title("Dell")
                .price(2000)
                .live(true)
                .stock(true)
                .productImage("dell.png")
                .description("This is best in Market")
                .quantity(2)
                .discountedPrice(5600)
                .build();

    }

    @Test
    public void createProductTest()
    {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product1);
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product1));

        ProductDto productDto = productServiceI.createProduct(mapper.map(product1, ProductDto.class));
        System.out.println(productDto.getTitle());
        Assertions.assertNotNull(productDto);
        Assertions.assertEquals("Lenovo",productDto.getTitle());
    }



}
