package com.recipeapp.planner.domain.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecipeEntity> recipes = new ArrayList<>();


    public UserEntity(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public void addRecipe(RecipeEntity recipe) {
        recipes.add(recipe);
        recipe.setCreatedBy(this);
    }

    public void removeRecipe(RecipeEntity recipe) {
        recipes.remove(recipe);
        recipe.setCreatedBy(null);
    }
}
