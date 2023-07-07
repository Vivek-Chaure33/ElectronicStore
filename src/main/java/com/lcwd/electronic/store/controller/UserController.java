package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dto.ApiConstant;
import com.lcwd.electronic.store.dto.ApiResponseMessage;
import com.lcwd.electronic.store.dto.UserDto;
import com.lcwd.electronic.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    //logger
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    /**
     * @author VivekChaure
     * @param userDto
     * @return
     */

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){

        logger.info("Intiating request to create user");

        UserDto createdUserDto = userService.createUser(userDto);

        logger.info("Completed reqest of create user");

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
            @RequestBody UserDto userDto
    ) {

        logger.info("Initiating request to update user");

        UserDto updatedUserDto = userService.updateUser(userDto, userId);

        logger.info("Complete request of update user ");

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

        logger.info("Initiating request to delete user");

        userService.deleteUser(userId);

        logger.info("complete request of delete user");

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
    public ResponseEntity<List<UserDto>> getAllUsers(){

        logger.info("Initiating request to get all user");

        List<UserDto> allUsersDtos = userService.getAllUsers();

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

        logger.info("Initiating request to get user");

        UserDto singleUserDto = userService.getUser(userId);

        logger.info("Complete request to get user");

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

        logger.info("Initiating request to get user");

        UserDto userByEmail = userService.getUserByEmail(email);

        logger.info("Complete request of get user");

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

        logger.info("Initiating request to get user");

        List<UserDto> dtoList = userService.searchUser(keywords);

        logger.info("Complete request of get user");

        return new ResponseEntity<>(dtoList,HttpStatus.OK);

    }




}
