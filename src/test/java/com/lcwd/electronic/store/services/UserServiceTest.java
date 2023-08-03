package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.UserDto;
import com.lcwd.electronic.store.entity.User;
import com.lcwd.electronic.store.repository.UserRepository;
import com.lcwd.electronic.store.service.UserService;
import com.lcwd.electronic.store.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    User user;
    String userId;

    @BeforeEach
    public void init(){
        user = User.builder()
                .name("Vivek")
                .email("vivekchaure@gmail.com")
                .about("this is testing create method")
                .gender("male")
                .imageName("vivek")
                .build();
        userId="abc";

    }
//    @Test
//    public void createUserTest(){
//        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
//        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
//        UserDto user1=userService.createUser(this.mapper.map(user,UserDto.class));
//        System.out.println(user1.getName());
//        Assertions.assertNotNull(user1);
//        Assertions.assertEquals("Vivek",user1.getName());
//    }
    @Test
    public void deleteUserTest(){
        String userId = UUID.randomUUID().toString();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userServiceImpl.deleteUser(userId);
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }

    @Test
    public void getAllUserTest(){

        User user1 = User.builder()
                .name("Rahul")
                .email("rahul@gmail.com")
                .about("This is testing create method")
                .gender("male")
                .imageName("rahul.png")
                .password("rahul")
                .build();
        User user2 = User.builder()
                .name("Panjak")
                .email("pankaj@gmail.com")
                .about("This is testing create method")
                .gender("male")
                .imageName("pankaj.png")
                .password("pankaj")
                .build();

        List<User> userList = Arrays.asList(user1, user2);
        Page<User> page = new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        Sort sort=Sort.by("name").ascending();
        Pageable pageable= PageRequest.of(1,2,sort);
        PageableResponse<UserDto> allUser = userServiceImpl.getAllUsers(1,2,"name","asc");
        Assertions.assertEquals(2,allUser.getContent().size());
    }

}
