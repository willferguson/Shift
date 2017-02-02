package com.github.willferguson.shift.path;

import com.github.willferguson.shift.exceptions.NoValidConversionPath;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by will on 16/03/2016.
 */
public interface PathFinder<V> {

    /**
     * Given the possible conversion paths between versions, this path finder should return any valid
     * route between fromVersion -> toVersion.
     * @param fromVersion The starting version
     * @param toVersion The target version
     * @param possibleRoutes The set of possible paths - EG:
     *                       <pre>
     *                          1 -> {2, 3},
     *                          2 -> {3, 4, 5},
     *                          5 -> {6}
     *                       </pre>
     * @return A list of valid routes such that list[0].key == fromVersion, && list[n].value == toVersion
     * @throws NoValidConversionPath
     */
    List<Map.Entry<V, V>> findPath(V fromVersion, V toVersion, Map<V, Set<V>> possibleRoutes) throws NoValidConversionPath;
}
