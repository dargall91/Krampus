package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Holds a tour - a route at a particular time
 */
public class Tour extends IndexedObject{
    private String name;
    private LocalTime time;
    private Bus bus;
    private Route route;
    private Driver driver;


    /**
     *
     * @param id
     */
    public Tour(int id){
        super(id);
    }


    /**
     *
     * @param id
     * @param name
     * @param route
     * @param time
     */
    public Tour(int id, String name, Bus bus, Route route, LocalTime time){
        this(id);
        this.name=name;
        this.bus=bus;
        this.route=route;
        this.time=time;
    }


    /**
     *
     * @param routeColl
     * @param newTour
     * @throws IndexOutOfBoundsException
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Tour(RouteColl routeColl, BusColl busColl, TourLoader newTour) throws IndexOutOfBoundsException{
        this(newTour.getID());

        System.out.println("In Tour: newTour="+newTour);

        name=newTour.getName();

        DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("HH:mm");
        time = LocalTime.parse(newTour.getTime(), timeParser);

        Bus b= busColl.get(newTour.getBusID());
        if (b == null) {
            throw new IndexOutOfBoundsException("Bus index " + newTour.getBusID() + " not found");
        } else {
            bus=b;
        }

        Route r = routeColl.get(newTour.getRouteID());
        if (r == null) {
            throw new IndexOutOfBoundsException("Route index " + newTour.getRouteID() + " not found");
        } else {
            route=r;
        }
    }


    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     *
     * @param bus
     */
    public void setBus(Bus bus){
        this.bus=bus;
    }

    /**
     *
     * @param route
     */
    public void setRoute(Route route){
        this.route=route;
    }


    /**
     *
     * @param time
     */
    public void setTime(LocalTime time){
        this.time=time;
    }

    /**
     * standard getter for name
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return
     */
    @JsonIgnore
    public LocalTime getTime(){
        return time;
    }

    @JsonProperty("time")
    public String getTimeString(){
        return time.toString();
    }


   /**
    * Set a driver for this tour.
    *
    * @param driver Driver to set.
    */
   public void setDriver(Driver driver){
        this.driver=driver;
   }

    /**
     * Get the driver associated with this tour.
     *
     * @return Driver associated with this tour.
     */
    @JsonIgnore
    public Driver getDriver(){
        return driver;
    }

    /**
     * Delete driver associated with this tour.
     */
    public void delDriver(){
        driver=null;
    }

    /**
     *
     * @return
     */
    @JsonIgnore
    public Route getRoute(){
        return route;
    }

    /**
     * Returns an ordered list of only the ID of the route in this tour for storing in json file
     * @return
     */
    @JsonProperty
    public int getRouteID(){
        return route.getID();
    }


    /**
     *
     * @return
     */
    @JsonIgnore
    public Bus getBus(){
        return bus;
    }


    /**
     *
     * @return
     */
    @JsonProperty("busID")
    public int getBusID(){
        return bus.getID();
    }

    /**
     * standard toString()
     * @return
     */
    @Override
    public String toString(){
        return getID()+":"+"'"+name+"'"+route;
    }
}
