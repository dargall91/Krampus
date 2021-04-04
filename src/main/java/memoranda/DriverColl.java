package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.Iterator;

public class DriverColl extends DataCollection<Driver> implements Iterable<Driver>{

    /**
     * create a new Driver collection
     */
    public DriverColl(){
        super();
    }

    /**
     * add an entire collection of Drivers (post json import)
     *
     * @param c
     */
    public DriverColl(TourColl tourColl, Collection<DriverLoader> c) throws DuplicateKeyException {
        this();
        for (DriverLoader d:c){
            add(new Driver(tourColl, d));
        }
    }


    /**
     * Returns a new collection item with a unique key
     *
     * @return
     */
    public Driver newItem(){
        return new Driver(getUniqueID());
    }

    /**
     * Creates a new driver object with a unique ID
     *
     * @param d
     * @throws DuplicateKeyException
     */
    public void createUnique(Driver d) throws DuplicateKeyException {
        add(new Driver(getUniqueID(), d.getName(), d.getPhoneNumber()));
    }


    /**
     * return collection of Drivers for json output
     * @return
     */
    @JsonProperty
    public Collection<IndexedObject> getDrivers(){
        return getData();
    }



    /**
     * iterator
     * @return
     */
    @Override
    public Iterator<Driver> iterator() {
        return new DriverColl.DriverIterator();
    }

    /**
     * iterator
     * @param <Driver>
     */
    public class DriverIterator<Driver> implements Iterator<Driver>{
        Collection coll;
        Iterator<Driver> it;
        public DriverIterator(){
            coll=getData();
            it=coll.iterator();
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
