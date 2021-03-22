package main.java.memoranda;

import org.w3c.dom.traversal.NodeIterator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


// need Route class which will hold methods such as closest()

public class NodeColl implements Iterable<Node>{
    private HashMap<Integer,Node> nodeList;

    public NodeColl(){
        nodeList=new HashMap<>();
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

    public HashMap<Integer, Node> getNodeList(){
        return nodeList;
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
