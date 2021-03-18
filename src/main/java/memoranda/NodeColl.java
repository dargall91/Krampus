package main.java.memoranda;

import java.util.HashMap;


// need Route class which will hold methods such as closest()

public class NodeColl {
    private HashMap<int,Node> nodeList;
    private HashMap<String,Node> nodeListByName;

    public Node getNode(int id){
        return nodeList.get(id);
    }
    public Node getNodeByName(String name){
        return nodeListByName.get(name);
    }
}
