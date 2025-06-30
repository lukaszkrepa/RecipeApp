package com.recipeapp.planner.repositories;

import com.recipeapp.planner.domain.entities.RecipeIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredientEntity, UUID> {

    List<RecipeIngredientEntity> findAllByRecipe_RecipeId(UUID recipeId);
}
