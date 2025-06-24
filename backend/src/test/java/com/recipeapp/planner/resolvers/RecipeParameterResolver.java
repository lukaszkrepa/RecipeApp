package com.recipeapp.planner.resolvers;

import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.domain.entities.UserEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.*;

public class RecipeParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return Objects.equals(parameter.getParameterizedType().getTypeName(), "java.util.Map<java.lang.String, com.recipeapp.planner.domain.entities.RecipeEntity>");
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        UserEntity testUser = UserEntity.builder().username("TestUser").build();

        return Map.of(
                "recipe1", RecipeEntity.builder()
                        .name("Recipe 1")
                        .description("Description 1")
                        .instructions(new ArrayList<>(List.of("Step 1", "Step 2")))
                        .createdBy(testUser)
                        .build(),

                "recipe2", RecipeEntity.builder()
                        .name("Recipe 2")
                        .description("Description 2")
                        .instructions(new ArrayList<>(List.of("Step A", "Step B")))
                        .createdBy(testUser)
                        .build(),

                "publicRecipe", RecipeEntity.builder()
                        .name("Public Recipe")
                        .description("Shared with everyone")
                        .instructions(new ArrayList<>(List.of("Step X")))
                        .createdBy(null)
                        .build()
        );
    }
}
