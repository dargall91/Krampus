package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Driver extends IndexedObject {
//    int id;
    String name;
    String phoneNumber;


    /**
     *
     * @param id
     * @param name
     * @param phoneNumber
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Driver(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("phoneNumber")
                  String phoneNumber){
        super(id);
        this.name=name;
        this.phoneNumber=phoneNumber;
    }


    public String getName(){
        return name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    @Override
    public String toString(){
        return "Driver "+getId()+":'"+name+"','"+phoneNumber+"'";
    }

}


