package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dto.*;
import com.lcwd.electronic.store.service.CategoryService;
import com.lcwd.electronic.store.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private FileService fileService;

    private Logger logger =  LoggerFactory.getLogger(CategoryController.class);

    @Value("${category.profile.image.path}")
    private String imageUploadPath;
    @Autowired
    private CategoryService categoryService;

    /**
     * @author VivekChaure
     * @param categoryDto
     * @return
     */

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {

        logger.info("Initiating request of create category");
        //call service to save object
        CategoryDto newCategoryDto = categoryService.create(categoryDto);
        logger.info("Completed request of create category ");

        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    /**
     * @author VivekChaure
     * @param categoryDto
     * @param categoryId
     * @return
     */
    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid@RequestBody CategoryDto categoryDto ,@PathVariable String categoryId){

        logger.info("Initiating request for update category");
        //call service to update object
        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        logger.info("Complete request for update category");

        return new ResponseEntity<>(updatedCategory , HttpStatus.CREATED);
    }


    /**
     * @author VivekChaure
     * @param categoryId
     * @return
     */

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){

        logger.info("Initiating requst for delete category");

        categoryService.delete(categoryId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message(ApiConstant.CATEGORY_DELETE)
                .status(HttpStatus.OK)
                .success(true).build();
        logger.info("Completed request for delete category");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * @author VivekChaure
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    //get all category
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value = "pageNumber",defaultValue = "0" ,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize ,
            @RequestParam(value = "sortBy",defaultValue ="title" ,required = false ) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc" ,required = false) String sortDir
    ){
        logger.info("Initiating request of getting all category");
        PageableResponse<CategoryDto> allCategory = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Completed request for get all category");

        return new ResponseEntity<>(allCategory ,HttpStatus.OK);
    }


    /**
     * @author VivekChaure
     * @param categoryId
     * @return
     */
    //get single category
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId){
        logger.info("Initiating request for get category");
        CategoryDto newCategoryDto = categoryService.getCategory(categoryId);
        logger.info("Completed request for get category");

        return new ResponseEntity<>(newCategoryDto,HttpStatus.OK);
    }

    /**
     * @author VivekChaure
     * @param title
     * @return
     */

    //search
    @GetMapping("/search/{title}")
    public ResponseEntity<List<CategoryDto>> getCategoryByTitle(@PathVariable String title){

        logger.info("Initiating request of search category by title");
        List<CategoryDto> categoryDtos = categoryService.searchByTitle(title);
        logger.info("Complete request of search category by title");

        return new ResponseEntity<>(categoryDtos,HttpStatus.OK);
    }

    /**
     * @author VivekChaure
     * @param file
     * @param categoryId
     * @return
     * @throws IOException
     */
    @PostMapping("/image/upload/{categoryId}")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam(value = "categoryImage")MultipartFile file,@PathVariable String categoryId) throws IOException {

        logger.info("Initiating request for upload image");

        String uploadImage = fileService.uploadFile(file, imageUploadPath);

        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        categoryDto.setCoverImage(uploadImage);
        categoryService.update(categoryDto,categoryId);
        ImageResponse response = ImageResponse.builder().imageName(uploadImage)
                .message(ApiConstant.IMAGE_UPLOAD)
                .status(HttpStatus.OK)
                .success(true).build();

        logger.info("Completed request for upload image");
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    /**
     * @author VivekChaure
     * @param response
     * @param categoryId
     * @throws IOException
     */
    @GetMapping("image/{userId}")
    public void serveCategoryImage(HttpServletResponse response , @PathVariable String categoryId) throws IOException {

        logger.info("Initiating request for serve image");
        CategoryDto category = categoryService.getCategory(categoryId);
        logger.info("User image is {}",category.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("Completed request of serve image");

    }

}
