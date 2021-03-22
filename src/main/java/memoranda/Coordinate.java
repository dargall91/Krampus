package main.java.memoranda;

// supports up to 12 significant digits, 9 after decimal point
public class Coordinate {
    private Double lat;
    private Double lon;

    public Coordinate(Double lat, Double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat(){
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Double distanceTo(Coordinate c){
        // using cartesian distance formula (not accounting for spheroid characteristics)
        return Math.sqrt(Math.pow(c.getLat()- lat, 2) + Math.pow(c.getLon()- lon, 2));
    }

    public Double latitudeDelta(Coordinate c){
        return Math.abs(c.getLat()- lat);
    }

    public Double longitudeDelta(Coordinate c){
        return Math.abs(c.getLon()- lon);
    }

    public boolean northOf(Coordinate c){
        return lat -c.lat > 0;
    }

    public boolean eastOf(Coordinate c){
        return false;
    }

    public boolean southOf(Coordinate c){
        return lat -c.lat < 0;
    }

    public boolean westOf(Coordinate c){
        return false;
    }

    @Override
    public boolean equals(Object o){
        Integer sigDigits=9;
        Coordinate c=(Coordinate) o;
        String lat1=((Double)(lat *sigDigits)).toString().split(".")[0];
        String lat2=((Double)(c.lat *sigDigits)).toString().split(".")[0];
        String long1=((Double)(lat *sigDigits)).toString().split(".")[0];
        String long2=((Double)(c.lat *sigDigits)).toString().split(".")[0];
        return (this == o) || (lat1.equals(lat2) && long1.equals(long2));
    }
}
