package com.github.willferguson.shift;

/**
 * Created by will on 10/03/16.
 */
@FunctionalInterface
public interface Converter<T> {

    T convert(T t);

}
