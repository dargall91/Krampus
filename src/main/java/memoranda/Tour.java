package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Tour object representing a route at a given time, assigned to a bus.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class Tour extends IndexedObject {
    private String name;
    private LocalTime time;
    private Bus bus;
    private Route route;
    private Driver driver;


    /**
     * create new Tour object with given id.
     *
     * @param id id for Tour
     */
    public Tour(int id) {
        super(id);
    }


    /**
     * create new tour with given information.
     *
     * @param id    id for tour
     * @param name  name for tour
     * @param bus   bus assigned to tour
     * @param route Route for tour
     * @param time  time that tour starts
     */
    public Tour(int id, String name, Bus bus, Route route, LocalTime time) {
        this(id);
        this.name = name;
        this.bus = bus;
        this.route = route;
        this.time = time;
    }


    /**
     * constructor for json deserialization.
     *
     * @param routeColl collection of Routes containing the route with an ID matching that
     *                  specified in TourLoader obj
     * @param busColl   collection of Buses containing the Bus with an ID matching that
     *                  specified in TourLoader obj
     * @param newTour   TourLoader obj holding deserialized json data with integer route and bus IDs
     * @throws IndexOutOfBoundsException if provided id is not unique
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Tour(RouteColl routeColl, BusColl busColl, TourLoader newTour)
            throws IndexOutOfBoundsException {
        this(newTour.getID());

        System.out.println("In Tour: newTour=" + newTour);

        name = newTour.getName();

        DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("HH:mm");
        time = LocalTime.parse(newTour.getTime(), timeParser);

        Bus b = busColl.get(newTour.getBusID());
        if (b == null) {
            throw new IndexOutOfBoundsException("Bus index " + newTour.getBusID() + " not found");
        } else {
            bus = b;
        }

        Route r = routeColl.get(newTour.getRouteID());
        if (r == null) {
            throw new IndexOutOfBoundsException("Route index " + newTour.getRouteID()
                    + " not found");
        } else {
            route = r;
        }
    }


    /**
     * name setter.
     *
     * @param name name for Tour
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * bus setter.
     *
     * @param bus Bus for tour
     */
    public void setBus(Bus bus) {
        this.bus = bus;
    }

    /**
     * route setter.
     *
     * @param route Route for bus to travel
     */
    public void setRoute(Route route) {
        this.route = route;
    }


    /**
     * time setter.
     *
     * @param time time for Tour to start
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * standard getter for name.
     *
     * @return name of tour
     */
    public String getName() {
        return name;
    }

    /**
     * time getter.
     *
     * @return time of tour
     */
    @JsonIgnore
    public LocalTime getTime() {
        return time;
    }

    /**
     * json serialization routine.
     *
     * @return time as a string
     */
    @JsonProperty("time")
    public String getTimeString() {
        return time.toString();
    }


    /**
     * Set a driver for this tour.
     *
     * @param driver Driver to set.
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Get the driver associated with this tour.
     *
     * @return Driver associated with this tour.
     */
    @JsonIgnore
    public Driver getDriver() {
        return driver;
    }

    /**
     * Delete driver associated with this tour.
     *
     * @param driver driver to delete
     * @throws UnsupportedOperationException if an invalid driver is passed to the method.
     */
    public void delDriver(Driver driver) throws UnsupportedOperationException {
        if (this.driver.equals(driver)) {
            this.driver = null;
        } else {
            throw new UnsupportedOperationException("Cannot unilaterally remove driver. " +
                    "Call driver.delTour()");
        }

    }

    /**
     * route getter.
     *
     * @return route for tour
     */
    @JsonIgnore
    public Route getRoute() {
        return route;
    }

    /**
     * json serialization routine.
     *
     * @return integer id of this tour's route
     */
    @JsonProperty
    public int getRouteID() {
        return route.getID();
    }


    /**
     * bus getter.
     *
     * @return Bus for this route
     */
    @JsonIgnore
    public Bus getBus() {
        return bus;
    }


    /**
     * json serialization routine.
     *
     * @return integer id of this tour's bus
     */
    @JsonProperty("busID")
    public int getBusID() {
        return bus.getID();
    }

    /**
     * standard toString().
     *
     * @return string repr of obj
     */
    @Override
    public String toString() {
        return getID() + ":" + "'" + name + "'" + route;
    }
}
