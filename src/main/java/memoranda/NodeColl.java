package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.DataCollection;
import main.java.memoranda.util.DuplicateKeyException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


/**
 *
 */
public class NodeColl extends DataCollection implements Iterable<Node>{
    private HashMap<Integer,Node> nodeList;

//    @JsonIgnore
//    private int maxID=0;

    /**
     * create a new node collection
     */
    public NodeColl(){
        nodeList=new HashMap<>();
    }

    /**
     * add an entire collection of nodes (post json import)
     *
     * @param c
     */
    public NodeColl(Collection<Node> c) throws DuplicateKeyException{
        this();
        for (Node n:c){
            addNode(n);
        }
    }

    /**
     * add a node
     * @param n
     */
    public void addNode(Node n) throws DuplicateKeyException {
        if (getNode(n.getId()) != null){
            throw new DuplicateKeyException("Key "+n.getId()+" already exists.");
        }

        // save the max ID
        registerID(n.getId());

        nodeList.put(n.getId(), n);
    }

    /**
     *
     * @param name
     * @param lat
     * @param lon
     */
    public void createNode(String name, double lat, double lon) throws DuplicateKeyException {
        addNode(new Node(getUniqueID(), name, lat, lon));
    }
    /**
     * Returns a unique key to be used in Node construction
     *
     * @return
     */
//    public int getUniqueID(){
//        return ++maxID;
//    }

    /**
     *
     * @return
     */
//    private int getMaxID(){
//        int max=0;
//        for (int i: nodeList.keySet()){
//            if (i > max){
//                max=i;
//            }
//        }
//        return max;
//    }

    /**
     * delete node by id
     *
     * @param id
     * @return
     */
    public Node delNode(Integer id){
        Node n=nodeList.remove(id);
        maxID=findMaxID();
        return n;
    }

    /**
     * delete node by node pointer
     *
     * @param n
     * @return
     */
    public Node delNode(Node n){
        Node node=nodeList.remove(n.getId());
        maxID=findMaxID();
        return node;
    }

    /**
     * get node by ID
     * @param id
     * @return
     */
    public Node getNode(int id){
        return nodeList.get(id);
    }

    /**
     * return collection of nodes for json output
     * @return
     */
    @JsonProperty
    public Collection<Node> getNodes(){
        return nodeList.values();
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
            coll=nodeList.values();
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
