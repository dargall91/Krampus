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
    private Route route;


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
    public Tour(int id, String name, Route route, LocalTime time){
        this(id);
        this.name=name;
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
    public Tour(RouteColl routeColl, TourLoader newTour) throws IndexOutOfBoundsException{
        this(newTour.getId());

        Route n;
        name=newTour.getName();

        DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("HH:mm");
        time = LocalTime.parse(newTour.getTime(), timeParser);

        n = routeColl.get(newTour.getRouteId());
        if (n == null) {
            throw new IndexOutOfBoundsException("Route index " + newTour.getRouteId() + " not found");
        } else {
            setRoute(n);
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
    public int getRouteId(){
        return route.getId();
    }

    /**
     * standard toString()
     * @return
     */
    @Override
    public String toString(){
        return getId()+":"+"'"+name+"'"+route;
    }
}
