package main.java.memoranda.util;

public class DuplicateKeyException extends Exception{
    String msg;
    public DuplicateKeyException(String msg){
        this.msg=msg;
    }
    @Override
    public String toString(){
        return "Duplicate Key Exception ("+msg+")";
    }
}
