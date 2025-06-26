package com.recipeapp.planner.controllers;

import com.recipeapp.planner.api.ApiResponse;
import com.recipeapp.planner.domain.dto.UserRequestDto;
import com.recipeapp.planner.domain.dto.UserResponseDto;
import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.errors.user.InvalidUserInputException;
import com.recipeapp.planner.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.recipeapp.planner.utils.UuidUtils.parse;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {

        ApiResponse<List<UserResponseDto>> response = ApiResponse.success(
                userService.getAllUsers()
                        .stream()
                        .map(UserResponseDto::from)
                        .toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@RequestBody UserRequestDto userRequestDto) {
        UserEntity userEntity = userService.createUser(userRequestDto.username());
        ApiResponse<UserResponseDto> response = ApiResponse.success(UserResponseDto.from(userEntity));

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable String userId) {
        final UUID uuid = parse(userId, () -> new InvalidUserInputException("User id is invalid"));

        UserEntity userEntity = userService.getUserById(uuid);
        ApiResponse<UserResponseDto> response = ApiResponse.success(UserResponseDto.from(userEntity));

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable String userId, @RequestBody UserRequestDto userRequestDto) {
        final UUID uuid = parse(userId, () -> new InvalidUserInputException("User id is invalid"));

        UserEntity userEntity = userService.updateUser(uuid, userRequestDto.username());
        ApiResponse<UserResponseDto> response = ApiResponse.success(UserResponseDto.from(userEntity));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> deleteUser(@PathVariable String userId) {
        final UUID uuid = parse(userId, () -> new InvalidUserInputException("User id is invalid"));

        userService.deleteUser(uuid);
        ApiResponse<UserResponseDto> response = ApiResponse.success(null);

        return ResponseEntity.status(204).body(response);
    }
}
