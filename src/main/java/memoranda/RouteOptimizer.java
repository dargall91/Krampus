package main.java.memoranda;

import java.util.LinkedList;

/**
 * RouteOptimizer provides methods for the optimization of a route's path, both with a given start point
 * and a selected start point
 *
 * @author Chris Boveda
 * @version 2021-04-10
 */
public class RouteOptimizer {
    private final Route route;

    public RouteOptimizer(Route route) {
        this.route = route;
    }

    /**
     * Optimizes the path between the nodes of the routes using a nearest-neighbor/greedy algorithm.
     * Behavior with duplicate nodes is uncertain. Will select first element of a set of nodes that
     * are equidistant from the current. Only factor that affects the determination of the route is the
     * starting node.
     */
    public void optimize() {
        LinkedList<Node> routeCopy = new LinkedList<>(route.getRoute());
        LinkedList<Node> newRoute = new LinkedList<>();

        newRoute.add(routeCopy.removeFirst());
        while(!routeCopy.isEmpty()) {
            double thisDistance, minDistance = Double.MAX_VALUE;
            int minDistIndex = 0;
            for (Node n : routeCopy) {
                thisDistance = newRoute.getLast().distanceTo(n);
                if (thisDistance < minDistance) {
                    minDistance = thisDistance;
                    minDistIndex = routeCopy.indexOf(n);
                }
            }
            newRoute.add(routeCopy.remove(minDistIndex));
        }
        route.setRoute(newRoute);
    }


    /**
     * Utilizes the optimize method with the addition of start point selection.
     */
    public void optimizeWithStart() {
        optimize();
        Route bestRoute = route;
        for (int i = 1; i < bestRoute.getRoute().size(); i++) {
            Route trial = new Route(i);
            trial.setRoute(bestRoute.getRoute());
            trial.setStart(trial.getRoute().get(i));
            new RouteOptimizer(trial).optimize();
            if (trial.length() < bestRoute.length()) {
                bestRoute = trial;
            }
        }
        route.setRoute(bestRoute.getRoute());
    }
}
