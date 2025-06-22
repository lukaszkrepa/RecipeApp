package com.recipeapp.planner.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Represents a generic API response wrapper.
 *
 * @param <T> the type of the response payload
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * Indicates if the request was successful.
     */
    private boolean success;

    /**
     * Timestamp when the response was generated (ISO-8601).
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp;

    /**
     * The response payload, present when success is true.
     */
    private T data;

    /**
     * Error information, present when success is false.
     */
    private ApiError error;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(Instant.now())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String code,
                                           String message,
                                           List<String> details) {
        return ApiResponse.<T>builder()
                .success(false)
                .timestamp(Instant.now())
                .data(null)
                .error(ApiError.builder()
                        .code(code)
                        .message(message)
                        .details(details)
                        .build())
                .build();
    }

    public static <T> ApiResponse<T> error(String code,
                                           String message,
                                           String detail) {
        return error(code, message,
                List.of(detail == null ? "Unknown error" : detail));
    }

}