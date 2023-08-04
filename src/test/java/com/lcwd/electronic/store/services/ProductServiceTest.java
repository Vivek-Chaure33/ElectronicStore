package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dto.PageableResponse;
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
import org.springframework.data.domain.*;

import java.util.*;

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
                .title("Samsung")
                .price(2000)
                .live(true)
                .stock(true)
                .productImage("samsung.png")
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

    @Test
    public void updateProductTest()
    {
        String productId = "hasdfvjyehf";

        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product1));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product1);

        ProductDto updatedProduct = productServiceI.updateProduct(productDto, productId);
        System.out.println(updatedProduct.getTitle());

        Assertions.assertNotNull(updatedProduct);
        Assertions.assertEquals("Samsung",updatedProduct.getTitle(),"product name is not Valid");
    }

    @Test
    public void deleteProductTest()
    {
        String productId = UUID.randomUUID().toString();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product1));

        productServiceI.deleteProduct(productId);
        Mockito.verify(productRepository,Mockito.times(1)).delete(product1);
    }
    @Test
    public void getAllProductTest(){
        List<Product> productList = Arrays.asList(product1,product2);
        Page<Product> page = new PageImpl<>(productList);
        Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        //Sort sort = Sort.by("name").ascending();
        //Pageable pageable = PageRequest.of(1,2,sort);
        PageableResponse<ProductDto> allProduct = productServiceI.getAllProducts(1,2,"name","asc");
        Assertions.assertEquals(2,allProduct.getContent().size());
    }

    @Test
    public void getProductTest(){
        String productId="d7da596d-2a1c-4292-be7d-4f3a71e65f60";
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product1));

        // actual call of service method
        ProductDto productDto = productServiceI.getProduct(productId);

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(product1.getTitle(),productDto.getTitle(),"Title not matched");
    }
    @Test
    public void searchProductTest(){
        String subTitle = "Laptops";
        List<Product>productList = Arrays.asList(product1,product2);

        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(2,2,sort);
        Page<Product>page = new PageImpl<>(productList);
        Mockito.when(productRepository.findByTitleContaining(pageable,subTitle)).thenReturn(page);

        PageableResponse<ProductDto> allProduct = productServiceI.searchByTitle("Laptops",2,2,"name","asc");
        Assertions.assertEquals(2,allProduct.getTotalElements());
    }
    @Test
    public void getAllLiveTest()
    {

        List<Product> productList = Arrays.asList(product1,product2);
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(1,2,sort);
        Page<Product> page = new PageImpl<>(productList);
        Mockito.when(productRepository.findByLiveTrue(pageable)).thenReturn(page);

        PageableResponse<ProductDto> allProduct = productServiceI.getAllLive(1,2,"name","asc");
        Assertions.assertEquals(2,allProduct.getContent().size());
    }


}
