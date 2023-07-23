package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dto.ApiConstant;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.UserDto;
import com.lcwd.electronic.store.entity.User;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repository.UserRepository;
import com.lcwd.electronic.store.service.UserService;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${user.profile.image.path}")
    private String imagePath;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        logger.info("sending request to repository to create user:{}",userDto.getName());
        //generate unique id in String format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //dto -> Entity
        User user = mapper.map(userDto , User.class);
        User saveUser = userRepository.save(user);
        //Entity -> dto
        UserDto newDto = mapper.map(saveUser , UserDto.class);
        logger.info("sending response to controller for successfully create user:{}",userDto.getName());

        return newDto;
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        logger.info("sending request to repository for update user:{}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.USER_NOT_FOUND+userId));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        User updatedUser = userRepository.save(user);
        UserDto updatedDto = mapper.map(updatedUser , UserDto.class);
        logger.info("sending response to controller for successfully update user:{}",userId);

        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {

        logger.info("sending request to repository for delete user:{}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.USER_NOT_FOUND+userId));
        //user image delete
        //images/users/abc.png
        String fullPath = imagePath + File.separator+ user.getImageName();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("No such file found in folder");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //delete user
        userRepository.delete(user);
        logger.info("sending response to controller for successfully delete user:{}",userId);

    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber , int pageSize , String sortBy , String sortDir) {

        logger.info("sending request to repository for get all user");
        Sort sort =sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize ,sort);
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);
        logger.info("sending request to controller for successfully get all user");

        return pageableResponse;
    }

    @Override
    public UserDto getUser(String userId) {

        logger.info("sending request to repository for get user:{}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.USER_NOT_FOUND+userId));
        UserDto singleUser = mapper.map(user , UserDto.class);
        logger.info("sending response to controller for get user:{}",userId);

        return singleUser;
    }

    @Override
    public UserDto getUserByEmail(String email) {

        logger.info("sending request to repository for get user by email:{}",email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(ApiConstant.USER_NOT_FOUND+email));
        logger.info("sending response to controller for get user by email:{}",email);

        return mapper.map(user , UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {

        logger.info("sending request to repository for search user by keyword:{}",keyword);
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map((user) -> mapper.map(user , UserDto.class)).collect(Collectors.toList());
        logger.info("sending request to controller for successfully search user by keyword:{}",keyword);

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
