package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

public class Route extends IndexedObject{
    private String name;
    private LinkedList<Node> route;


    public Route(int id){
        super(id);
        route=new LinkedList<>();
    }

    /**
     *
     * @param nodeColl
     * @param newRoute
     * @throws IndexOutOfBoundsException
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Route(NodeColl nodeColl, RouteLoader newRoute) throws IndexOutOfBoundsException{
        this(newRoute.getId());

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

    /**
     * Create a new Route based upon the supplied route but with the newly supplied id.  This is to support
     * creation of routes by RouteColl that have a guaranteed unique id.
     * @param id
     * @param r
     */
    public Route(int id, Route r){
        this(id);
        this.name=r.getName();
        this.route=r.getRoute();
    }

    /**
     * Add a new node to the end of this route
     *
     * @param node
     */
    public void addNode(Node node){
        route.add(node);
    }


    /**
     * Returns the length of this route
     * @return
     */
    public Double length() {
        double len=0.0;

        // a route of 0 or 1 nodes has a length of 0
        if (route.size() < 2){
            return 0.0;
        } else {
            boolean load=true;
            Node tmp=null;
            for (Node n: route){
                if (load){
                    tmp=n;
                    load=false;
                } else{
                    len += (tmp.distanceTo(n));
                    tmp=n;
                }

            }
        }
        return len;
    }

    /**
     * returns the amount of time in hours required to traverse this route based upon the given speed in km/h.
     * @param speed
     * @return
     */
    public Double duration(double speed){
        return length()/speed;
    }

    /**
     * standard getter for name
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * returns an ordered list of the nodes in the route
     * @return
     */
    @JsonIgnore
    public LinkedList<Node> getRoute(){
        return route;
    }

    /**
     * Returns an ordered list of only the IDs of the nodes in this route for storing in json file
     * @return
     */
    @JsonProperty
    public LinkedList<Integer> getNodeIDs(){
        LinkedList<Integer> li=new LinkedList<>();
        for (Node n: route){
            li.add(n.getId());
        }
        return li;
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
