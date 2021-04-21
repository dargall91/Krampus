package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Iterator;

import main.java.memoranda.util.DuplicateKeyException;

/**
 * DriverColl object holding a collection of drivers in the MTB scheduling system.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class DriverColl extends DataCollection<Driver> implements Iterable<Driver> {

    /**
     * create a new Driver collection.
     */
    public DriverColl() {
        super();
    }

    /**
     * add an entire collection of Drivers (post json import).
     *
     * @param tourColl collection of Tours containing a Tour with the integer IDs associated
     *                 with this driver's Tours
     * @param c        collection of DriverLoader objects to convert to Driver objs and add
     *                 to this collection
     * @throws DuplicateKeyException if a provided Driver id (in DriverLoader obj) is not unique
     */
    public DriverColl(TourColl tourColl, Collection<DriverLoader> c) throws DuplicateKeyException {
        this();
        if (c != null) {
            for (DriverLoader d : c) {
                add(new Driver(tourColl, d));
            }
        }
    }


    /**
     * Returns a new collection item with a unique key.
     *
     * @return new Driver obj with unique key
     */
    public Driver newItem() {
        return new Driver(getUniqueID());
    }


    /**
     * json serialization routine - return collection of Drivers for json output.
     *
     * @return collection of Drivers in this collection
     */
    @JsonProperty
    public Collection<IndexedObject> getDrivers() {
        return getData();
    }


    /**
     * iterator.
     *
     * @return Iterator
     */
    @Override
    public Iterator<Driver> iterator() {
        return new DriverColl.DriverIterator();
    }

    /**
     * Defines how to Iterate over the collection.
     */
    public class DriverIterator<DriverT> implements Iterator<Driver> {
        Collection coll;
        Iterator<Driver> it;

        public DriverIterator() {
            coll = getData();
            it = coll.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Driver next() {
            return it.next();
        }
    }

}
