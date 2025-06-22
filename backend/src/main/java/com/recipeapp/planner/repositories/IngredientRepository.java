package com.recipeapp.planner.repositories;

import com.recipeapp.planner.domain.entities.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IngredientRepository extends JpaRepository<IngredientEntity, UUID> {
    List<IngredientEntity> findAllByName(String name);
}
