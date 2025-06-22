package com.recipeapp.planner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipeapp.planner.api.ApiResponse;
import com.recipeapp.planner.domain.dto.UserRequestDto;
import com.recipeapp.planner.domain.dto.UserResponseDto;
import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {

        ApiResponse<List<UserResponseDto>> response = ApiResponse.success(
                userService.getAllUsers()
                        .stream()
                        .map(user -> objectMapper.convertValue(user, UserResponseDto.class))
                        .toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@RequestBody UserRequestDto userRequestDto) {
        UserEntity userEntity = userService.createUser(userRequestDto.username());
        ApiResponse<UserResponseDto> response = ApiResponse.success(objectMapper.convertValue(userEntity, UserResponseDto.class));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable String id) {
        UserEntity userEntity = userService.getUserById(UUID.fromString(id));
        ApiResponse<UserResponseDto> response = ApiResponse.success(objectMapper.convertValue(userEntity, UserResponseDto.class));

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable String id, @RequestBody UserRequestDto userRequestDto) {
        UserEntity userEntity = userService.updateUser(UUID.fromString(id), userRequestDto.username());
        ApiResponse<UserResponseDto> response = ApiResponse.success(objectMapper.convertValue(userEntity, UserResponseDto.class));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> deleteUser(@PathVariable String id) {
        userService.deleteUser(UUID.fromString(id));
        ApiResponse<UserResponseDto> response = ApiResponse.success(null);

        return ResponseEntity.ok(response);
    }
}
