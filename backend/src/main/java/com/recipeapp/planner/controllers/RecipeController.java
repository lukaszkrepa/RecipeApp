package com.recipeapp.planner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipeapp.planner.api.ApiResponse;
import com.recipeapp.planner.domain.dto.RecipeRequestDto;
import com.recipeapp.planner.domain.dto.RecipeResponseDto;
import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.errors.recipe.InvalidRecipeInputException;
import com.recipeapp.planner.services.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.recipeapp.planner.utils.UuidUtils.parse;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<RecipeResponseDto>>> getRecipes() {
        List<RecipeResponseDto> response = recipeService.getAllRecipes()
                .stream()
                .map(RecipeResponseDto::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeResponseDto>> getRecipeById(@PathVariable String recipeId) {
        final UUID uuid = parse(recipeId, () -> new InvalidRecipeInputException("Recipe id is invalid"));

        RecipeResponseDto response = RecipeResponseDto.from(recipeService.getRecipeById(uuid));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<RecipeResponseDto>> createRecipe(@RequestBody RecipeRequestDto recipe) {
        RecipeResponseDto response = RecipeResponseDto.from(recipeService.createRecipe(recipe));
        return ResponseEntity.status(201).body(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RecipeResponseDto>>> getRecipesByUserId(@PathVariable String userId) {
        final UUID uuid = parse(userId, () -> new InvalidRecipeInputException("User id is invalid"));

        List<RecipeResponseDto> response = recipeService.getAllRecipesByUserId(uuid)
                .stream()
                .map(RecipeResponseDto::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeResponseDto>> updateRecipe(@PathVariable String recipeId,
                                                                       @RequestBody(required = false) RecipeRequestDto recipe) // Setting the required to false to throw a custom error on the service
    {
        final UUID uuid = parse(recipeId, () -> new InvalidRecipeInputException("Recipe id is invalid"));

        RecipeResponseDto response = RecipeResponseDto.from(recipeService.updateRecipe(uuid, recipe));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("{recipeId}")
    public ResponseEntity<ApiResponse<Void>> deleteRecipe(@PathVariable String recipeId) {
        final UUID uuid = parse(recipeId, () -> new InvalidRecipeInputException("Recipe id is invalid"));

        recipeService.deleteRecipe(uuid);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


}
