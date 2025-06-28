package com.recipeapp.planner.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recipeapp.planner.domain.enums.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ingredientId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();

    public void addRecipeLink(RecipeIngredientEntity link) {
        recipeIngredients.add(link);
        link.setIngredient(this);
    }

    public void removeRecipeLink(RecipeIngredientEntity link) {
        recipeIngredients.remove(link);
        link.setIngredient(null);
    }

}

