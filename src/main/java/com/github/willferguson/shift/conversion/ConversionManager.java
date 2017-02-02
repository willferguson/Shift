package com.github.willferguson.shift.conversion;

import com.github.willferguson.shift.Converter;

import java.util.List;

/**
 * Created by will on 14/03/2016.
 */
public interface ConversionManager<V, T> {

    void addConverter(V fromVersion, V toVersion, Converter<T> converter);
    List<Converter<T>> findConversionChain(V fromVersion, V toVersion);
}
