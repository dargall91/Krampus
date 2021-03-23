package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.DataCollection;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DriverColl extends DataCollection implements Iterable<Driver>{
    private HashMap<Integer, Driver> driverList;

    /**
     * create a new Driver collection
     */
    public DriverColl(){
        driverList=new HashMap<>();
    }

    /**
     * add an entire collection of Drivers (post json import)
     *
     * @param c
     */
    public DriverColl(Collection<Driver> c) throws DuplicateKeyException {
        this();
        for (Driver n:c){
            addDriver(n);
        }
    }

    /**
     * add a Driver
     * @param d
     */
    public void addDriver(Driver d) throws DuplicateKeyException {
        if (getDriver(d.getId()) != null){
            throw new DuplicateKeyException("Key "+d.getId()+" already exists.");
        }

        // save the max ID
        registerID(d.getId());
        driverList.put(d.getId(), d);
    }

    /**
     *
     * @param name
     * @param phoneNumber
     * @throws DuplicateKeyException
     */
    public void createDriver(String name, String phoneNumber) throws DuplicateKeyException {
        addDriver(new Driver(getUniqueID(), name, phoneNumber));
    }

    /**
     * get Driver by ID
     * @param id
     * @return
     */
    public Driver getDriver(int id){
        return driverList.get(id);
    }

    /**
     * return collection of Drivers for json output
     * @return
     */
    @JsonProperty
    public Collection<Driver> getDrivers(){
        return driverList.values();
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
            coll=driverList.values();
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
