package mk.seavus.demoprojcet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mk.seavus.demoprojcet.entity.User;
import mk.seavus.demoprojcet.exception.UserException;
import mk.seavus.demoprojcet.mapper.UserResponseMapper;
import mk.seavus.demoprojcet.service.IUserService;
import mk.seavus.model.UserDto;
import mk.seavus.model.UserResponseDto;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests {

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    UserController userController;

    @Mock
    IUserService userService;

    UserDto userDto1 = new UserDto(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
    UserDto userDto2 = new UserDto(2L, "cd", "ccc", "ddd", "test@gmail.com", "123", "456", 2);
    User user = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<UserDto> users = new ArrayList<>(Arrays.asList(userDto1, userDto2));

        Mockito.when(userService.getUsers()).thenReturn(users);
//        Mockito.doNothing().when(userService).createUser(any(UserDto.class));
//        Mockito.doThrow(new RuntimeException()).when(employeeDAO).delete(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username", Matchers.is("cd")));
    }

    @Test
    public void testGetUserById() throws Exception {
        Mockito.when(userService.getUserById(userDto1.getId())).thenReturn(userDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("ab")));
    }

    @Test
    public void testGetUserByName() throws Exception {
        Mockito.when(userService.getUserByName(userDto1.getUsername())).thenReturn(userDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/username/ab")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    public void testCreateUser() throws Exception {
        Mockito.when(userService.createUser(userDto1)).thenReturn(any(UserResponseDto.class)).thenReturn(any(UserResponseDto.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(userDto1));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    public void testUpadateUser() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(userService).updateUser(any(UserDto.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(userDto1));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    public void testUpdateUser_NullId() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(userDto1));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException() instanceof UserException))
                .andExpect(result -> Assert.assertEquals("Id must exist in User object", result.getResolvedException().getMessage()));
    }

    @Test
    public void testUpdateUser_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(userService).updateUser(any(UserDto.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(userDto1));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> Assert.assertEquals("User with id 1 does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(5L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> Assert.assertEquals("User with id 1 does not exist", result.getResolvedException()));

    }
}
