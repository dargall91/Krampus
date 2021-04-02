package main.java.memoranda;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class Bus extends IndexedObject{
    private int number;

    /**
     *
     * @param id
     */
    Bus(Integer id) {
        super(id);
    }


    /**
     *
     * @param id
     * @param number
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    Bus(@JsonProperty("id") Integer id, @JsonProperty("number") int number){
        this(id);
        this.number=number;
    }



    /**
     *
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     *
     * @return
     */
    public int getNumber() {
        return number;
    }


    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return "bus id "+getID()+" number "+getNumber();
    }
}
