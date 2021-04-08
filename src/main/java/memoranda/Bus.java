package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bus object representing a bus in the MTB scheduling system.
 * Buses can be part of tours
 *
 * @author Brian Pape
 * @version 2021-04-01
 */

public class Bus extends IndexedObject {
    private int number;

    /**
     * Standard constructor. Sets id for this bus.
     *
     * @param id bus id
     */
    Bus(Integer id) {
        super(id);
    }


    /**
     * constructor for json deserialization
     *
     * @param id     bus ID (typically unique)
     * @param number bus number (the number painted on the bus's side)
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    Bus(@JsonProperty("id") Integer id, @JsonProperty("number") int number) {
        this(id);
        this.number = number;
    }


    /**
     * number setter
     *
     * @param number bus number
     */
    public void setNumber(int number) {
        this.number = number;
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
     * standard toString() returns string repr of obj
     *
     * @return string repr of obj
     */
    @Override
    public String toString() {
        return "bus id " + getID() + " number " + getNumber();
    }
}
