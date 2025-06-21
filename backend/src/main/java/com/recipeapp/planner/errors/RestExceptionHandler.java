package com.recipeapp.planner.errors;

import com.recipeapp.planner.api.ApiError;
import com.recipeapp.planner.api.ApiResponse;
import com.recipeapp.planner.errors.user.DuplicateUsernameException;
import com.recipeapp.planner.errors.user.InvalidUserInputException;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadJson(HttpMessageNotReadableException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .success(false)
                .timestamp(Instant.now())
                .error(ApiError.builder()
                        .code("400")
                        .message("Invalid input")
                        .details(List.of(ex.getMessage() == null ? "Unknown error" : ex.getMessage()))
                        .build())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .success(false)
                .timestamp(Instant.now())
                .error(ApiError.builder()
                        .code("404")
                        .message("User not found")
                        .details(List.of(ex.getMessage() == null ? "Unknown error" : ex.getMessage()))
                        .build())
                .build();
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(InvalidUserInputException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidUserInput(InvalidUserInputException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .success(false)
                .timestamp(Instant.now())
                .error(ApiError.builder()
                        .code("400")
                        .message("Invalid input")
                        .details(List.of(ex.getMessage() == null ? "Unknown error" : ex.getMessage()))
                        .build())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateUsername(DuplicateUsernameException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .success(false)
                .timestamp(Instant.now())
                .error(ApiError.builder()
                        .code("400")
                        .message("User with this username already exists")
                        .details(List.of(ex.getMessage() == null ? "Unknown error" : ex.getMessage()))
                        .build())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .success(false)
                .timestamp(Instant.now())
                .error(ApiError.builder()
                        .code("500")
                        .message("Internal server error")
                        .details(List.of(ex.getMessage() == null ? "Unknown error" : ex.getMessage()))
                        .build())
                .build();
        return ResponseEntity.internalServerError().body(response);
    }
}
