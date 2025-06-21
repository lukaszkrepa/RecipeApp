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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get all users with one user")
    @Order(2)
    void getAllUsersWithOneUser() throws Exception {
        UserEntity user = UserEntity.builder().userId(UUID.randomUUID()).username("Test").build();
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].username").value("Test"))
                .andExpect(jsonPath("$.data[0].userId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get all users with two users")
    @Order(3)
    void getAllUsersWithTwoUsers() throws Exception {
        UserEntity user1 = UserEntity.builder().userId(UUID.randomUUID()).username("Test").build();
        UserEntity user2 = UserEntity.builder().userId(UUID.randomUUID()).username("Test2").build();
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].username").value("Test"))
                .andExpect(jsonPath("$.data[1].username").value("Test2"))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get user by id")
    @Order(4)
    void getUserById() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserEntity user = UserEntity.builder().userId(uuid).username("Test").build();
        when(userService.getUserById(uuid)).thenReturn(user);

        mockMvc.perform(get("/users/" + uuid))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.username").value("Test"))
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get user by id not found")
    @Order(5)
    void getUserByIdNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userService.getUserById(uuid)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get("/users/" + uuid))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error.message").value("User not found"));
    }

    @Test
    @DisplayName("create user")
    @Order(6)
    void createUser() throws Exception {
        UserRequestDto req = new UserRequestDto("Test");
        UserEntity user = UserEntity.builder().userId(UUID.randomUUID()).username("Test").build();
        when(userService.createUser("Test")).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.username").value("Test"))
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("create user with empty body")
    @Order(7)
    void createUserWithEmptyBody() throws Exception {
        when(userService.createUser("")).thenThrow(InvalidUserInputException.class);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error.message").value("Invalid input"));
    }

    @Test
    @DisplayName("create user with existing name")
    @Order(9)
    void createUserWithExistingName() throws Exception {
        UserRequestDto req = new UserRequestDto("Test");
        when(userService.createUser("Test")).thenThrow(DuplicateUsernameException.class);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error.message").value("User with this username already exists"));
    }

    @Test
    @DisplayName("update user")
    @Order(10)
    void updateUser() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserRequestDto req = new UserRequestDto("Test");
        UserEntity user = UserEntity.builder().userId(uuid).username("Test").build();
        when(userService.updateUser(uuid, "Test")).thenReturn(user);

        mockMvc.perform(patch("/users/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.username").value("Test"))
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.error.message").doesNotExist());
    }

    @Test
    @DisplayName("update user with invalid input")
    @Order(11)
    void updateUserWithInvalidInput() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userService.updateUser(uuid, "")).thenThrow(InvalidUserInputException.class);
        mockMvc.perform(patch("/users/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("") )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error.message").value("Invalid input"));
    }

    @Test
    @DisplayName("update user with non-existing id")
    @Order(13)
    void updateUserWithNonExistingId() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserRequestDto req = new UserRequestDto("Test");
        when(userService.updateUser(uuid, "Test")).thenThrow(UserNotFoundException.class);
        mockMvc.perform(patch("/users/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error.message").value("User not found"));
    }

    @Test
    @DisplayName("delete user")
    @Order(15)
    void deleteUser() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(userService).deleteUser(uuid);

        mockMvc.perform(delete("/users/" + uuid))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error.message").doesNotExist());
    }

    @Test
    @DisplayName("delete user with non-existing id")
    @Order(16)
    void deleteUserWithNonExistingId() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(UserNotFoundException.class).when(userService).deleteUser(uuid);

        mockMvc.perform(delete("/users/" + uuid))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error.message").value("User not found"));
    }
}
