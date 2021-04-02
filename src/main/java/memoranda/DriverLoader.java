package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

public class DriverLoader extends IndexedObject{
    private String name;
    private String phoneNumber;
    private LinkedList<Integer> tourIDs;


    /**
     *
     * @param id
     * @param name
     * @param phoneNumber
     * @param tourIDs
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DriverLoader(@JsonProperty("id") int id, @JsonProperty("name") String name,
                        @JsonProperty("phoneNumber") String phoneNumber,
                        @JsonProperty("tourIDs") LinkedList<Integer> tourIDs) {
        super(id);
        this.name = name;
        this.phoneNumber=phoneNumber;
        this.tourIDs = tourIDs;
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
    public LinkedList<Integer> getTourIDs(){
        return tourIDs;
    }


    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return "driver " + getId() + " " + name;
    }

}
