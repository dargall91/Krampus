package main.java.memoranda;

// supports up to 12 significant digits, 9 after decimal point
// expects coordinates in decimal degrees, - longitudes are west of the prime meridian, -latitudes are south of the equator
public class Coordinate {
    private Double lat;
    private Double lon;

    /**
     * immutable object; must use this constructor
     * @param lat
     * @param lon
     */
    public Coordinate(Double lat, Double lon){
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 decimal degrees, inclusive.");
        } else if (lon < -180 || lon > 180){
            throw new IllegalArgumentException("Longitude must be between -180 and 180 decimal degrees, inclusive.");
        }
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * standard getter for latitude
     * @return
     */
    public Double getLat(){
        return lat;
    }

    /**
     * standard getter for longitude
     * @return
     */
    public Double getLon() {
        return lon;
    }


    /**
     * Haversine formula for calculating the great-circle distance between two points on an idealized spheroid (Earth)
     *
     * Formula pulled from wikipedia and cross-checked with several other sources.
     *
     * @param lat1 latitude in decimal degrees
     * @param lat2 latitude in decimal degrees
     * @param lon1 longitude in decimal degrees
     * @param lon2 longitude in decimal degrees
     */
    private Double haversine(double lat1, double lat2, double lon1, double lon2){
        lat1=deg2rad(lat1);
        lat2=deg2rad(lat2);
        lon1=deg2rad(lon1);
        lon2=deg2rad(lon2);

        // use 6371km for avg radius of Earth
        double r=6371;

        return 2*r*Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin((lat2-lat1)/2), 2)
                        + Math.cos(lat1) * Math.cos(lat2) *
                        Math.pow(Math.sin((lon2-lon1)/2), 2)
                )
        );
    }



    /**
     * return distance in km to another coordinate using Haversine formula
     * @param c
     * @return
     */
    public Double distanceTo(Coordinate c){
        return haversine(lat, c.getLat(), lon, c.getLon());
    }

    /**
     * convert degrees to radians for haversine formula
     * @param deg
     * @return
     */
    private double deg2rad(double deg){
        return deg/360*2*Math.PI;
    }

    /**
     * Get difference in latitudes in degrees
     * @param c
     * @return
     */
    public Double latitudeDelta(Coordinate c){
        return Math.abs(c.getLat()- lat);
    }

    /**
     * get difference in longitudes in degrees
     * @param c
     * @return
     */
    public Double longitudeDelta(Coordinate c){
        return Math.abs(c.getLon()- lon);
    }

    /**
     * whether this coordinate is north of coordinate c
     * @param c
     * @return
     */
    public boolean northOf(Coordinate c){
        return lat -c.lat > 0;
    }

    /**
     * whether this coordinate is east of coordinate c
     * ** UNIMPLEMENTED **
     * @param c
     * @return
     */
//    public boolean eastOf(Coordinate c){
//        return false;
//    }

    /**
     * whether this coordinate is south of coordinate c
     * @param c
     * @return
     */
    public boolean southOf(Coordinate c){
        return lat -c.lat < 0;
    }

    /**
     * whether this coordinate is west of coordinate c
     * ** UNIMPLEMENTED **
     *
     * @param c
     * @return
     */
//    public boolean westOf(Coordinate c){
//        return false;
//    }

    @Override
    public String toString(){
        return lat+","+lon;
    }

    /**
     * check for equality to 9 decimal places
     * @param o
     * @return
     */
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
