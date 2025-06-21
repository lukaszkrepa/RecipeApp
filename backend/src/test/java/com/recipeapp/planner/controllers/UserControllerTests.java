package com.recipeapp.planner.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipeapp.planner.domain.dto.UserRequestDto;
import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.errors.user.DuplicateUsernameException;
import com.recipeapp.planner.errors.user.InvalidUserInputException;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import com.recipeapp.planner.services.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("<= UserControllerTests =>")
@ExtendWith(MockitoExtension.class)

public class UserControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    @Test
    @DisplayName("get all users with no users")
    @Order(1)
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("get all users with one user")
    @Order(2)
    void getAllUsersWithOneUser() throws Exception {

        when(userService.getAllUsers()).thenReturn(List.of(UserEntity.builder().username("Test").build()));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(""))
                .andExpect(jsonPath("$[0].username").value("Test"))
                .andExpect(jsonPath("$[0].userId").exists())
                .andExpect(jsonPath("$[0].userId").isNotEmpty());
    }

    @Test
    @DisplayName("get all users with two users")
    @Order(3)
    void getAllUsersWithTwoUsers() throws Exception {

        when(userService.getAllUsers()).thenReturn(List.of(
                UserEntity.builder().username("Test").build(),
                UserEntity.builder().username("Test2").build()
        ));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(""))
                .andExpect(jsonPath("$[0].username").value("Test"))
                .andExpect(jsonPath("$[0].userId").exists())
                .andExpect(jsonPath("$[0].userId").isNotEmpty())
                .andExpect(jsonPath("$[1].username").value("Test2"))
                .andExpect(jsonPath("$[1].userId").exists())
                .andExpect(jsonPath("$[1].userId").isNotEmpty());
    }

    @Test
    @DisplayName("get user by id")
    @Order(4)
    void getUserById() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userService.getUserById(uuid)).thenReturn(UserEntity.builder().username("Test").build());
        mockMvc.perform(get("/users/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(""))
                .andExpect(jsonPath("$.username").value("Test"))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.userId").isNotEmpty());
    }

    @Test
    @DisplayName("get user by id not found")
    @Order(5)
    void getUserByIdNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userService.getUserById(uuid)).thenThrow(UserNotFoundException.class);
        mockMvc.perform(get("/users/" + uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("create user")
    @Order(6)
    void createUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("Test");

        when(userService.createUser("Test")).thenReturn(UserEntity.builder().username("Test").build());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Test"))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.userId").isNotEmpty());
    }

    @Test
    @DisplayName("create user with empty body")
    @Order(7)
    void createUserWithEmptyBody() throws Exception {
        when(userService.createUser("")).thenThrow(InvalidUserInputException.class);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("create user with null body")
    @Order(8)
    void createUserWithNullBody() throws Exception {
        when(userService.createUser(null)).thenThrow(InvalidUserInputException.class);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("create user with existing name")
    @Order(9)
    void createUserWithExistingName() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("Test");
        when(userService.createUser("Test")).thenThrow(DuplicateUsernameException.class);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update user")
    @Order(10)
    void updateUser() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserRequestDto userRequestDto = new UserRequestDto("Test");
        when(userService.updateUser(uuid, "Test")).thenReturn(UserEntity.builder().username("Test").build());
        mockMvc.perform(patch("/users/" + uuid)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Test"))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.userId").isNotEmpty());
    }

    @Test
    @DisplayName("update user with empty body")
    @Order(11)
    void updateUserWithEmptyBody() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userService.updateUser(uuid, "")).thenThrow(InvalidUserInputException.class);
        mockMvc.perform(patch("/users/" + uuid)
                .contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update user with null body")
    @Order(12)
    void updateUserWithNullBody() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userService.updateUser(uuid, null)).thenThrow(InvalidUserInputException.class);
        mockMvc.perform(patch("/users/" + uuid)
                .contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update user with non-existing id")
    @Order(13)
    void updateUserWithNonExistingId() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserRequestDto userRequestDto = new UserRequestDto("Test");
        when(userService.updateUser(uuid, "Test")).thenThrow(UserNotFoundException.class);
        mockMvc.perform(patch("/users/" + uuid)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("update user with null id")
    @Order(14)
    void updateUserWithNullId() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("Test");
        when(userService.updateUser(null, "Test")).thenThrow(InvalidUserInputException.class);
        mockMvc.perform(patch("/users/" + null)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("delete user")
    @Order(15)
    void deleteUser() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(userService).deleteUser(uuid);

        mockMvc.perform(delete("/users/" + uuid))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("delete user with non-existing id")
    @Order(16)
    void deleteUserWithNonExistingId() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(UserNotFoundException.class).when(userService).deleteUser(uuid);

        mockMvc.perform(delete("/users/" + uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("delete user with null id")
    @Order(17)
    void deleteUserWithNullId() throws Exception {
        doThrow(InvalidUserInputException.class).when(userService).deleteUser(null);

        mockMvc.perform(delete("/users/" + null))
                .andExpect(status().isBadRequest());
    }

}
