package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Node object representing a node ("bus stop") in the MTB scheduling system.  Nodes have coordinates and can be
 * associated with routes.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class Node extends IndexedObject {
    private String name;
    private Coordinate coords;


    /**
     * create a new Node with given id
     *
     * @param id id for node
     */
    public Node(int id){
        super(id);
    }

    /**
     * constructor for json deserialization
     *
     * @param id node id
     * @param name node name
     * @param lat node latitude
     * @param lon node longitude
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Node(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("lat") Double lat,
                @JsonProperty("lon") Double lon){
        super(id);
        this.name=name;
        coords=new Coordinate(lat, lon);
    }


    /**
     * name setter
     *
     * @param name name to set
     */
    public void setName(String name){
        this.name=name;
    }

    /**
     * coordinate setter
     * @param coords the coordinate to assign to this node
     */
    public void setCoords(Coordinate coords){
        this.coords=coords;
    }


    /**
     * return distance to another node
     *
     * @param n node to calculate distance to
     * @return distance to provided node
     */
    public Double distanceTo(Node n) throws NullPointerException{
        return this.coords.distanceTo(n.getCoords());
    }

    /**
     * Standard getter for latitude
     *
     * @return latitude of this node
     */
    public Double getLat(){
        return coords.getLat();
    }

    /**
     * Standard getter for longitude
     *
     * @return longitude of this node
     */
    public Double getLon(){
        return coords.getLon();
    }

    /**
     * coords getter
     *
     * @return Coordinate object representing this node's location
     */
    @JsonIgnore
    public Coordinate getCoords(){
        return coords;
    }

    /**
     * standard getter for name
     *
     * @return name of this node
     */
    public String getName(){
        return name;
    }

    /**
     * standard toString()
     *
     * @return String repr of node
     */
    @Override
    public String toString(){
        return getID()+":"+"'"+name+"'@"+ coords;
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
