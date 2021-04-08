package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.HashMap;

/**
 * DataCollection abstract class
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public abstract class DataCollection <T extends IndexedObject> implements Iterable<T>{
    private HashMap<Integer, IndexedObject> coll;
    private int maxID=0;


    /**
     * create a new node collection
     */
    public DataCollection(){
        coll=new HashMap<>();
    }

    /**
     * add an entire collection of nodes (post json import)
     *
     * @param c collection of data nodes to add
     * @throws DuplicateKeyException if a provided node ID is not unique
     */
    public DataCollection(Collection<Node> c) throws DuplicateKeyException{
        this();
        for (Node n:c){
            add(n);
        }
    }


    /**
     * abstract method for creating new collection item
     *
     * @return new data node
     */
    public abstract T newItem();


     /**
     * Returns a unique key to be used in Node construction
     *
     * @return unique id for new node
     */
    @JsonIgnore
    public int getUniqueID(){
        return ++maxID;
    }


    public int size(){
        return coll.size();
    }

    /**
     * reset the maximum ID tracker after deleting an entry.
     *
     * @param h hashmap of collection objects to scan
     * @param <V> type of hashmap value
     * @return maximum id in use
     */
    public <V> int resetMaxID(HashMap<Integer, V> h){
        int max=0;
        for (int i: h.keySet()){
            if (i > max){
                max=i;
            }
        }
        return max;
    }

    /**
     * adds a data node to this collection
     *
     * @param n data node to add
     * @throws DuplicateKeyException if provided id is not unique
     */
    public void add(IndexedObject n) throws DuplicateKeyException {
        if (get(n.getID()) != null){
            throw new DuplicateKeyException("Key "+n.getID()+" already exists.");
        }

        // save the max ID
        registerID(n.getID());

        coll.put(n.getID(), n);
    }

    /**
     * delete a data node by integer id
     *
     * @param id id of node to delete
     */
    public void del(Integer id){
        coll.remove(id);
        resetMaxID(coll);
    }

    /**
     * delete data node by node pointer
     *
     * @param n data Node to delete from collection
     */
    public void del(Node n){
        coll.remove(n.getID());
        resetMaxID(coll);
    }


    /**
     * get data node by ID
     *
     * @param id id of data Node to find
     * @return found data Node or null if not found
     */
    public IndexedObject get(int id){
        return coll.get(id);
    }

    /**
     * return collection of nodes for json output
     *
     * @return  collection of data nodes
     */
    @JsonIgnore
    public Collection<IndexedObject> getData(){
        return coll.values();
    }


    /**
     * get current maximum ID utilized in this collection
     *
     * @return current maximum ID utilized in this collection
     */
    @JsonIgnore
    public int getCurrentID(){
        return maxID;
    }

    /**
     * registers the provided ID as the maximum ID utilized in this collection (if greater than the current one)
     * @param id id to register
     */
    public void registerID(int id){
        if (id > maxID){
            maxID=id;
        }
    }


}
