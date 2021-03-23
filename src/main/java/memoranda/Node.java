package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Node {
    private int id;
    private String name;
    private Coordinate coords;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Node(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("lat") Double lat,
                @JsonProperty("lon") Double lon){
        this.id=id;
        this.name=name;
        coords=new Coordinate(lat, lon);
    }

    public Double getLat(){
        return coords.getLat();
    }

    public Double getLon(){
        return coords.getLon();
    }

    public String getName(){
        return name;
    }

    /*
    public void setDescription(String description){
        this.description=description;
    }

    public String getDescription(){
        return description;
    }
     */

    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return id+":"+"'"+name+"'@"+getLat()+","+getLon();
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
