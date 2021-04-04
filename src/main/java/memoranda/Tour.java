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
    private int driverID;
    
    private final int NO_DRIVER_ID = -1;


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
        
        //Driver d = (Driver) driverColl.get(newTour.getDriverID());
        int d = newTour.getDriverID();
        
        if (d == NO_DRIVER_ID) {
        	driverID = NO_DRIVER_ID;
        }
        
        else {
        	driverID = d;
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
    * Assigns this Tour to a Driver
    * 
    * @param driverID The Driver's ID
    */
   public void setDriverID(int driverID){
       this.driverID = driverID;
   }
   
   /**
    * Unassigns a driver from this Tour
    * 
    * @param Driver
    */
   public void removeDriver(){
       driverID = NO_DRIVER_ID;
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
    * Gets the ID of the Driver assigned to this tour
    * 
    * @return A positive integer if there is a Driver assigned to tour, or the value of getNoDriverID() if there is not
    */
   @JsonProperty("driverID")
   public int getDriverID(){
	   return driverID;
   }
   
   /**
   * Returns the id value for a tour with no driver
   * 
   * @return the ID value that represents a driver-less tour
   */
  @JsonIgnore
  public int getNoDriverID(){
      return NO_DRIVER_ID;
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
