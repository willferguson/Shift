package com.github.willferguson.shift;

import com.github.willferguson.shift.exceptions.NoValidConversionPath;
import com.github.willferguson.shift.path.PathFinder;
import com.github.willferguson.shift.path.SimplePathFinder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by will on 14/03/2016.
 */
public class PathFinderTest {

    private static final Logger logger = LoggerFactory.getLogger(PathFinderTest.class);
    PathFinder<Integer> pathFinder = new SimplePathFinder<>();
    Map<Integer, Set<Integer>> possibleRoutes;

    @Before
    public void setup() {

        /*
         * Some routes
         * 1 -> 2, 3, 4
         * 2 -> 3
         * 3 -> {}
         * 4 -> 5
         * 5 -> 6
         * 6 -> 7, 8, 9
         * 7 - {}
         * //intentionally no 8
         * 9 -> 10, 11
         */
        possibleRoutes = new HashMap<>();
        possibleRoutes.put(1, new HashSet<>(Arrays.asList(2, 3, 4)));
        possibleRoutes.put(2, new HashSet<>(Collections.singletonList(3)));
        possibleRoutes.put(3, new HashSet<>());
        possibleRoutes.put(4, new HashSet<>(Collections.singletonList(5)));
        possibleRoutes.put(5, new HashSet<>(Collections.singletonList(6)));
        possibleRoutes.put(6, new HashSet<>(Arrays.asList(7, 8, 9)));
        possibleRoutes.put(7, new HashSet<>());
        possibleRoutes.put(9, new HashSet<>(Arrays.asList(10, 11)));

    }
    @Test
    public void testValidRoute() {
        Integer from = 1;
        Integer to = 11;
        List<Map.Entry<Integer, Integer>> path = pathFinder.findPath(from, to, possibleRoutes);
        isValid(from, to, path, possibleRoutes);
    }

    @Test(expected = NoValidConversionPath.class)
    public void testInvalidRoute() {
        Integer from = 1;
        Integer to = 14;
        pathFinder.findPath(from, to, possibleRoutes);
    }
    /*
     * Validates the path is correct
     */
    private void isValid(Integer from, Integer to, List<Map.Entry<Integer, Integer>> path, Map<Integer, Set<Integer>> possibleRoutes) {
        //First validate that the beginning and end are valid
        if (!(path.get(0).getKey().equals(from) && path.get(path.size() -1).getValue().equals(to))) {
            throw new AssertionError("Path does not begin with " + from + " and end with " + to);
        }
        Integer startingPoint = from;
        for (Map.Entry<Integer, Integer> singleRoute : path) {
            //Ensure that the destination of the previous route, is the starting point of this.
            if (!startingPoint.equals(singleRoute.getKey())) {
                throw new AssertionError("Routes not contiguous. Destination of previous route " + startingPoint + " does not equal the starting point of " + singleRoute.getKey());
            }
            //Ensure that the mapping is valid
            boolean routeExists = possibleRoutes.get(singleRoute.getKey()).contains(singleRoute.getValue());
            if (!routeExists) {
                throw new AssertionError("This route does not exist in the possible routes");
            }
            //Update the starting point.
            startingPoint = singleRoute.getValue();
        }
    }
}
