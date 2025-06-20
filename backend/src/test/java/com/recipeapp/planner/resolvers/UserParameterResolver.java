package com.recipeapp.planner.resolvers;

import com.recipeapp.planner.domain.entities.UserEntity;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

public class UserParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return Objects.equals(parameter.getParameterizedType().getTypeName(), "java.util.Map<java.lang.String, com.recipeapp.planner.domain.entities.UserEntity>");
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return Map.of(
                "user1", UserEntity.builder().username("TestUser").build(),
                "user2", UserEntity.builder().username("TestUser2").build(),
                "nullUser", UserEntity.builder().build()
        );
    }
}
