package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Iterator;

import main.java.memoranda.util.DuplicateKeyException;


/**
 * NodeColl object holding a collection of nodes in the MTB scheduling system.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class NodeColl extends DataCollection<Node> implements Iterable<Node> {

    /**
     * create a new node collection.
     */
    public NodeColl() {
        super();
    }

    /**
     * add an entire collection of nodes (post json import).
     *
     * @param c collection of Nodes to add to collection
     * @throws DuplicateKeyException if a provided Node id is not unique
     */
    public NodeColl(Collection<Node> c) throws DuplicateKeyException {
        this();
        if (c != null) {
            for (Node n : c) {
                add(n);
            }
        }
    }


    /**
     * Returns a new collection item with a unique key.
     *
     * @return new Node
     */
    @Override
    public Node newItem() {
        return new Node(getUniqueID());
    }


    /**
     * get node by ID.
     *
     * @param id id of the node to get
     * @return Node if found, null otherwise
     */
    public Node get(int id) {
        return (Node) super.get(id);
    }

    /**
     * return a collection of all nodes in this collection.
     *
     * @return All nodes in this collection, null otherwise
     */
    @JsonProperty
    public Collection<IndexedObject> getNodes() {
        return getData();
    }

    /**
     * iterator.
     *
     * @return Iterator of Node type
     */
    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator();
    }

    /**
     * Defines how to Iterate over the collection.
     */
    public class NodeIterator<NodeT> implements Iterator<Node> {
        Collection coll;
        Iterator<Node> it;

        public NodeIterator() {
            coll = getData();
            it = coll.iterator();
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
