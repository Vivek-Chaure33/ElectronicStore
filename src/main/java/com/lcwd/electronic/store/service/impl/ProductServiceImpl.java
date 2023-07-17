package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dto.ApiConstant;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.ProductDto;
import com.lcwd.electronic.store.entity.Category;
import com.lcwd.electronic.store.entity.Product;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repository.CategoryRepository;
import com.lcwd.electronic.store.repository.ProductRepository;
import com.lcwd.electronic.store.service.ProductServiceI;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductServiceI {

    @Autowired
    private ProductRepository productRepo;

    private Logger logger= LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;

    @Value("${product.profile.image.path}")
    private String imagePath;
    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product = mapper.map(productDto, Product.class);
        //generate random product id
        String productId = UUID.randomUUID().toString();

        product.setAddedDate(new Date());

        product.setProdructId(productId);
        productRepo.save(product);
        ProductDto savedProductDto = mapper.map(product, ProductDto.class);

        return savedProductDto;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId)
    {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.PRODUCT_NOT_FOUND + productId));

        product.setDescription(productDto.getDescription());
        product.setTitle(productDto.getTitle());
        product.setProductImage(productDto.getProductImage());
        product.setQuantity(productDto.getQuantity());
        product.setAddedDate(product.getAddedDate());
        product.setStock(productDto.isStock());
        product.setLive(productDto.isLive());

        Product updatedProduct = productRepo.save(product);

        ProductDto updatedProductDto = mapper.map(updatedProduct, ProductDto.class);
        return updatedProductDto;
    }

    @Override
    public void deleteProduct(String productId) {

        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.PRODUCT_NOT_FOUND + productId));
        String fullProductImagePath = imagePath + File.separator + product.getProductImage();

        try {
            Path path = Path.of(fullProductImagePath);
            Files.delete(path);
        }catch(NoSuchFileException ex){
            ex.printStackTrace();

        } catch(IOException e) {
            e.printStackTrace();
        }

        productRepo.delete(product);

    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageSize, int pageNumber, String sortBy, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepo.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);

        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageSize, int pageNumber, String sortBy, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> byLiveTrue = productRepo.findByLiveTrue(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(byLiveTrue, ProductDto.class);

        return pageableResponse;
    }

    @Override
    public ProductDto getProduct(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.PRODUCT_NOT_FOUND + productId));
        ProductDto newProductDto = mapper.map(product, ProductDto.class);
        return newProductDto;
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subString ,int pageSize, int pageNumber, String sortBy, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepo.findByTitleContaining(pageable, subString);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);

        return pageableResponse;
    }


    public ProductDto createWithCategory(@RequestBody ProductDto productDto , @PathVariable String categoryId)
    {


        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.CATEGORY_NOT_FOUND + categoryId));
        logger.info("category object:{}",category.getTitle());
        Product product = mapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProdructId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product saveProduct = productRepo.save(product);

        return mapper.map(saveProduct,ProductDto.class);
    }

}
