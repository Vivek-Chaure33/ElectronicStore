package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dto.*;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.UserService;
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
import javax.validation.OverridesAttribute;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    //logger
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    /**
     * @author VivekChaure
     * @param userDto
     * @return
     */

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){

        logger.info("Intiating request to create user:{}",userDto.getName());

        UserDto createdUserDto = userService.createUser(userDto);

        logger.info("Completed reqest of create user:{}",userDto.getName());

        return new ResponseEntity<>(createdUserDto , HttpStatus.CREATED);

    }


    /**
     * @author VivekChaure
     * @param userId
     * @param userDto
     * @return
     */

    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") String userId ,
            @Valid @RequestBody UserDto userDto
    ) {

        logger.info("Initiating request to update user:{}",userId);

        UserDto updatedUserDto = userService.updateUser(userDto, userId);

        logger.info("Complete request of update user:{}",userId);

        return new ResponseEntity<>(updatedUserDto , HttpStatus.CREATED);

    }

    /**
     * @author VivekChaure
     * @param userId
     * @return
     */

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId){

        logger.info("Initiating request to delete user:{}",userId);

        userService.deleteUser(userId);

        logger.info("complete request of delete user:{}",userId);

        ApiResponseMessage message = ApiResponseMessage.builder()
                .message(ApiConstant.USER_DELETE)
                .success(true)
                .status(HttpStatus.OK).build();

        return new ResponseEntity<>(message, HttpStatus.OK);

    }


    /**
     * @author VivekChaure
     * @return
     */

    //get all user
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize" ,defaultValue = "10" ,required = false) int pageSize,
            @RequestParam(value = "sortBy" , defaultValue = "name" , required = false) String sortBy,
            @RequestParam(value = "sortDir" , defaultValue = "asc" ,required = false) String sortDir
    ){

        logger.info("Initiating request to get all user");
        PageableResponse<UserDto> allUsersDtos = userService.getAllUsers(pageNumber,pageSize,sortBy , sortDir);
        logger.info("Compelete request of get all user");
        return new ResponseEntity<>(allUsersDtos , HttpStatus.OK);
    }

    /**
     * @author VivekChaure
     * @param userId
     * @return
     */

    //get single user
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId){

        logger.info("Initiating request to get user::{}",userId);
        UserDto singleUserDto = userService.getUser(userId);
        logger.info("Complete request to get user:{}",userId);
        return new ResponseEntity<>(singleUserDto , HttpStatus.OK);
    }

    /**
     * @author VivekChaure
     * @param email
     * @return
     */

    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email){

        logger.info("Initiating request to get user:{}",email);
        UserDto userByEmail = userService.getUserByEmail(email);
        logger.info("Complete request of get user:{}",email);
        return new ResponseEntity<>(userByEmail , HttpStatus.OK);
    }

    /**
     * @author VivekChaure
     * @param keywords
     * @return
     */

    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keywords") String keywords){

        logger.info("Initiating request to search user:{}",keywords);
        List<UserDto> dtoList = userService.searchUser(keywords);
        logger.info("Complete request of search user:{}",keywords);
        return new ResponseEntity<>(dtoList,HttpStatus.OK);

    }

    //upload user image
    @PostMapping("/image/{userId}")
    ResponseEntity<ImageResponse> uploadImage(@RequestParam("userImage")MultipartFile image , @PathVariable String userId) throws IOException {

        logger.info("Initiating request to upload user image:{}",userId);
        String imageName = fileService.uploadFile(image, imageUploadPath);
        UserDto userDto = userService.getUser(userId);
        userDto.setImageName(imageName);
        userService.updateUser(userDto,userId);
        ImageResponse response = ImageResponse.builder().imageName(imageName)
                .success(true)
                .message(ApiConstant.IMAGE_UPLOAD)
                .status(HttpStatus.CREATED).build();
        logger.info("Completed request for upload user image:{}",userId);

        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    //serve user image
    @GetMapping("image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        logger.info("Initialize the request to serve user image:{}",userId);
        UserDto user = userService.getUser(userId);
        logger.info("User image name:{}",user.getName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("Completed the request to serve user image:{}",userId);

    }


}
