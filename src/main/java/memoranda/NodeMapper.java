package main.java.memoranda;

import java.awt.*;

/**
 * NodeMapper class.
 * <p>
 * Maps nodes (coordinates) to a given plotting window size Implement scale object to allow for easy
 * Cartesian plotting of map coordinates assuming an ideal Mercator projection.
 * </p>
 *
 * @author Brian Pape
 * @version 2021-04-06
 */

public class NodeMapper {
    private static final int MAP_DEFAULT_WIDTH = 640;
    private static final int MAP_DEFAULT_HEIGHT = 480;
    private final NodeColl nodeColl;
    private Dimension dim;
    private Dimension mapSize;
    private Coordinate origin;
    private Coordinate outlier;
    private Insets inset;
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

        // set default inset and map size
        inset = new Insets(0, 0, 0, 0);
        setMapSize(new Dimension(MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT));
    }

    /**
     * sets scaling factor for this plotting window.
     */
    private void setScale() {
        int insetWidth = inset.left + inset.right;
        int insetHeight = inset.top + inset.bottom;

        dim = mapSize.getSize();
        dim.setSize(mapSize.getWidth() - insetWidth - 1,
                mapSize.getHeight() - insetHeight - 1);

        scale = new Scale(dim.getWidth() / origin.lonDelta(outlier),
                dim.getHeight() / origin.latDelta(outlier));
    }

    /**
     * set insets for the map (blank border).
     *
     * @param inset insets to set
     */
    public void setInsets(Insets inset) {
        if ((inset.left + inset.right) >= dim.getWidth()
                || (inset.top + inset.bottom) >= dim.getHeight()) {
            throw new IllegalArgumentException("Insets cannot be greater than map dimensions.");
        }
        this.inset = inset;

        setScale();
    }

    /**
     * Set the map size for calculations. Note that scales are 0-based, so a map size of 1000x1000
     * results in a return range of 0-999 x 0-999.
     *
     * @param dim dimensions
     */
    public void setMapSize(Dimension dim) {
        if (dim.getWidth() < 1 || dim.getHeight() < 1) {
            throw new IllegalArgumentException("Dimensions must be positive.");
        }

        // set the map size...
        this.mapSize = dim.getSize();

        // and the working dimensions, adjusting for inset, scale, etc.
        //this.dim = dim;
        //this.dim.setSize(mapSize.getWidth() - 1, mapSize.getHeight() - 1);

        setScale();

        //setScale(this.dim);
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
     * get the point scaled to the specified map size. Allows passing arbitrary nodes for scaling as
     * long as they are within the scale window calculated from the original nodes passed to this
     * object's constructor.
     *
     * @param n node
     * @return scaled Point
     * @throws IllegalArgumentException if the Node is not in range of the collection
     * @throws NullPointerException     if the node is null
     */
    public Point getScaled(Node n) throws IllegalArgumentException, NullPointerException {
        int lat;
        int lon;

        if (!inRange(n.getCoords())) {
            throw new IllegalArgumentException("Node is not in range of provided node collection");
        }

        lat = (int) (origin.latDelta(n.getCoords()) * scale.getLatScale()) + inset.top;
        lon = (int) (origin.lonDelta(n.getCoords()) * scale.getLonScale()) + inset.left;
        return new Point(lon, lat);
    }

    /**
     * returns a new node based upon its location on the configured map, taking insets into
     * account.
     *
     * <p>For instance, if you have a 1000x1000 map with insets of 10 on all sides, scaled nodes
     * using getScaled() will be in the range of 10-989 for both x and y (lon and lat).
     *
     * <p>If you pass (500,500) to newScaledNode, you will get a node with a latitude approximately
     * halfway between the norther- and southern-most nodes in the node collection, and a longitude
     * approximately halfway between the western- and eastern-most nodes in the node collection.
     *
     * @param n a new to populate with new coordinates.
     * @param p the point on the map (e.g. 500, 500) to create a new node from.
     * @return the modified node
     */
    public Node newScaledNode(Node n, Point p) {
        if (p.getX() + inset.left + inset.right >= mapSize.getWidth()
                || p.getY() + inset.top + inset.bottom >= mapSize.getHeight()) {
            throw new IllegalArgumentException("Point out of range of map size.");
        }
        double newLon = ((p.getX() - inset.left) / scale.getLonScale() + origin.getLon());
        double newLat = ((p.getY() - inset.top) / scale.getLatScale() + origin.getLat());
        n.setCoords(new Coordinate(newLat, newLon));
        return n;
    }

    /**
     * Check to see if provided coordinate is in range of this mapper's NodeColl.
     *
     * @param coord coordinate to check
     * @return true if coord is in domain/range of this mapper's NodeColl
     */
    private boolean inRange(Coordinate coord) {
        return (coord.getLon() >= origin.getLon() && coord.getLon() <= outlier.getLon())
                && (coord.getLat() >= origin.getLat() && coord.getLat() <= outlier.getLat());
    }

    /**
     * Find the farthest "west" and "north" nodes. Based on 0,-180 (lat/lon) being west
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
     * Find the farthest "east" and "south" nodes. Based on 0,-180 (lat/lon) being west
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

    private static class Scale {
        double latScale;
        double lonScale;

        Scale(double lonScale, double latScale) {
            this.latScale = latScale;
            this.lonScale = lonScale;
        }

        /**
         * Gets the Latitude Scale.
         *
         * @return the latitude scale
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

        /**
         * standard toString.
         *
         * @return string rep
         */
        @Override
        public String toString() {
            return latScale + "," + lonScale;
        }
    }
}
