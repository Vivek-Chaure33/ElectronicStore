package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dto.*;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.ProductServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceI productServiceI;

    @Autowired
    private FileService fileService;

    @Value("${product.profile.image.path}")
    private String imagePath;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {

        ProductDto newProductDto = productServiceI.createProduct(productDto);
        return new ResponseEntity<>(newProductDto, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId) {

        ProductDto updatedProductDto = productServiceI.updateProduct(productDto, productId);

        return new ResponseEntity<>(updatedProductDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {

        productServiceI.deleteProduct(productId);
        ApiResponseMessage response = ApiResponseMessage.builder().message(ApiConstant.PRODUCT_DELETE)
                .success(true)
                .status(HttpStatus.OK).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {

        ProductDto product = productServiceI.getProduct(productId);

        return new ResponseEntity<>(product, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {

        PageableResponse<ProductDto> allProducts = productServiceI.getAllProducts(pageSize, pageNumber, sortBy, sortDir);

        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allLive = productServiceI.getAllLive(pageSize, pageNumber, sortBy, sortDir);

        return new ResponseEntity<>(allLive, HttpStatus.OK);
    }


    @GetMapping("search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @PathVariable String query,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {

        PageableResponse<ProductDto> productDtoPageableResponse = productServiceI.searchByTitle(query, pageSize, pageNumber, sortBy, sortDir);

        return new ResponseEntity<>(productDtoPageableResponse, HttpStatus.OK);
    }

    @PostMapping("upload/image/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam(value = "productImage") MultipartFile productImage, @PathVariable String productId) throws IOException {

        ProductDto product = productServiceI.getProduct(productId);
        String newProductImage = fileService.uploadFile(productImage, imagePath);
        product.setProductImage(newProductImage);
        productServiceI.updateProduct(product,productId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(newProductImage)
                .success(true)
                .status(HttpStatus.CREATED)
                .message(ApiConstant.IMAGE_UPLOAD).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void serveImage(HttpServletResponse response,@PathVariable String productId) throws IOException {

        ProductDto product = productServiceI.getProduct(productId);


        InputStream resource = fileService.getResource(imagePath, product.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }

}
