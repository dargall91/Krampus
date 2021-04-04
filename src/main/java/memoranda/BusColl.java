package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.Iterator;

/**
 * BusColl object holding a collection of buses in the MTB scheduling system.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class BusColl extends DataCollection<Bus> implements Iterable<Bus>{

    /**
     * create a new bus collection
     */
    public BusColl(){
        super();
    }



    /**
     * add an entire collection of buses (post json import)
     *
     * @param c Bus objs to add to collection
     */
    public BusColl(Collection<Bus> c) throws DuplicateKeyException {
        this();
        for (Bus b:c){
            add(b);
        }
    }

    /**
     * return new Bus obj with unique id
     *
     * @return new Bus obj with unique id
     */
    @Override
    public Bus newItem(){
        return new Bus(getUniqueID());
    }


    /**
     * get bus by ID
     *
     * @param id id of bus to lookup
     * @return Bus obj if found, null otherwise
     */
    public Bus get(int id){
        return (Bus)super.get(id);
    }


    /**
     * json serialization routine
     *
     * @return collection of Bus objects held in this collection
     */
    @JsonProperty
    public Collection<IndexedObject> getBuses(){
        return getData();
    }



    /**
     * iterator
     *
     * @return Bus Iterator
     */
    @Override
    public Iterator<Bus> iterator() {
        return new BusColl.BusIterator();
    }

    /**
     * iterator
     *
     * @param <Bus>
     */
    public class BusIterator<Bus> implements Iterator<Bus>{
        Collection coll;
        Iterator<Bus> it;
        public BusIterator(){
            coll=getData();
            it=coll.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Bus next() {
            return it.next();
        }
    }
}
