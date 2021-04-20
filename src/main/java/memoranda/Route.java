package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Route object representing a route in the MTB scheduling system.
 * Routes contain an ordered list of Nodes
 *
 * @author Brian Pape, Chris Boveda
 * @version 2021-04-10
 */
public class Route extends IndexedObject {
    private String name;
    private LinkedList<Node> route;
    private ArrayList<Tour> tours;


    /**
     * Create a new route.
     *
     * @param id id for this route
     */
    public Route(int id) {
        super(id);
        route = new LinkedList<>();
        tours = new ArrayList<>();
    }


    /**
     * constructor for json deserialization.
     *
     * @param nodeColl collection of nodes used to associate node IDs in deserialized Route object
     * @param newRoute a RouteLoader class with data to populate this object
     * @throws IndexOutOfBoundsException if provided ID is not unique
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Route(NodeColl nodeColl, RouteLoader newRoute) throws IndexOutOfBoundsException {
        this(newRoute.getID());

        Node n;
        this.name = newRoute.getName();
        for (Integer i : newRoute.getNodes()) {
            n = nodeColl.get(i);
            if (n == null) {
                throw new IndexOutOfBoundsException("Node index " + i + " not found");
            } else {
                route.add(n);
            }
        }
    }


    /**
     * Add an ordered list of nodes to this route.
     *
     * @param nodes list of nodes to add
     */
    public void addNodes(List<Node> nodes) {
        for (Node n : nodes) {
            addNode(n);
        }
    }


    /**
     * Add a new node to the end of this route.
     *
     * @param node node to add
     */
    public void addNode(Node node) {
        route.add(node);
    }


    /**
     * Returns the length of this route (in km!).
     *
     * @return length in km
     */
    public Double length() {
        double len = 0.0;

        // a route of 0 or 1 nodes has a length of 0
        if (route.size() < 2) {
            return 0.0;
        } else {
            boolean load = true;
            Node tmp = null;
            for (Node n : route) {
                if (load) {
                    tmp = n;
                    load = false;
                } else {
                    len += (tmp.distanceTo(n));
                    tmp = n;
                }

            }
        }
        return len;
    }


    /**
     * returns the amount of time in hours required to traverse this route based upon the given
     * speed in km/h.
     *
     * @param speed speed in km/h
     * @return duration in hours (e.g. 61 minutes would be 1.01666... )
     */
    public Double duration(double speed) {
        return length() / speed;
    }


    /**
     * Sets the given node as the start of the route. May be used in route generation interface.
     *
     * @param n the node to set as the new start
     * @return false if the route did not contain the node
     */
    public boolean setStart(Node n) {
        if (route.removeFirstOccurrence(n)) {
            route.addFirst(n);
            return true;
        }
        return false;
    }


    /**
     * Sets the route to the given route.
     *
     * @param newRoute the new route to set
     */
    public void setRoute(LinkedList<Node> newRoute) {
        this.route = newRoute;
    }


    /**
     * standard getter for name.
     *
     * @return name of this node
     */
    public String getName() {
        return name;
    }


    /**
     * Standard setter for name
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * returns an ordered list of the nodes in the route.
     *
     * @return list of nodes in the route
     */
    @JsonIgnore
    public LinkedList<Node> getRoute() {
        return route;
    }
    
    /**
     * Adds a Tour to this Route.
     * 
     * @param tour The Tour to add
     */
    public void addTour(Tour tour) {
        tours.add(tour);
    }
    
    /**
     * Gets the list of Tours associated with this Route.
     * 
     * @return The list of Tours
     */
    @JsonIgnore
    public ArrayList<Tour> getTours() {
        return tours;
    }
    
    /**
     * Returns an ordered list of only the IDs of the nodes in this route for json serialization.
     *
     * @return ordered list of integer IDs of nodes in this route
     */
    @JsonProperty
    public LinkedList<Integer> getNodeIDs() {
        LinkedList<Integer> li = new LinkedList<>();
        for (Node n : route) {
            li.add(n.getID());
        }
        return li;
    }


    /**
     * standard toString().
     *
     * @return string repr of obj
     */
    @Override
    public String toString() {
        return getID() + ":" + "'" + name + "'" + route;
    }

}
