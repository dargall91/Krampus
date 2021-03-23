package main;

import java.util.HashMap;

public abstract class DataCollection {
    private int maxID=0;

    /**
     * Returns a unique key to be used in Node construction
     *
     * @return
     */
    public int getUniqueID(){
        return ++maxID;
    }

    /**
     *
     * @return
     */
    public <V> int findMaxID(HashMap<Integer, V> h){
        int max=0;
        for (int i: h.keySet()){
            if (i > max){
                max=i;
            }
        }
        return max;
    }

    public int getCurrentID(){
        return maxID;
    }

    public void registerID(int id){
        if (id > maxID){
            maxID=id;
        }
    }
}
