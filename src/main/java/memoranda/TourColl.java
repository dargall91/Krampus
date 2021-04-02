package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.Iterator;

/**
 * Holds all loaded Tours
 */
public class TourColl extends DataCollection<Tour> implements Iterable<Tour> {


    /**
     * create a new route collection
     */
    public TourColl(){
        super();
    }

    /**
     * add an entire collection of routes (post json import)
     *
     * @param c
     */
    public TourColl(Collection<Tour> c) throws DuplicateKeyException {
        this();
        for (Tour n:c){
            add(n);
        }
    }

    /**
     * Allows deserializing routes from JSON files.  TourLoader class is needed to deal with converting route IDs to
     * route objects
     *
     * @param routeColl
     * @param c
     * @throws DuplicateKeyException
     */
    public TourColl(RouteColl routeColl, Collection<TourLoader> c) throws DuplicateKeyException{
        this();
        System.out.println("in TourCOll constructor. routecoll="+routeColl+", tourloader coll="+c);
        for (TourLoader tl: c){
            System.out.println("adding new tour to this collection");
            add(new Tour(routeColl, tl));
        }
    }


    /**
     * Returns a new collection item with a unique key
     *
     * @return
     */
    @Override
    public Tour newItem(){
        return new Tour(getUniqueID());
    }


    /**
     * get route by ID
     * @param id
     * @return
     */
    public Tour get(int id){
        return (Tour)super.get(id);
    }

    /**
     *
     * @return
     */
    @JsonProperty
    public Collection<IndexedObject> getTours(){
        return getData();
    }

    /**
     * iterator
     * @return
     */
    @Override
    public Iterator<Tour> iterator() {
        return new TourColl.TourIterator();
    }

    /**
     * iterator
     * @param <Tour>
     */
    public class TourIterator<Tour> implements Iterator<Tour>{
        Collection coll;
        Iterator<Tour> it;
        public TourIterator(){
            coll=getData();
            it=coll.iterator();
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
