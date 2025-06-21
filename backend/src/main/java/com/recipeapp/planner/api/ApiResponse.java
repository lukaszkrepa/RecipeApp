package com.recipeapp.planner.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
}