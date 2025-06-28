package com.recipeapp.planner.resolvers;

import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.enums.Unit;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

public class IngredientParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return Objects.equals(parameter.getParameterizedType().getTypeName(),
                "java.util.Map<java.lang.String, com.recipeapp.planner.domain.entities.IngredientEntity>");
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return Map.of(
                "salt", IngredientEntity.builder().name("Salt").unit(Unit.GRAMS).build(),
                "pepper", IngredientEntity.builder().name("Pepper").unit(Unit.GRAMS).build(),
                "oliveOil", IngredientEntity.builder().name("Olive Oil").unit(Unit.ML).build()
        );
    }
}
