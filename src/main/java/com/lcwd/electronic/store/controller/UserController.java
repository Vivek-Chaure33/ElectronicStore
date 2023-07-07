package com.lcwd.electronic.store.controller;

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

        logger.info("Creating user");

        UserDto createdUserDto = userService.createUser(userDto);

        logger.info("user created:"+createdUserDto);

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

        logger.info("Updating user");

        UserDto updatedUserDto = userService.updateUser(userDto, userId);

        logger.info("User Updated:"+updatedUserDto);

        return new ResponseEntity<>(updatedUserDto , HttpStatus.CREATED);

    }

    /**
     * @author VivekChaure
     * @param userId
     * @return
     */

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId){

        logger.info("deleting user");

        userService.deleteUser(userId);

        logger.info("User deleted");

        return new ResponseEntity<>("user deleted success" , HttpStatus.OK);

    }


    /**
     * @author VivekChaure
     * @return
     */

    //get all user
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){

        logger.info("getting all user");

        List<UserDto> allUsersDtos = userService.getAllUsers();

        logger.info("all user:" +allUsersDtos);

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

        logger.info("getting user by id");

        UserDto singleUserDto = userService.getUser(userId);

        logger.info("user is:" +singleUserDto);

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

        logger.info("getting user by email");

        UserDto userByEmail = userService.getUserByEmail(email);

        logger.info("user is:"+userByEmail);

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

        logger.info("searching user by keyword");
        return new ResponseEntity<>(userService.searchUser(keywords),HttpStatus.OK);
    }




}
