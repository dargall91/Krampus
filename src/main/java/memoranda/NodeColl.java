package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


/**
 *
 */
public class NodeColl implements Iterable<Node>{
    private HashMap<Integer,Node> nodeList;

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
    public NodeColl(Collection<Node> c){
        this();
        for (Node n:c){
            addNode(n);
        }
    }

    /**
     * add a node
     * @param n
     */
    public void addNode(Node n){
        nodeList.put(n.getId(), n);
    }

    /**
     * delete node by id
     *
     * @param id
     * @return
     */
    public Node delNode(Integer id){
        return nodeList.remove(id);
    }

    /**
     * delete node by node pointer
     *
     * @param n
     * @return
     */
    public Node delNode(Node n){
        return nodeList.remove(n.getId());
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
