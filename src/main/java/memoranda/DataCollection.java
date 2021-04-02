package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.HashMap;

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
     * @param c
     */
    public DataCollection(Collection<Node> c) throws DuplicateKeyException{
        this();
        for (Node n:c){
            add(n);
        }
    }


    /**
     *
     * @return
     */
    public abstract T newItem();


    /**
     *
     * @param o
     * @throws DuplicateKeyException
     */
//    public abstract void createUnique(T o) throws DuplicateKeyException;


        /**
         * Returns a unique key to be used in Node construction
         *
         * @return
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
     * @return
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

    public void add(IndexedObject n) throws DuplicateKeyException {
        if (get(n.getId()) != null){
            throw new DuplicateKeyException("Key "+n.getId()+" already exists.");
        }

        // save the max ID
        registerID(n.getId());

        coll.put(n.getId(), n);
    }

    /**
     *
     * @param id
     */
    public void del(Integer id){
        coll.remove(id);
        resetMaxID(coll);
    }

    /**
     * delete node by node pointer
     *
     * @param n
     * @return
     */
    public void del(Node n){
        coll.remove(n.getId());
        resetMaxID(coll);
    }


    /**
     * get node by ID
     * @param id
     * @return
     */
    public IndexedObject get(int id){
        return coll.get(id);
    }

    /**
     * return collection of nodes for json output
     * @return
     */
    @JsonIgnore
    public Collection<IndexedObject> getData(){
        return coll.values();
    }


    @JsonIgnore
    public int getCurrentID(){
        return maxID;
    }

    public void registerID(int id){
        if (id > maxID){
            maxID=id;
        }
    }


}
