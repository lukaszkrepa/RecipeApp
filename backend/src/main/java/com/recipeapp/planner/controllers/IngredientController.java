package com.recipeapp.planner.controllers;

import com.recipeapp.planner.domain.dto.IngredientRequestDto;
import com.recipeapp.planner.services.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping()
    public ResponseEntity getAllIngredients() {
        return null;
    }
    @PostMapping()
    public ResponseEntity createIngredient(@RequestBody IngredientRequestDto ingredientRequestDto) {
        return null;
    }
    @GetMapping("/{id}")
    public ResponseEntity getIngredientById(@PathVariable String id) {
        return null;
    }
    @PatchMapping("/{id}")
    public ResponseEntity updateIngredient(@PathVariable String id) {
        return null;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteIngredient(@PathVariable String id) {
        return null;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity getIngredientsByName(@PathVariable String name) {
        return null;
    }


}
