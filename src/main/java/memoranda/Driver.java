package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Driver {
    int id;
    String name;
    String phoneNumber;


    /**
     *
     */
    public Driver(){

    }

    /**
     *
     * @param id
     * @param name
     * @param phoneNumber
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Driver(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("phoneNumber")
                  String phoneNumber){
        this.id=id;
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    public int getId(){
        return id;
    }

}


