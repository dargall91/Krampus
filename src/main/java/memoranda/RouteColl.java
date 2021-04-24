package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Iterator;
import main.java.memoranda.util.DuplicateKeyException;

/**
 * RouteColl object holding a collection of routes in the MTB scheduling system.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class RouteColl extends DataCollection<Route> implements Iterable<Route> {


    /**
     * create a new route collection.
     */
    public RouteColl() {
        super();
    }

    /**
     * add an entire collection of routes (post json import).
     *
     * @param c collection of routes to add to collection
     * @throws DuplicateKeyException if a provided Route has a non-unique key
     */
    public RouteColl(Collection<Route> c) throws DuplicateKeyException {
        this();
        if (c != null) {
            for (Route n : c) {
                add(n);
            }
        }
    }

    /**
     * Allows deserializing routes from JSON files.  RouteLoader class is needed to deal with
     * converting node IDs to node objects in json deserialization.
     *
     * @param nodeColl collection of nodes that holds IDs matching those used in each Route being
     *                 added.
     * @param c        collection of RouteLoader objects (only used in json deserialization)
     * @throws DuplicateKeyException if duplicate key exists in provided routes
     */
    public RouteColl(NodeColl nodeColl, Collection<RouteLoader> c) throws DuplicateKeyException {
        this();
        if (c != null) {
            for (RouteLoader rl : c) {
                add(new Route(nodeColl, rl));
            }
        }
    }


    /**
     * Returns a new collection item with a unique key.
     *
     * @return a new Route object with a unique id
     */
    @Override
    public Route newItem() {
        return new Route(getUniqueID());
    }


    /**
     * get route by ID.
     *
     * @param id route id to find
     * @return matching Route or null if none found
     */
    public Route get(int id) {
        return (Route) super.get(id);
    }

    /**
     * returns a collection of Routes.
     *
     * @return all routes in this collection
     */
    @JsonProperty
    public Collection<IndexedObject> getRoutes() {
        return getData();
    }

    /**
     * iterator.
     *
     * @return a Route Iterator
     */
    @Override
    public Iterator<Route> iterator() {
        return new RouteColl.RouteIterator();
    }

    /**
     * Defines how to Iterate over the collection.
     */
    public class RouteIterator<RouteT> implements Iterator<Route> {
        Collection coll;
        Iterator<Route> it;

        public RouteIterator() {
            coll = getData();
            it = coll.iterator();
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
