package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.traversal.NodeIterator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


// need Route class which will hold methods such as closest()

public class NodeColl implements Iterable<Node>{
    private HashMap<Integer,Node> nodeList;

    public NodeColl(){
        nodeList=new HashMap<>();
    }

    public NodeColl(Collection<Node> c){
        this();
        for (Node n:c){
            addNode(n);
        }
    }

    public void addNode(Node n){
        nodeList.put(n.getId(), n);
    }
    public Node delNode(Integer id){
        return nodeList.remove(id);
    }
    public Node delNode(Node n){
        return nodeList.remove(n.getId());
    }
    public Node getNode(int id){
        return nodeList.get(id);
    }

    @JsonProperty
    public Collection<Node> getNodes(){
        return nodeList.values();
    }

    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator();
    }

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
