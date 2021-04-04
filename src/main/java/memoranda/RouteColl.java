package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.Iterator;

public class RouteColl extends DataCollection<Route> implements Iterable<Route> {


    /**
     * create a new route collection
     */
    public RouteColl(){
        super();
    }

    /**
     * add an entire collection of routes (post json import)
     *
     * @param c
     */
    public RouteColl(Collection<Route> c) throws DuplicateKeyException {
        this();
        for (Route n:c){
            add(n);
        }
    }

    /**
     * Allows deserializing routes from JSON files.  RouteLoader class is needed to deal with converting node IDs to
     * node objects
     *
     * @param nodeColl
     * @param c
     * @throws DuplicateKeyException
     */
    public RouteColl(NodeColl nodeColl, Collection<RouteLoader> c) throws DuplicateKeyException{
        this();
        for (RouteLoader rl: c){
            add(new Route(nodeColl, rl));
        }
    }


    /**
     * Returns a new collection item with a unique key
     *
     * @return
     */
    @Override
    public Route newItem(){
        return new Route(getUniqueID());
    }

    /**
     * Return a new route with a unique ID
     *
     * @return new Route object
     */
    public Route newRoute(){
        return new Route(getUniqueID());
    }

    /**
     * get route by ID
     * @param id
     * @return
     */
    public Route get(int id){
        return (Route)super.get(id);
    }

    /**
     *
     * @return
     */
    @JsonProperty
    public Collection<IndexedObject> getRoutes(){
        return getData();
    }

    /**
     * iterator
     * @return
     */
    @Override
    public Iterator<Route> iterator() {
        return new RouteColl.RouteIterator();
    }

    /**
     * iterator
     * @param <Route>
     */
    public class RouteIterator<Route> implements Iterator<Route>{
        Collection coll;
        Iterator<Route> it;
        public RouteIterator(){
            coll=getData();
            it=coll.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Route next() {
            return it.next();
        }
    }
}
