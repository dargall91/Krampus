package main.java.memoranda;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.memoranda.util.DuplicateKeyException;

/**
 * Tour object representing a route at a given time, assigned to a bus.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class Tour extends IndexedObject {
    public static final int DEFAULT_SPEED = 1;

    private String name;
    private int speed;
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
        speed = DEFAULT_SPEED;
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
     * @param routeColl collection of Routes containing the route with an ID matching that specified
     *                  in TourLoader obj
     * @param busColl   collection of Buses containing the Bus with an ID matching that specified in
     *                  TourLoader obj
     * @param newTour   TourLoader obj holding deserialized json data with integer route and bus
     *                  IDs
     * @throws IndexOutOfBoundsException if provided id is not unique
     * @throws DuplicateKeyException     if a bus already associated with another tour
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Tour(RouteColl routeColl, BusColl busColl, TourLoader newTour)
            throws IndexOutOfBoundsException, DuplicateKeyException {
        this(newTour.getID());

        System.out.println("In Tour: newTour=" + newTour);

        name = newTour.getName();


        if (newTour.getTime() == null) {
            time = null;
        } else {
            DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("HH:mm");
            time = LocalTime.parse(newTour.getTime(), timeParser);
        }

        speed = newTour.getSpeed();

        if (newTour.getBusID() == null) {
            bus = null;
        } else {
            Bus b = busColl.get(newTour.getBusID());
            if (b == null) {
                throw new IndexOutOfBoundsException(
                        "Bus index " + newTour.getBusID() + " not found");
            } else {
                bus = b;
                bus.addTour(this);
            }
        }

        if (newTour.getRouteID() == null) {
            route = null;
        } else {
            Route r = routeColl.get(newTour.getRouteID());
            if (r == null) {
                throw new IndexOutOfBoundsException("Route index " + newTour.getRouteID()
                        + " not found");
            } else {
                route = r;
            }
        }
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
     * name setter.
     *
     * @param name name for Tour
     */
    public void setName(String name) {
        this.name = name;
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
     * time setter.
     *
     * @param time time for Tour to start
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * updated name for this method.
     *
     * @return the time this tour starts
     */
    @JsonIgnore
    public LocalTime getStartTime() {
        return getTime();
    }

    /**
     * return the end time of this tour based on supplied speed.
     *
     * @return end time of the tour
     */
    @JsonIgnore
    public LocalTime getEndTime() {

        // route.duration is returned in hours.
        final int secPerHour = 3600;

        int travelTime = (int) (route.duration(speed) * secPerHour);
        return getTime().plusSeconds(travelTime);
    }

    /**
     * set the bus speed for duration calculations.
     *
     * @return the bus speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed for this tour in km/h to support end-time calculation.
     *
     * @param speed speed to set in km/h
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * json serialization routine.
     *
     * @return time as a string
     */
    @JsonProperty("time")
    public String getTimeString() {
        if (time == null) {
            return null;
        }
        return time.toString();
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
     * Set a driver for this tour.
     *
     * @param driver Driver to set.
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
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
            throw new UnsupportedOperationException("Cannot unilaterally remove driver. "
                    + "Call driver.delTour()");
        }

    }

    /**
     * Delete driver associated with this tour.
     *
     * @param route route to delete
     * @throws UnsupportedOperationException if an invalid driver is passed to the method.
     */
    public void delRoute(Route route) throws UnsupportedOperationException {
        if (this.route.equals(route)) {
            this.route = null;
        } else {
            throw new UnsupportedOperationException("Cannot unilaterally remove route. "
                    + "Call route.delTour()");
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
     * route setter.
     *
     * @param route Route for bus to travel
     */
    public void setRoute(Route route) {
        this.route = route;
        route.addTour(this);
    }

    /**
     * json serialization routine.
     *
     * @return integer id of this tour's route
     */
    @JsonProperty
    public Integer getRouteID() {
        if (route == null) {
            return null;
        }
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
     * bus setter.
     *
     * @param bus Bus for tour
     */
    public void setBus(Bus bus) {
        this.bus = bus;
    }

    /**
     * Delete bus associated with this tour.
     *
     * @param bus driver to delete
     * @throws UnsupportedOperationException if an invalid bus is passed to the method.
     */
    public void delBus(Bus bus) throws UnsupportedOperationException {
        if (this.bus.equals(bus)) {
            this.bus = null;
        } else {
            throw new UnsupportedOperationException("Cannot unilaterally remove bus. "
                    + "Call bus.delTour()");
        }
    }


    /**
     * json serialization routine.
     *
     * @return integer id of this tour's bus
     */
    @JsonProperty("busID")
    public Integer getBusID() {
        if (bus == null) {
            return null;
        } else {
            return bus.getID();
        }
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
