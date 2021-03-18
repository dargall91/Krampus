package main.java.memoranda;

// supports up to 12 significant digits, 9 after decimal point
public class Coordinate {
    private Double latitude;
    private Double longitude;

    public Coordinate(Double latitude, Double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public Double getLatitude(){
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double distanceTo(Coordinate c){
        // using cartesian distance formula (not accounting for spheroid characteristics)
        return Math.sqrt(Math.pow(c.getLatitude()-latitude, 2) + Math.pow(c.getLongitude()-longitude, 2);
    }

    public Double latitudeDelta(Coordinate c){
        return Math.abs(c.getLatitude()-latitude);
    }

    public Double longitudeDelta(Coordinate c){
        return Math.abs(c.getLongitude()-longitude);
    }

    public boolean northOf(Coordinate c){
        return latitude-c.latitude > 0;
    }

    public boolean eastOf(Coordinate c){
        return false;
    }

    public boolean southOf(Coordinate c){
        return latitude-c.latitude < 0;
    }

    public boolean westOf(Coordinate c){
        return false;
    }

    @Override
    public boolean equals(Object o){
        Integer sigDigits=9;
        Coordinate c=(Coordinate) o;
        String lat1=((Double)(latitude*sigDigits)).toString().split(".")[0];
        String lat2=((Double)(c.latitude*sigDigits)).toString().split(".")[0];
        String long1=((Double)(latitude*sigDigits)).toString().split(".")[0];
        String long2=((Double)(c.latitude*sigDigits)).toString().split(".")[0];
        return (this == o) || (lat1.equals(lat2) && long1.equals(long2));
    }
}
