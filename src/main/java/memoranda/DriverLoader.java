package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedList;

/**
 * Used to load drivers with numeric IDs from JSON and subsequently be converted into an object
 * hierarchy.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class DriverLoader extends IndexedObject {
    private String name;
    private String phoneNumber;
    private LinkedList<Integer> tourIDs;


    /**
     * constructor for json deserialization.
     *
     * @param id          id of driver
     * @param name        name of driver
     * @param phoneNumber phone number of driver
     * @param tourIDs     integer ids for tours associated with driver
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DriverLoader(@JsonProperty("id") int id, @JsonProperty("name") String name,
                        @JsonProperty("phoneNumber") String phoneNumber,
                        @JsonProperty("tourIDs") LinkedList<Integer> tourIDs) {
        super(id);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.tourIDs = tourIDs;
    }


    /**
     * name getter.
     *
     * @return name of driver
     */
    public String getName() {
        return name;
    }

    /**
     * phonenumber getter.
     *
     * @return phone number of driver
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }


    /**
     * returns a list of integer tour IDs for this driver.
     *
     * @return list of integer tour IDs for this driver
     */
    public LinkedList<Integer> getTourIDs() {
        return tourIDs;
    }


    /**
     * standard toString().
     *
     * @return string repr of obj
     */
    @Override
    public String toString() {
        return "driver " + getID() + " " + name;
    }

}
