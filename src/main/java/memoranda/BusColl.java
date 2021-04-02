package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.Iterator;

public class BusColl extends DataCollection<Bus> implements Iterable<Bus>{

    /**
     * create a new node collection
     */
    public BusColl(){
        super();
    }



    /**
     * add an entire collection of nodes (post json import)
     *
     * @param c
     */
    public BusColl(Collection<Bus> c) throws DuplicateKeyException {
        this();
        for (Bus b:c){
            add(b);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Bus newItem(){
        return new Bus(getUniqueID());
    }


    /**
     * get bus by ID
     * @param id
     * @return
     */
    public Bus get(int id){
        return (Bus)super.get(id);
    }


    /**
     *
     * @return
     */
    @JsonProperty
    public Collection<IndexedObject> getBuses(){
        return getData();
    }



    /**
     * iterator
     * @return
     */
    @Override
    public Iterator<Bus> iterator() {
        return new BusColl.BusIterator();
    }

    /**
     * iterator
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
