package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * holds a node (i.e. a bus stop)
 */
public class Node extends IndexedObject {
    private String name;
    private Coordinate coords;

    /**
     *
     * @param id
     * @param name
     * @param lat
     * @param lon
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Node(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("lat") Double lat,
                @JsonProperty("lon") Double lon){
        super(id);
        this.name=name;
        coords=new Coordinate(lat, lon);
    }


    /**
     * return distance to another node
     * @param n
     * @return
     */
    public Double distanceTo(Node n) throws NullPointerException{
        return this.coords.distanceTo(n.getCoords());
    }
    /**
     * Standard getter for latitude
     * @return
     */
    public Double getLat(){
        return coords.getLat();
    }

    /**
     * Standard getter for longitude
     * @return
     */
    public Double getLon(){
        return coords.getLon();
    }

    @JsonIgnore
    public Coordinate getCoords(){
        return coords;
    }
    /**
     * standard getter for name
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * standard toString()
     * @return
     */
    @Override
    public String toString(){
        return getId()+":"+"'"+name+"'@"+ coords;
    }

    // file name will be .nodes in home directory
    /*
    The input file for the system will be in json format with specific key/value pairs that define the following, defined as “nodes”:

- unique numeric ID
- textual description
- latitude
- longitude

A data class and collection class will be defined to hold, aggregate, and access the data.

AC1: JSON file contains all required data fields

AC2: Data are exposed in a manner accessible to the application through an object with a defined interface.
     */
}
