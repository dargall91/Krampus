package main.java.memoranda;

public abstract class IndexedObject {
    private Integer id;


    IndexedObject(Integer id){
        this.id=id;
    }

    public int getId(){
        return id;
    }

    public abstract String toString();
}
