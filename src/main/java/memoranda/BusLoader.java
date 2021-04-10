package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

/**
 * Used to load drivers with numeric IDs from JSON and subsequently be converted into an object hierarchy
 *
 * @author Derek Argall
 * @version 2021-04-09
 */
public class BusLoader extends IndexedObject {
    private int number;
    private LinkedList<Integer> tourIDs;


    /**
     * constructor for json deserialization
     *
     * @param id          id of driver
     * @param name        name of driver
     * @param phoneNumber phone number of driver
     * @param tourIDs     integer ids for tours associated with driver
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BusLoader(@JsonProperty("id") int id, @JsonProperty("number") int number,
                        @JsonProperty("tourIDs") LinkedList<Integer> tourIDs) {
        super(id);
        this.number = number;
        this.tourIDs = tourIDs;
    }

    /**
     * number getter
     *
     * @return bus number
     */
    public int getNumber() {
        return number;
    }


    /**
     * returns a list of integer tour IDs for this driver
     *
     * @return list of integer tour IDs for this driver
     */
    public LinkedList<Integer> getTourIDs() {
        return tourIDs;
    }


    /**
     * standard toString()
     *
     * @return string repr of obj
     */
    @Override
    public String toString() {
        return "bus " + getID() + " " + number;
    }

}
