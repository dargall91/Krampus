package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
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
    }

    /**
     *
     * @param id
     * @param name
     * @param phoneNumber
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Driver(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("phoneNumber")
                  String phoneNumber){
        super(id);
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    // WIP
    /**
     *
     * @param tour
     */
    public void addTour(Tour tour){
        tours.put(tour.getId(), tour);
    }


    /**
     *
     * @param id
     * @return
     */
    public Tour getTour(int id){
        return tours.get(id);
    }

    /**
     *
     * @return
     */
    @JsonProperty
    public Collection<Tour> getTours(){
        return tours.values();
    }

    /**
     *
     * @return
     */
    public LinkedList<Integer> getTourIDs(){
        LinkedList<Integer> li=new LinkedList<>();
        for (Tour t: tours.values()){
            li.add(t.getId());
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
        return "Driver "+getId()+":'"+name+"','"+phoneNumber+"'";
    }

}


