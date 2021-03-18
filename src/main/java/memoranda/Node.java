package main.java.memoranda;

public class Node {
    private int id;
    private String name;
    private String description;
    private doublelatitude;
    private double longitude;

    public Node(int id, String name, double latitude, double longitude){
        this.id=id;
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String getName(){
        return getName();
    }

    public String getDescription(){
        return getDescription();
    }

    public int getId() {
        return id;
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
