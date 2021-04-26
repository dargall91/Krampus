package main.java.memoranda;

import java.awt.*;

/**
 * NodeMapper class.
 * <p>
 *      Maps nodes (coordinates) to a given plotting window size Implement scale object to allow
 *      for easy Cartesian plotting of map coordinates assuming an ideal Mercator projection.
 * </p>
 * 
 * @author Brian Pape
 * @version 2021-04-06
 */

public class NodeMapper {
    private static final int MAP_DEFAULT_WIDTH = 640;
    private static final int MAP_DEFAULT_HEIGHT = 480;

    private Dimension dim;
    private final NodeColl nodeColl;
    private Coordinate origin;
    private Coordinate outlier;
    private Scale scale;

    /**
     * Creates a new NodeMapper from a NodeCollection.
     * 
     * @param nodeColl the NodeCollection
     */
    public NodeMapper(NodeColl nodeColl) {
        if (nodeColl.size() == 0) {
            throw new IllegalStateException("Cannot operate on empty node list");
        }
        this.nodeColl = nodeColl;
        findBaseline();
        findOutlier();
        setMapSize(new Dimension(MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT));
    }

    private static class Scale {
        double latScale;
        double lonScale;

        Scale(double lonScale, double latScale) {
            this.latScale = latScale;
            this.lonScale = lonScale;
        }

        /**
         * Gets the Lateral Scale.
         * 
         * @return the lateral scale
         */
        public double getLatScale() {
            return latScale;
        }

        /**
         * Gets the Longitude Scale.
         * 
         * @return the longitude scale
         */
        public double getLonScale() {
            return lonScale;
        }

        @Override
        public String toString() {
            return latScale + "," + lonScale;
        }
    }

    /**
     * sets scaling factor for this plotting window.
     *
     * @param dim dimension to scale to
     */
    private void setScale(Dimension dim) {
        scale = new Scale(dim.getWidth() / origin.lonDelta(outlier),
                dim.getHeight() / origin.latDelta(outlier));
    }

    /**
     * Set the map size for calculations. Note that scales are 0-based, so a map size of
     * 1000x1000 results in a return range of 0-999 x 0-999.
     *
     * @param dim dimensions
     */
    public void setMapSize(Dimension dim) {
        if (dim.getWidth() < 1 || dim.getHeight() < 1) {
            throw new IllegalArgumentException("Dimensions must be positive.");
        }
        dim.setSize(dim.getWidth() - 1, dim.getHeight() - 1);
        this.dim = dim;
        setScale(this.dim);
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
     * @throws IllegalArgumentException if the Node is not in range of the collection
     * @throws NullPointerException if the node is null
     */
    public Point getScaled(Node n) throws IllegalArgumentException, NullPointerException {
        int lat;
        int lon;

        if (!inRange(n.getCoords())) {
            throw new IllegalArgumentException("Node is not in range of provided node collection");
        }

        //Gives 10 buffer from left and top of map.
        lat = (int) (origin.latDelta(n.getCoords()) * scale.getLatScale()) + 10;
        lon = (int) (origin.lonDelta(n.getCoords()) * scale.getLonScale()) + 10;
        return new Point(lon, lat);
    }


    /**
     * Check to see if provided coordinate is in range of this mapper's NodeColl.
     *
     * @param coord coordinate to check
     * @return true if coord is in domain/range of this mapper's NodeColl
     */
    private boolean inRange(Coordinate coord) {
        if ((coord.getLon() >= origin.getLon() && coord.getLon() <= outlier.getLon())
                && (coord.getLat() >= origin.getLat() && coord.getLat() <= outlier.getLat())) {
            return true;
        }
        return false;
    }

    /**
     * Find the farthest "west" and "north" nodes.
     * Based on 0,-180 (lat/lon) being west
     */
    private void findBaseline() {
        double lat = Coordinate.LAT_MAX;
        double lon = Coordinate.LON_MAX;
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
     * Find the farthest "east" and "south" nodes.
     * Based on 0,-180 (lat/lon) being west
     */
    private void findOutlier() {
        double lat = Coordinate.LAT_MIN;
        double lon = Coordinate.LON_MIN;
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
