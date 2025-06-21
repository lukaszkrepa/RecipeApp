package com.recipeapp.planner.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Represents a generic API error structure.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    /**
     * A machine-readable error code.
     */
    private String code;

    /**
     * A human-readable error message.
     */
    private String message;

    /**
     * Additional error details, if any.
     */
    private List<String> details;
}

