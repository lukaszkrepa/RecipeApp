package com.recipeapp.planner.errors;

import com.recipeapp.planner.api.ApiResponse;
import com.recipeapp.planner.errors.user.DuplicateUsernameException;
import com.recipeapp.planner.errors.user.InvalidUserInputException;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadJson(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("400", "Invalid input", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(ApiResponse.error("404", "User not found", ex.getMessage()));
    }

    @ExceptionHandler(InvalidUserInputException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidUserInput(InvalidUserInputException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("400", "Invalid input", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateUsername(DuplicateUsernameException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("400", "User with this username already exists", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.error("500", "Internal server error", ex.getMessage()));
    }
}
