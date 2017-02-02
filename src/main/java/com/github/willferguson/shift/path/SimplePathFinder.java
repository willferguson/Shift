package com.github.willferguson.shift.path;

import com.github.willferguson.shift.exceptions.NoValidConversionPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by will on 16/03/2016.
 */
public class SimplePathFinder<V> implements PathFinder<V> {

    private static final Logger logger = LoggerFactory.getLogger(SimplePathFinder.class);

    @Override
    public List<Map.Entry<V, V>> findPath(V fromVersion, V toVersion, Map<V, Set<V>> possibleRoutes) throws NoValidConversionPath {
        Deque<Map.Entry<V, V>> conversionChain = new ArrayDeque<>();
        Deque<V> visiting = new ArrayDeque<>();
        boolean found = findConversionChain(fromVersion, toVersion, conversionChain, visiting, possibleRoutes);
        if (!found) {
            logger.warn("No route found between {}, and {}", fromVersion, toVersion);
            throw new NoValidConversionPath("Could not find a route");
        }
        else {
            List<Map.Entry<V, V>> pathList = new ArrayList<>(conversionChain);
            //Because we've been using Deque like a stack, we're in the wrong order.
            Collections.reverse(pathList);
            logger.info("Found path {} between {} and {}", pathList, fromVersion, toVersion);
            return pathList;
        }
    }

    private boolean findConversionChain(V fromVersion, V toVersion, Deque<Map.Entry<V, V>> conversionChain, Deque<V> visiting, Map<V, Set<V>> possibleRoutes) {
        logger.info("Searching routes from {}", fromVersion);
        visiting.push(fromVersion);
        Set<V> toSeries = possibleRoutes.get(fromVersion);
        if (toSeries == null) {
            logger.info("{} has no converters, moving on", fromVersion);
            return false;
        }
        //if we have found our toVersion, add and return
        if (toSeries.contains(toVersion)) {
            logger.info("Found our destination version {}", toVersion);
            conversionChain.push(new AbstractMap.SimpleEntry<>(fromVersion, toVersion));
            return true;
        }
        else {
            for (V v : toSeries) {
                logger.info("Found converter from {} to {}", fromVersion, v);
                conversionChain.push(new AbstractMap.SimpleEntry<>(fromVersion, v));
                boolean found = findConversionChain(v, toVersion, conversionChain, visiting, possibleRoutes);
                if (found) {
                    return true;
                }
                else {
                    logger.info("Version {} has no routes to {}", v, toVersion);
                    conversionChain.pop();
                    visiting.pop();
                }
            }
            return false;
        }
    }

}
