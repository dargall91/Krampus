package main.java.memoranda;

/**
 * IndexedObject represents an object with a unique integer ID, used for json serialization and
 * deserialization.
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public abstract class IndexedObject {
    private final Integer id;


    /**
     * create a new object with given integer id.
     *
     * @param id id for new object
     */
    IndexedObject(Integer id) {
        this.id = id;
    }

    /**
     * id getter.
     *
     * @return id
     */
    public int getID() {
        return id;
    }

    /**
     * standard toString.
     *
     * @return String repr of obj
     */
    public abstract String toString();
}
