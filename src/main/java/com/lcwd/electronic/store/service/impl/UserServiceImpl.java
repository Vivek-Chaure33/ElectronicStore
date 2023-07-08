package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dto.ApiConstant;
import com.lcwd.electronic.store.dto.UserDto;
import com.lcwd.electronic.store.entity.User;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.repository.UserRepository;
import com.lcwd.electronic.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        logger.info("fetching user detail");

        //generate unique id in String format
        String userId = UUID.randomUUID().toString();

        userDto.setUserId(userId);

        //dto -> Entity
        User user = mapper.map(userDto , User.class);
        User saveUser = userRepository.save(user);
        //Entity -> dto
        UserDto newDto = mapper.map(saveUser , UserDto.class);

        logger.info("completed request for saving the user");

        return newDto;
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        logger.info("fetching user from userId");

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.USER_NOT_FOUND+userId));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepository.save(user);

        UserDto updatedDto = mapper.map(updatedUser , UserDto.class);

        logger.info("Complete request for update user");

        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {

        logger.info("Fetching user details");

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.USER_NOT_FOUND+userId));
        //delete user
        userRepository.delete(user);

        logger.info("complete request for delete user");

    }

    @Override
    public List<UserDto> getAllUsers() {

        logger.info("Fetching all users detail");

        List<User> allUsers = userRepository.findAll();

        //convert all list object into dto
        List<UserDto> dtoList = allUsers.stream().map((user) -> mapper.map(user , UserDto.class)).collect(Collectors.toList());

        logger.info("complete request for get all users");

        return dtoList;
    }

    @Override
    public UserDto getUser(String userId) {

        logger.info("Fetching user details ");

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.USER_NOT_FOUND+userId));

        UserDto singleUser = mapper.map(user , UserDto.class);

        logger.info("complete request for get user");

        return singleUser;
    }

    @Override
    public UserDto getUserByEmail(String email) {

        logger.info("Fetching user details by email");

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.USER_NOT_FOUND+email));

        logger.info("complete request for get user by email");

        return mapper.map(user , UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {

        logger.info("Fetching users detail");

        List<User> users = userRepository.findByNameContaining(keyword);

        List<UserDto> dtoList = users.stream().map((user) -> mapper.map(user , UserDto.class)).collect(Collectors.toList());

        logger.info("complete request for search user");

        return dtoList;
    }

/*    private UserDto entityToDto(User user) {
        UserDto userDto = UserDto.builder().userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .about(user.getAbout())
                .password(user.getPassword())
                .gender(user.getGender())
                .imageName(user.getImageName()).build();

        return mapper.map(user , UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
        User user = User.builder().userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .about(userDto.getAbout())
                .gender(userDto.getGender()).
                imageName(userDto.getImageName()).build();

        return mapper.map(userDto , User.class);
    }
*/

}
