package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to load tours with numeric IDs from JSON and subsequently be converted into an object hierarchy
 */
public class TourLoader extends IndexedObject{
    private String name;
    private String time;
    private int busID;
    private int routeID;
    private int driverID;

    /**
     *
     * @param id
     * @param name
     * @param time
     * @param routeID
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TourLoader(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("time") String time,
                      @JsonProperty("busID") Integer busID, @JsonProperty("routeID") Integer routeID, @JsonProperty("driverID") Integer driverID) {
        super(id);
        this.name = name;
        this.time = time;
        this.busID = busID;
        this.routeID = routeID;
        this.driverID = driverID;
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
    public Integer getBusID(){
        return busID;
    }

    /**
     *
     * @return
     */
    public Integer getRouteID(){
        return routeID;
    }
    
    /**
    *
    * @return
    */
   public Integer getDriverID(){
       return driverID;
   }

    /**
     * @return
     */
    public String toString() {
        return "route " + getID() + " " + getName();
    }

}

