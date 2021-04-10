package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to load tours with numeric IDs from JSON and subsequently be converted into an object hierarchy
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class TourLoader extends IndexedObject {
    private String name;
    private String time;
    private int speed;
    private int routeID;

    /**
     * constructor for json deserialization
     *
     * @param id      id for Tour
     * @param name    name of tour
     * @param time    time of tour
     * @param busID   integer id for this tour's bus
     * @param routeID integer id for this tour's route
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TourLoader(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("time") String time,
                      @JsonProperty("routeID") Integer routeID) {
        super(id);
        this.name = name;
        this.time = time;
        this.routeID = routeID;
    }

    /**
     * name getter.
     *
     * @return name of tour
     */
    public String getName() {
        return name;
    }

    /**
     * time getter.
     *
     * @return time of route as string
     */
    public String getTime() {
        return time;
    }


    /**
     * Gets speed for this tour.
     *
     * @return speed for this tour
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * route getter.
     *
     * @return id of route as integer
     */
    public Integer getRouteID() {
        return routeID;
    }

    /**
     * standard toString()
     *
     * @return string repr of obj
     */
    public String toString() {
        return "route " + getID() + " " + getName();
    }

}

