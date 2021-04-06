package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

/**
 * Used to load routes with numeric IDs from JSON and subsequently be converted into an
 * object hierarchy.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class RouteLoader extends IndexedObject {
    private String name;
    private LinkedList<Integer> nodeIDs;

    /**
     * constructor for json deserialization.
     *
     * @param id      id for route
     * @param name    name of route
     * @param nodeIDs IDs of nodes (ordered list) associated with route
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RouteLoader(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("nodeIDs") LinkedList<Integer> nodeIDs) {
        super(id);
        this.name = name;
        this.nodeIDs = nodeIDs;
    }

    /**
     * name getter.
     *
     * @return name of route
     */
    public String getName() {
        return name;
    }

    /**
     * json serialization routine.
     *
     * @return ordered list of integer nodes IDs in route
     */
    public LinkedList<Integer> getNodes() {
        return nodeIDs;
    }

    /**
     * standard toString().
     *
     * @return string repr of obj
     */
    public String toString() {
        return "route " + getID() + " " + getName();
    }

}
