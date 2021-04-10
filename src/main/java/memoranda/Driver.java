package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import main.java.memoranda.util.DuplicateKeyException;

/**
 * Driver object representing a driver in the MTB scheduling system.  Drivers can be associated with tours.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class Driver extends IndexedObject {
    private String name;
    private String phoneNumber;

    @JsonIgnore
    private HashMap<Integer, Tour> tours;


    /**
     * stock constructor.
     *
     * @param id id for driver
     */
    public Driver(int id) {
        super(id);
        tours = new HashMap<>();
    }

    /**
     * create driver obj with specified ID, name, phonenumber.
     *
     * @param id          driver id
     * @param name        driver name
     * @param phoneNumber driver phonenumber
     */
    public Driver(int id, String name, String phoneNumber) {
        this(id);
        this.name = name;
        this.phoneNumber = phoneNumber;
    }


    /**
     * build a Driver based upon json deserialization data (DriverLoader) which requires Tour
     * information.
     *
     * @param tc        the tourColl
     * @param newDriver the DriverLoader object
     */
    public Driver(TourColl tc, DriverLoader newDriver) throws DuplicateKeyException {
        this(newDriver.getID());
        System.out.println("Adding tour driver " + newDriver.getID() + " to tours " + newDriver.getTourIDs());
        //WIP
        this.name = newDriver.getName();
        this.phoneNumber = newDriver.getPhoneNumber();
        for (int t : newDriver.getTourIDs()) {
            addTour(tc.get(t));
        }
    }

    /**
     * Add a tour to this driver.
     *
     * @param tour the tour to add
     */
    public void addTour(Tour tour) throws DuplicateKeyException {
        if (tour.getDriver() != this && tour.getDriver() != null) {
            throw new DuplicateKeyException("Tour already associated with a driver");
        }
        tour.setDriver(this);
        tours.put(tour.getID(), tour);
    }


    /**
     * Remove a tour from this driver.
     *
     * @param tour Tour to delete from this driver
     */
    public void delTour(Tour tour) {
        Tour foundTour = tours.get(tour.getID());
        if (foundTour != null) {
            tour.delDriver(this);
        }
        tours.remove(tour.getID());
    }

    /**
     * gets a tour based on ID.
     *
     * @param id the integer id for the tour.
     * @return Tour if exists, null otherwise
     */
    @JsonIgnore
    public Tour getTour(int id) {
        return tours.get(id);
    }

    /**
     * gets a full list of this driver's tours.
     *
     * @return Tour collection; null if no tours.
     */
    @JsonIgnore
    public Collection<Tour> getTours() {
        return tours.values();
    }

    /**
     * gets a list of integer tour IDs for all tours for this driver. Used for json serialization.
     *
     * @return a linked list of integers representing the tour IDs associated with this driver
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
     * name setter.
     *
     * @param name the driver's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * phonenumber setter.
     *
     * @param phoneNumber the driver's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * name getter.
     *
     * @return driver's name
     */
    public String getName() {
        return name;
    }

    /**
     * phonenumber getter.
     *
     * @return driver's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * standard toString() function.
     *
     * @return string repr of this obj
     */
    @Override
    public String toString() {
        return "Driver " + getID() + ":'" + name + "','" + phoneNumber + "'";
    }

}


