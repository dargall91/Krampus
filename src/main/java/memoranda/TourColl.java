package main.java.memoranda;

import java.util.Collection;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.memoranda.util.DuplicateKeyException;

/**
 * TourColl object holding a collection of tours in the MTB scheduling system.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class TourColl extends DataCollection<Tour> implements Iterable<Tour> {


    /**
     * create a new tour collection.
     */
    public TourColl() {
        super();
    }

    /**
     * add an entire collection of tours (post json import).
     *
     * @param c tours to add
     * @throws DuplicateKeyException if a provided Tour has a non-unique key
     */
    public TourColl(Collection<Tour> c) throws DuplicateKeyException {
        this();
        if (c != null) {
            for (Tour n : c) {
                add(n);
            }
        }
    }

    /**
     * Allows deserializing routes from JSON files.  TourLoader class is needed to deal with
     * converting route IDs to route objects.
     *
     * @param routeColl Collection of routes holding routes with indexes (IDs) held by TourLoader
     *                  obj
     * @param busColl   Collection of buses holding buses with indexes (IDs) held by TourLoader obj
     * @param c         TourLoader obj from json deserialization
     * @throws DuplicateKeyException if provided id/key is not unique
     */
    public TourColl(RouteColl routeColl, BusColl busColl, Collection<TourLoader> c) throws
            DuplicateKeyException {
        this();
        if (c != null) {
            for (TourLoader tl : c) {
                add(new Tour(routeColl, busColl, tl));
            }
        }
    }


    /**
     * Returns a new collection item with a unique key.
     *
     * @return new Tour with unique id
     */
    @Override
    public Tour newItem() {
        return new Tour(getUniqueID());
    }


    /**
     * get route by ID.
     *
     * @param id id to look up
     * @return Tour obj if found, null otherwise
     */
    public Tour get(int id) {
        return (Tour) super.get(id);
    }

    /**
     * json serialization routine.
     *
     * @return collection of Tours in this collection
     */
    @JsonProperty
    public Collection<IndexedObject> getTours() {
        return getData();
    }

    /**
     * iterator.
     *
     * @return Tour Iterator
     */
    @Override
    public Iterator<Tour> iterator() {
        return new TourColl.TourIterator();
    }

    /**
     * Defines how to Iterate over the collection.
     */
    public class TourIterator<TourT> implements Iterator<Tour> {
        Collection coll;
        Iterator<Tour> it;

        public TourIterator() {
            coll = getData();
            it = coll.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Tour next() {
            return it.next();
        }
    }
}
