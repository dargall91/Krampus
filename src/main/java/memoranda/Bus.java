package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Bus object representing a bus in the MTB scheduling system.
 * Buses can be part of tours
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class Bus extends IndexedObject {
    private int number;

    @JsonIgnore
    private HashMap<Integer, Tour> tours;

    /**
     * Standard constructor. Sets id for this bus.
     *
     * @param id bus id
     */
    public Bus(Integer id) {
        super(id);
        tours = new HashMap<>();
    }
    
    /**
     * Create bus obj with specified number
     *
     * @param id bus id
     */
    public Bus(int id, int number) {
        this(id);
        this.number = number;
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
     * build a Driver based upon json deserialization data (DriverLoader) which requires Tour
     * information.
     *
     * @param tc        the tourColl
     * @param newDriver the DriverLoader object
     */
    public Bus(TourColl tc, BusLoader newBus) throws DuplicateKeyException {
        this(newBus.getID());
        System.out.println("Adding tour driver " + newBus.getID() + " to tours " + newBus.getTourIDs());
        //WIP
        this.number = newBus.getNumber();
        for (int t : newBus.getTourIDs()) {
            addTour(tc.get(t));
        }
    }


    /**
     * Add a tour to this bus.
     *
     * @param tour the tour to add
     */
    public void addTour(Tour tour) throws DuplicateKeyException {
        if (tour.getBus() != this && tour.getBus() != null) {
            throw new DuplicateKeyException("Tour already associated with a bus");
        }
        tour.setBus(this);
        tours.put(tour.getID(), tour);
    }


    /**
     * Remove a tour from this bus.
     *
     * @param tour Tour to delete from this bus
     */
    public void delTour(Tour tour) {
        Tour foundTour = tours.get(tour.getID());
        if (foundTour != null) {
            tour.delBus(this);
        }
        tours.remove(tour.getID());
    }

    /**
     * gets a tour based on ID.
     *
     * @param id the integer id for the tour
     * @return Tour if exists, null otherwise
     */
    @JsonIgnore
    public Tour getTour(int id) {
        return tours.get(id);
    }

    /**
     * gets a full list of this bus's tours.
     *
     * @return Tour collection; null if no tours
     */
    @JsonIgnore
    public Collection<Tour> getTours() {
        return tours.values();
    }

    /**
     * gets a list of integer tour IDs for all tours for this bus. Used for json serialization.
     *
     * @return a linked list of integers representing the tour IDs associated with this bus
     */
    @JsonProperty("tourIDs")
    public LinkedList<Integer> getTourIDs() {
        LinkedList<Integer> li = new LinkedList<>();
        for (Tour t : tours.values()) {
            li.add(t.getID());
        }
        return li;
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
