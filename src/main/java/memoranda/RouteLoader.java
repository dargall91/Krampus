package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

public class RouteLoader extends IndexedObject{
        private String name;
        private LinkedList<Integer> nodeIDs;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public RouteLoader(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("nodeIDs") LinkedList<Integer> nodes){
            super(id);
            this.name=name;
            this.nodeIDs=nodes;
        }

        public String getName(){
            return name;
        }

        public LinkedList<Integer> getNodes(){
            return nodeIDs;
        }

        public String toString(){
            return "route "+getId()+" "+getName();
        }

}
