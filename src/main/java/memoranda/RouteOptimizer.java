package main.java.memoranda;

import java.util.LinkedList;

public class RouteOptimizer {
    private Thread thread;
    private Route route;

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
        thread = new Thread(() -> {
                LinkedList<Node> routeCopy = new LinkedList<>(route.getRoute());
                LinkedList<Node> newRoute = new LinkedList<>();

                newRoute.add(routeCopy.removeFirst());
                while (!routeCopy.isEmpty()) {
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

            //notify listeners
        });
        thread.start();
    }

    /**
     * returns the optimizer thread for purposes of synchronization
     *
     * @return the thread of the optimizer
     */
    public Thread getThread() {
        return thread;
    }
}
