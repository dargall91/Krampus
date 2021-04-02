package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

/**
 * Used to load routes with numeric IDs from JSON and subsequently be converted into an object hierarchy
 */
public class RouteLoader extends IndexedObject {
    private String name;
    private LinkedList<Integer> nodeIDs;

    /**
     * @param id
     * @param name
     * @param nodeIDs
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RouteLoader(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("nodeIDs") LinkedList<Integer> nodeIDs) {
        super(id);
        this.name = name;
        this.nodeIDs = nodeIDs;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public LinkedList<Integer> getNodes() {
        return nodeIDs;
    }

    /**
     * @return
     */
    public String toString() {
        return "route " + getID() + " " + getName();
    }

}
