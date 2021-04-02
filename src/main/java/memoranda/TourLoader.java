package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to load tours with numeric IDs from JSON and subsequently be converted into an object hierarchy
 */
public class TourLoader extends IndexedObject{
    private String name;
    private String time;
    private int routeID;

    /**
     *
     * @param id
     * @param name
     * @param time
     * @param routeID
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TourLoader(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("time") String time, @JsonProperty("routeId") Integer routeID) {
        super(id);
        this.name = name;
        this.time = time;
        this.routeID = routeID;
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
    public String getTime() {
        return time;
    }

    /**
     *
     * @return
     */
    public Integer getRouteId(){
        return routeID;
    }

    /**
     * @return
     */
    public String toString() {
        return "route " + getId() + " " + getName();
    }

}

