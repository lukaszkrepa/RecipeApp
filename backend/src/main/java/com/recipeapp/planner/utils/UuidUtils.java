package com.recipeapp.planner.utils;

import java.util.UUID;
import java.util.function.Supplier;

public final class UuidUtils {
    private UuidUtils() {}

    /**
     * Try to parse a UUID, or throw the supplied exception if it fails.
     *
     * @param source            the raw String
     * @param exceptionSupplier produces the RuntimeException to throw on parse failure
     */
    public static <E extends RuntimeException> UUID parse(
            String source,
            Supplier<E> exceptionSupplier
    ) {
        try {
            return UUID.fromString(source);
        } catch (IllegalArgumentException ex) {
            throw exceptionSupplier.get();
        }
    }
}