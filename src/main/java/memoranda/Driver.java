package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Driver extends IndexedObject {
    private String name;
    private String phoneNumber;

    @JsonIgnore
    HashMap<Integer, Tour> tours;


    public Driver(int id){
        super(id);
        tours=new HashMap<>();
    }

    /**
     *
     * @param id
     * @param name
     * @param phoneNumber
     */
    public Driver(int id, String name, String phoneNumber){
        this(id);
        this.name=name;
        this.phoneNumber=phoneNumber;
    }


    /**
     *
     * @param tc
     * @param newDriver
     */
    public Driver(TourColl tc, DriverLoader newDriver){
        this(newDriver.getID());
        this.name=newDriver.getName();
        this.phoneNumber= newDriver.getPhoneNumber();
        for (int t:newDriver.getTourIDs()){
            addTour(tc.get(t));
        }
    }

    /**
     *
     * @param tour
     */
    public void addTour(Tour tour){
        tours.put(tour.getID(), tour);
    }


    /**
     *
     * @param id
     * @return
     */
    @JsonIgnore
    public Tour getTour(int id){
        return tours.get(id);
    }
    
    /**
    *
    * @param id
    * @return
    */
   @JsonIgnore
   public void removeTour(int id){
       tours.remove(id);
   }

    /**
     *
     * @return
     */
    @JsonIgnore
    public Collection<Tour> getTours(){
        return tours.values();
    }

    /**
     *
     * @return
     */
    @JsonProperty("tourIDs")
    public LinkedList<Integer> getTourIDs(){
        LinkedList<Integer> li=new LinkedList<>();
        for (Tour t: tours.values()){
            li.add(t.getID());
        }
        return li;
    }


    /**
     *
     * @param name
     */
    public void setName(String name){
        this.name=name;
    }

    /**
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }


    /**
     *
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return "Driver "+ getID()+":'"+name+"','"+phoneNumber+"'";
    }

}


