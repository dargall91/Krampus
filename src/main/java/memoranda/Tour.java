package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Holds a tour - a route at a particular time
 */
public class Tour extends IndexedObject{
    private String name;
    private LocalDateTime time;
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
    public Tour(int id, String name, Route route, LocalDateTime time){
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
        this.name=newTour.getName();

        n = routeColl.get(newTour.getRouteId());
        if (n == null) {
            throw new IndexOutOfBoundsException("Route index " + newTour.getRouteId() + " not found");
        } else {
            setRoute(n);
        }
    }



    /**
     *
     * @param route
     */
    public void setRoute(Route route){
        this.route=route;
    }

    // WIP
    /**
     *
     * @param time
     */
    public void setTime(String time){

    }

    // WIP
    /**
     *
     * @param time
     */
    public void setTime(LocalDateTime time){

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
