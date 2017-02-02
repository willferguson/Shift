package com.github.willferguson.shift.conversion;

import com.github.willferguson.shift.Converter;
import com.github.willferguson.shift.path.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by will on 14/03/2016.
 */
public class ConversionManagerImpl<V, T> implements ConversionManager<V, T> {

    private static final Logger logger = LoggerFactory.getLogger(ConversionManagerImpl.class);
    private final PathFinder<V> pathFinder;

    public ConversionManagerImpl(PathFinder<V> pathFinder) {
        this.pathFinder = pathFinder;
    }

    /**
     * fromVersion -> toVersion -> Converter
     * 1 -> 2 -> Converter
     * 1 -> 3 -> Converter
     * 2 -> 3 -> Converter
     */
    Map<V, Map<V, Converter<T>>> conversionSpace = new HashMap<>();


    @Override
    public void addConverter(V fromVersion, V toVersion, Converter<T> converter) {
        if (!conversionSpace.containsKey(fromVersion)) {
            conversionSpace.put(fromVersion, new HashMap<>());
        }
        conversionSpace.get(fromVersion).put(toVersion, converter);
    }

    @Override
    public List<Converter<T>> findConversionChain(V fromVersion, V toVersion) {
        Map<V, Set<V>> possibleRoutes = convertConversionSpace(conversionSpace);
        List<Map.Entry<V, V>> path = pathFinder.findPath(fromVersion, toVersion, possibleRoutes);
        return constructConverterList(path);
    }

    private Map<V, Set<V>> convertConversionSpace(Map<V, Map<V, Converter<T>>> conversionSpace) {
        Map<V, Set<V>> possibleRoutes = new HashMap<>(conversionSpace.size());
        conversionSpace.forEach((v, map) -> {
            possibleRoutes.put(v, map.keySet());
        });
        return possibleRoutes;
    }

    private List<Converter<T>>  constructConverterList(List<Map.Entry<V, V>> conversionPath) {
        List<Converter<T>> converters = new ArrayList<>(conversionPath.size());
        conversionPath
                .stream()
                .forEach(entry -> {
                    converters.add(conversionSpace.get(entry.getKey()).get(entry.getValue()));
                });
        return converters;
    }


}
