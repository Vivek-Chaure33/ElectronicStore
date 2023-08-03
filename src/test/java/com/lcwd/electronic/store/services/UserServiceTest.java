package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.entity.User;
import com.lcwd.electronic.store.repository.UserRepository;
import com.lcwd.electronic.store.service.UserService;
import com.lcwd.electronic.store.service.impl.UserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    User user;
    String userId;

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

}
