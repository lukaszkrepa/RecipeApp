package com.recipeapp.planner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipeapp.planner.domain.dto.UserRequestDto;
import com.recipeapp.planner.domain.dto.UserResponseDto;
import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import com.recipeapp.planner.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream().map(user -> objectMapper.convertValue(user, UserResponseDto.class)).toList());
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            UserEntity userEntity = userService.createUser(userRequestDto.username());
            return ResponseEntity.ok(objectMapper.convertValue(userEntity, UserResponseDto.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
        try {
            UserEntity userEntity = userService.getUserById(UUID.fromString(id));
            return ResponseEntity.ok(objectMapper.convertValue(userEntity, UserResponseDto.class));
        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String id, @RequestBody UserRequestDto userRequestDto) {
        try {
            UserEntity userEntity = userService.updateUser(UUID.fromString(id), userRequestDto.username());
            return ResponseEntity.ok(objectMapper.convertValue(userEntity, UserResponseDto.class));
        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(UUID.fromString(id));
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
