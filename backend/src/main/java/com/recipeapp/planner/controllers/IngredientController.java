package com.recipeapp.planner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipeapp.planner.api.ApiResponse;
import com.recipeapp.planner.domain.dto.IngredientRequestDto;
import com.recipeapp.planner.domain.dto.IngredientResponseDto;
import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.errors.ingredient.InvalidIngredientInputException;
import com.recipeapp.planner.services.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.recipeapp.planner.utils.UuidUtils.parse;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<IngredientResponseDto>>> getAllIngredients() {
        List<IngredientResponseDto> response = ingredientService.getAllIngredients()
                .stream()
                .map(x -> objectMapper.convertValue(x, IngredientResponseDto.class))
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));

    }
    @PostMapping()
    public ResponseEntity<ApiResponse<IngredientResponseDto>> createIngredient(@RequestBody IngredientRequestDto ingredientRequestDto) {
        IngredientResponseDto response = objectMapper.convertValue(ingredientService.createIngredient(ingredientRequestDto.name(), ingredientRequestDto.unit()), IngredientResponseDto.class);
        return ResponseEntity.status(201).body(ApiResponse.success(response));

    }
    @GetMapping("/{ingredientId}")
    public  ResponseEntity<ApiResponse<IngredientResponseDto>> getIngredientById(@PathVariable String ingredientId) {
        final UUID uuid = parse(ingredientId, () -> new InvalidIngredientInputException("Ingredient id is invalid"));

        IngredientResponseDto response = objectMapper.convertValue(ingredientService.getIngredientById(uuid), IngredientResponseDto.class);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    @PatchMapping("/{ingredientId}")
    public ResponseEntity<ApiResponse<IngredientResponseDto>> updateIngredient(
            @PathVariable String ingredientId,
            @RequestBody IngredientRequestDto dto) {

        final UUID uuid = parse(ingredientId, () -> new InvalidIngredientInputException("Ingredient id is invalid"));


        boolean hasName = dto.name() != null;
        boolean hasUnit = dto.unit() != null;

        IngredientEntity updated;
        if (hasName && hasUnit) {
            updated = ingredientService.updateIngredient(uuid, dto.name(), dto.unit());
        }
        else if (hasName) {
            updated = ingredientService.updateIngredientName(uuid, dto.name());
        }
        else if (hasUnit) {
            updated = ingredientService.updateIngredientUnit(uuid, dto.unit());
        }
        else {
            throw new InvalidIngredientInputException("At least one of name or unit must be provided");
        }

        IngredientResponseDto body = objectMapper
                .convertValue(updated, IngredientResponseDto.class);

        return ResponseEntity
                .ok(ApiResponse.success(body));
    }

    @DeleteMapping("/{ingredientId}")
    public  ResponseEntity<ApiResponse<IngredientResponseDto>> deleteIngredient(@PathVariable String ingredientId) {
        final UUID uuid = parse(ingredientId, () -> new InvalidIngredientInputException("Ingredient id is invalid"));

        ingredientService.deleteIngredient(uuid);
        return ResponseEntity.status(204).body(ApiResponse.success(null));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<List<IngredientResponseDto>>> getIngredientsByName(@PathVariable String name) {
        List<IngredientResponseDto> response = ingredientService.getIngredientsByName(name)
                .stream()
                .map(x -> objectMapper.convertValue(x, IngredientResponseDto.class))
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }


}
