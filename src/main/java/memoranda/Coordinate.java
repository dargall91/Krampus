package main.java.memoranda;

public class Coordinate {
    private double latitude;
    private double longitude;

    public Coordinate(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double distanceTo(Coordinate c){
        return 0.0;
    }

    public double latitudeDelta(Coordinate c){
        return 0;
    }

    public double longitudeDelta(Coordinate c){
        return 0;
    }

    public boolean northOf(Coordinate c){
        return false;
    }

    public boolean eastOf(Coordinate c){
        return false;
    }

    public boolean southOf(Coordinate c){
        return false;
    }

    public boolean westOf(Coordinate c){
        return false;
    }
}
