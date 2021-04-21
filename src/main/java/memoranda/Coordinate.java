package main.java.memoranda;

/**
 * Holds a coordinate (a location on Earth) and calculates distances between them.
 *
 * <p>supports up to 12 significant digits, 9 after decimal point
 * expects coordinates in decimal degrees, -longitudes are west of the prime meridian, -latitudes
 * are south of the equator
 *
 * @author Brian Pape
 * @version 2021-04-01
 */
public class Coordinate {
    private Double lat;
    private Double lon;
    public static final double LON_MIN = -180;
    public static final double LON_MAX = 180;
    public static final double LAT_MIN = -90;
    public static final double LAT_MAX = 90;

    /**
     * immutable object; must use this constructor.
     *
     * @param lat latitude of coordinate
     * @param lon longitude of coordinate
     */
    public Coordinate(Double lat, Double lon) {
        if (lat < LAT_MIN || lat > LAT_MAX) {
            throw new IllegalArgumentException("Latitude must be between " + LAT_MIN + " and "
                    + LAT_MAX + " decimal degrees, inclusive.");
        } else if (lon < LON_MIN || lon > LON_MAX) {
            throw new IllegalArgumentException("Longitude must be between " + LON_MIN + " and "
                    + LON_MAX + " decimal degrees, inclusive.");
        }
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * standard getter for latitude.
     *
     * @return latitude in decimal degrees
     */
    public Double getLat() {
        return lat;
    }

    /**
     * standard getter for longitude.
     *
     * @return in decimal degrees
     */
    public Double getLon() {
        return lon;
    }


    /**
     * Haversine formula for calculating the great-circle distance between two points on an
     * idealized spheroid (Earth).
     *
     * <p>Formula pulled from wikipedia and cross-checked with several other sources.
     *
     * @param lat1 latitude in decimal degrees
     * @param lat2 latitude in decimal degrees
     * @param lon1 longitude in decimal degrees
     * @param lon2 longitude in decimal degrees
     */
    private Double haversine(double lat1, double lat2, double lon1, double lon2) {
        lat1 = deg2rad(lat1);
        lat2 = deg2rad(lat2);
        lon1 = deg2rad(lon1);
        lon2 = deg2rad(lon2);

        // use 6371km for avg radius of Earth
        double r = 6371;

        return 2 * r * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin((lat2 - lat1) / 2), 2)
                                + Math.cos(lat1) * Math.cos(lat2)
                                * Math.pow(Math.sin((lon2 - lon1) / 2), 2)
                )
        );
    }


    /**
     * return distance in km to another coordinate using Haversine formula.
     *
     * @param c other coordinate
     * @return distance in km to other coordinate
     * @throws NullPointerException if the Coordinate is null
     */
    public Double distanceTo(Coordinate c) throws NullPointerException {
        return haversine(lat, c.getLat(), lon, c.getLon());
    }


    /**
     * convert degrees to radians for haversine formula.
     *
     * @param deg degrees to convert
     * @return degrees as radians
     */
    private double deg2rad(double deg) {
        return deg / 360 * 2 * Math.PI;
    }

    /**
     * Get difference in latitudes in degrees.
     *
     * @param c other coordinate
     * @return absolute distance to provided coordinate
     * @throws NullPointerException if the Coordinate is null
     */
    public Double latDelta(Coordinate c) throws NullPointerException {
        return Math.abs(c.getLat() - lat);
    }

    /**
     * get difference in longitudes in degrees.
     *
     * @param c other coordinate
     * @return absolute distance to provided coordinate
     * @throws NullPointerException if the Coordinate is null
     */
    public Double lonDelta(Coordinate c) throws NullPointerException {
        return Math.abs(c.getLon() - lon);
    }


    /**
     * standard toString().
     *
     * @return string repr of obj
     */
    @Override
    public String toString() {
        return lat + "," + lon;
    }

    /**
     * check for equality to 9 decimal places.
     *
     * @param o another coordinate for comparison
     * @return whether supplied coordinate is equal to this one
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o.getClass().equals(this.getClass())) {
            
        }
        
        Coordinate c = (Coordinate) o;
        String lat1 = lat.toString();
        String lat2 = c.lat.toString();
        String long1 = lat.toString();
        String long2 = c.lat.toString();
        return (this == o) || (lat1.equals(lat2) && long1.equals(long2));
    }
    
    /**
     * Override of hashCode for equals method.
     * 
     * @return an arbitrary constant
     */
    public int hashCode() {
        assert false : "hashCode not designed";
        return 0;
    }
}
