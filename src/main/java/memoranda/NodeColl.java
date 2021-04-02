package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 */
public class NodeColl extends DataCollection<Node> implements Iterable<Node>{

    /**
     * create a new node collection
     */
    public NodeColl(){
        super();
    }

    /**
     * add an entire collection of nodes (post json import)
     *
     * @param c
     */
    public NodeColl(Collection<Node> c) throws DuplicateKeyException{
        this();
        for (Node n:c){
            add(n);
        }
    }


    @Override
    public Node newItem(){
        return new Node(getUniqueID());
    }


     /**
     * Creates a new Node object with a unique ID
     *
     * @param n
     * @throws DuplicateKeyException
     */
    @Override
    public void createUnique(Node n) throws DuplicateKeyException {
        add(new Node(getUniqueID(), n.getName(), n.getLat(), n.getLon()));
    }

    /**
     * get node by ID
     * @param id
     * @return
     */
    public Node get(int id){
        return (Node)super.get(id);
    }

    /**
     *
     * @return
     */
    @JsonProperty
    public Collection<IndexedObject> getNodes(){
        return getData();
    }

    /**
     * iterator
     * @return
     */
    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator();
    }

    /**
     * iterator
     * @param <Node>
     */
    public class NodeIterator<Node> implements Iterator<Node>{
        Collection coll;
        Iterator<Node> it;
        public NodeIterator(){
            coll=getData();
            it=coll.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Node next() {
            return it.next();
        }
    }
}
