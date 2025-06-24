package com.recipeapp.planner.repositories;

import com.recipeapp.planner.domain.entities.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<RecipeEntity, UUID> {
    List<RecipeEntity> findAllByCreatedBy_UserId(UUID userId);
}
