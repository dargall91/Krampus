package main.java.memoranda;

import java.awt.*;

/**
 * NodeMapper class
 *
 * Maps nodes (coordinates) to a given plotting window size
 *
 * @author Brian Pape
 * @version 2021-04-06
 */

public class NodeMapper {
    private static final int MAP_DEFAULT_WIDTH = 640;
    private static final int MAP_DEFAULT_HEIGHT = 480;

    private Dimension dim;
    private NodeColl nodeColl;
    private Coordinate origin;
    private Coordinate outlier;
    private Scale scale;

    /**
     * @param nodeColl
     */
    public NodeMapper(NodeColl nodeColl) {
        if (nodeColl.size() ==0){
            throw new IllegalStateException("Cannot operate on empty node list");
        }
        this.nodeColl = nodeColl;
        findBaseline();
        findOutlier();
        setMapSize(new Dimension(MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT));
        System.out.println("origin="+origin);
        System.out.println("outlier="+outlier);
    }

    private class Scale {
        double latScale, lonScale;

        Scale(double lonScale, double latScale) {
            this.latScale = latScale;
            this.lonScale = lonScale;
        }

        public double getLatScale() {
            return latScale;
        }

        public double getLonScale() {
            return lonScale;
        }
        public String toString(){return latScale+","+lonScale;}
    }

    /**
     * sets scaling factor for this plotting window
     *
     * @param dim dimension to scale to
     */
    private void setScale(Dimension dim) {
        scale = new Scale(dim.getWidth() / origin.lonDelta(outlier),
                dim.getHeight() / origin.latDelta(outlier));
    }

    /**
     * Set the map size for calculations. Note that scales are 0-based, so a map size of 1000x1000 results
     * in a return range of 0-999 x 0-999
     *
     * @param dim dimensions
     */
    public void setMapSize(Dimension dim) {
        dim.setSize(dim.getWidth()-1,dim.getHeight()-1);
        if (dim.getWidth()<0 || dim.getHeight() < 0){
            throw new IllegalArgumentException("Dimensions must be positive.");
        }
        this.dim = dim;
        setScale(dim);
    }

    /**
     * the node collection associated with this mapper object.
     *
     * @return node collection
     */
    public NodeColl getNodeColl() {
        return nodeColl;
    }

    /**
     * get the point scaled to the specified map size. Allows passing arbitrary nodes for scaling
     * as long as they are within the scale window calculated from the original nodes passed to
     * this object's constructor.
     *
     * @param n node
     * @return scaled Point
     */
    public Point getScaled(Node n) throws IllegalArgumentException, NullPointerException{
        int lat, lon;

        if (! inRange(n.getCoords())){
            throw new IllegalArgumentException("Node is not in range of provided node collection");
        }

        lat = (int) (origin.latDelta(n.getCoords()) * scale.getLatScale());
        lon = (int) (origin.lonDelta(n.getCoords()) * scale.getLonScale());
        return new Point(lon, lat);
    }


    /**
     *
     * @param coord
     * @return
     */
    private boolean inRange(Coordinate coord){
        if ( (coord.getLon() >= origin.getLon() && coord.getLon() <= outlier.getLon() )
            && (coord.getLat() >= origin.getLat() && coord.getLat() <= outlier.getLat()) ) {
            return true;
        };
        return false;
    }

    /**
     *
     */
    private void findBaseline() {
        double lat = Coordinate.latMax;
        double lon = Coordinate.lonMax;
        for (Node n : nodeColl) {
            if (n.getLat() < lat) {
                lat = n.getLat();
            }
            if (n.getLon() < lon) {
                lon = n.getLon();
            }
        }
        origin = new Coordinate(lat, lon);
    }

    /**
     *
     */
    private void findOutlier() {
        double lat = Coordinate.latMin;
        double lon = Coordinate.lonMin;
        for (Node n : nodeColl) {
            if (n.getLat() > lat) {
                lat = n.getLat();
            }
            if (n.getLon() > lon) {
                lon = n.getLon();
            }
        }
        outlier = new Coordinate(lat, lon);
    }
}
