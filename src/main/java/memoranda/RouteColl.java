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

    public RouteColl(NodeColl nodeColl, Collection<RouteLoader> c) throws DuplicateKeyException{
        this();
        for (RouteLoader rl: c){
            add(new Route(nodeColl, rl));
        }
    }


    /**
     * Creates a new Route object with a unique ID
     *
     * @param n
     * @throws DuplicateKeyException
     */
    @Override
    public void createUnique(Route r) throws DuplicateKeyException {
        add(new Route(getUniqueID(), r));
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