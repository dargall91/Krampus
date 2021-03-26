package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

public class Route extends IndexedObject{
    private String name;
    private LinkedList<Node> route;

    /**
     *
     * @param id
     * @param name
     * @param lat
     * @param lon
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Route(NodeColl nodeColl, RouteLoader newRoute) throws IndexOutOfBoundsException{
        super(newRoute.getId());

        Node n;
        this.name=newRoute.getName();
        for (Integer i:newRoute.getNodes()){
                n=nodeColl.get(i);
            if (n == null) {
                throw new IndexOutOfBoundsException("Node index "+i+" not found");
            } else{
                route.add(n);
            }
        }
        this.name=name;
    }

    public Route(int id, Route r){
        super(id);
        this.name=r.getName();
        this.route=r.getRoute();
    }


    /**
     * Returns the length of this route
     * @return
     */
    public Double length(){
        return 0.0;
    }

    /**
     * returns the amount of time required to traverse this route based upon the given speed in km/h.
     * @param speed
     * @return
     */
    public Double duration(double speed){
        return 0.0;
    }

    /**
     * standard getter for name
     * @return
     */
    public String getName(){
        return name;
    }

    public LinkedList<Node> getRoute(){
        return route;
    }

    /**
     * standard toString()
     * @return
     */
    @Override
    public String toString(){
        return getId()+":"+"'"+name;
    }

}
