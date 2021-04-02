package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

public class Driver extends IndexedObject {
    String name;
    String phoneNumber;

    @JsonIgnore
    LinkedList<Tour> tours;


    public Driver(int id){
        super(id);
    }

    /**
     *
     * @param id
     * @param name
     * @param phoneNumber
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Driver(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("phoneNumber")
                  String phoneNumber){
        super(id);
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    // WIP
    /**
     *
     * @param tour
     */
    public void addTour(Tour tour){

    }

    // WIP
    /**
     *
     * @return
     */
    public LinkedList<Tour> getTours(){
        return null;
    }

    // WIP
    public LinkedList<Integer> getTourIDs(){
        return null;
    }


    /**
     *
     * @param name
     */
    public void setName(String name){
        this.name=name;
    }

    /**
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }


    /**
     *
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return "Driver "+getId()+":'"+name+"','"+phoneNumber+"'";
    }

}


