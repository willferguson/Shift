package com.github.willferguson.shift;

import com.github.willferguson.shift.conversion.ConversionManager;
import com.github.willferguson.shift.exceptions.CannotConvertException;

import java.util.List;
import java.util.function.Function;

/**
 * Simple API to convert between model versions.
 * When a new version of the model is released, rather than updating all the data in the DB
 * write a converter to convert between versions, and use that to "upgrade" the data on use.
 *
 * Suitable for converting between Client / Server boundaries. Eg Frontend / Backend, Application / Database etc.
 *
 * TODO - What about downgrading! :)
 * TODO - Make all methods fluent.
 *
 * @param <T> The data type to be converted
 * @param <V> The data type of the version. Usually String / Integer / Double
 */
public class VersionManagerImpl<V, T> implements VersionManager<V, T> {

    //TODO Having a single function for all versions means it can get out of hand
    private Function<T, V> versionDetectionFunction;
    private ConversionManager<V, T> conversionManager;
    private V latest;

    public VersionManagerImpl(ConversionManager<V, T> conversionManager) {
        this.conversionManager = conversionManager;
    }

    @Override
    public void addConverter(V fromVersion, V toVersion, Converter<T> converter) {
        conversionManager.addConverter(fromVersion, toVersion, converter);
    }

    @Override
    public void addConverter(V fromVersion, V toVersion, Converter<T> converter, V latest) {
        this.latest = latest;
        conversionManager.addConverter(fromVersion, toVersion, converter);
    }

    @Override
    public void addVersionDetectionFunction(Function<T, V> versionDetectionFunction) {
        this.versionDetectionFunction = versionDetectionFunction;
    }

    @Override
    public T convert(V fromVersion, V toVersion, T t) {
        T convertedT = t;
        for (Converter<T> converter : findConversionPath(fromVersion, toVersion)) {
            convertedT = converter.convert(convertedT);
        }
        return convertedT;
    }


    @Override
    public T convert(V toVersion, T t) {
        V fromVersion = versionDetectionFunction.apply(t);
        return convert(fromVersion, toVersion, t);
    }

    @Override
    public T convertToLatest(T t) {
        if (getLatest() == null) {
            throw new CannotConvertException("No latest value set");
        }
        V fromVersion = versionDetectionFunction.apply(t);
        return convert(fromVersion, getLatest(), t);
    }

    protected V getLatest() {
        return latest;
    }

    private List<Converter<T>> findConversionPath(V fromVersion, V toVersion) {
        return conversionManager.findConversionChain(fromVersion, toVersion);
    }


}
