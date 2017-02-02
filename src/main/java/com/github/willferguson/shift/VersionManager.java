package com.github.willferguson.shift;

import java.util.function.Function;


/**
 * Created by will on 10/03/16.
 */
public interface VersionManager<V, T> {

    /**
     * Adds a converter to convert between the given versions.
     * @param fromVersion
     * @param toVersion
     * @param converter
     */
    void addConverter(V fromVersion, V toVersion, Converter<T> converter);

    /**
     * Adds a converter, updating the latest (highest) version number
     * @param fromVersion
     * @param toVersion
     * @param converter
     * @param latest
     */
    void addConverter(V fromVersion, V toVersion, Converter<T> converter, V latest);

    void addVersionDetectionFunction(Function<T, V> detectionFunction);

    /**
     * Converts defining the from / to version
     * @param fromVersion
     * @param toVersion
     * @param t
     * @return
     */
    T convert(V fromVersion, V toVersion, T t);

    /**
     * Convert to a specific version, auto detecting the entity version
     * @param toVersion
     * @param t
     * @return
     */
    T convert(V toVersion, T t);

    /**
     * Convert to the latest version, auto detecting both the entity version and the latest
     * @param t
     * @return
     */
    T convertToLatest(T t);


}
