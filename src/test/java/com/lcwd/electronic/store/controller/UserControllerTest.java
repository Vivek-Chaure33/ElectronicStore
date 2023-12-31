package com.lcwd.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.UserDto;
import com.lcwd.electronic.store.entity.User;
import com.lcwd.electronic.store.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper mapper;

    private User user;

    @BeforeEach
    public void init(){
        user = User.builder()
                .name("Vivek")
                .email("vivekchaure@gmail.com")
                .about("this is testing create method")
                .gender("male")
                .password("vivek")
                .imageName("vivek")
                .build();
    }

    @Test
    public void createUserTest() throws Exception {

        UserDto dto = mapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        //actual request for url
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }

    private String convertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Test
    public void updateUserTest() throws Exception
    {
        String userId = "123";
        UserDto dto = this.mapper.map(user, UserDto.class);

        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/users/"+userId)
                                //.header(HttpHeaders.AUTHORIZATION,"Bearer Token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void deleteUserTest() throws Exception
    {
        String userId = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/"+userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserTest() throws Exception {
        String userId = UUID.randomUUID().toString();

        UserDto userDto = mapper.map(user, UserDto.class);

        Mockito.when(userService.getUser(userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" +userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());

    }

    @Test
    void getUserByEmail() throws Exception {
        String email="vivekchaure@gmail.com";

        UserDto userDto = mapper.map(user, UserDto.class);

        Mockito.when(userService.getUserByEmail(email)).thenReturn(userDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/email/" +email)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").exists());


    }
    @Test
    public void searchUserTest() throws Exception {

        UserDto userDto1 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Samar")
                .email("samar@gmail.com").password("samar123").about("developer").imageName("abc.png").build();
        UserDto userDto2 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Veeraj")
                .email("veeraj@gmail.com").password("veeraj123").about("developer").imageName("xyz.png").build();

        UserDto userDto3 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Sandip")
                .email("sandip@gmail.com").password("sandip123").about("developer").imageName("def.png").build();

        UserDto userDto4 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Kumar")
                .email("kumar@gmail.com").password("kumar123").about("developer").imageName("wds.png").build();


        List<UserDto> dtoList = Arrays.asList(userDto1, userDto2, userDto3, userDto4);

        String keyword="a";

        Mockito.when(userService.searchUser(keyword)).thenReturn(dtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search/"+keyword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());



    }

    @Test
    void getAllUsersTest() throws Exception {

        UserDto userDto1 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Samar")
                .email("samar@gmail.com").password("samar123").about("developer").imageName("abc.png").build();
        UserDto userDto2 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Veeraj")
                .email("veeraj@gmail.com").password("veeraj123").about("developer").imageName("xyz.png").build();

        UserDto userDto3 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Sandip")
                .email("sandip@gmail.com").password("sandip123").about("developer").imageName("def.png").build();

        UserDto userDto4 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Kumar")
                .email("kumar@gmail.com").password("kumar123").about("developer").imageName("wds.png").build();


        PageableResponse<UserDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(userDto1,userDto2,userDto3,userDto4));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalElements(1000l);
        pageableResponse.setTotalPages(100);
        pageableResponse.setLastPage(false);

        Mockito.when(userService.getAllUsers(Mockito.anyInt(),
                Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());



    }




}
