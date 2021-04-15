/**
 * RouteMap plots the stops on the map to visualize the nodes.
 *
 * @author Kevin Dolan
 * @version 1.0
 */
package main.java.memoranda.ui;

import main.java.memoranda.Coordinate;
import main.java.memoranda.Node;
import main.java.memoranda.NodeColl;
import main.java.memoranda.NodeMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;

public class RouteMap extends JPanel {
    private NodeColl nodes;
    private List<RouteStop> stops;
    private int id;

    /**
     * Constructor for RouteMap.
     */
    public RouteMap() {
        id = 1;
        nodes = new NodeColl();
        stops = new ArrayList<>();

        RouteStop s1 = new RouteStop(1, createPoint(100, 100));
        RouteStop s2 = new RouteStop(2, createPoint(200, 110));
        RouteStop s3 = new RouteStop(3, createPoint(300, 250));
        RouteStop s4 = new RouteStop(4, createPoint(400, 10));
        RouteStop s5 = new RouteStop(5, createPoint(500, 700));

        addStop(stops, s1.getBusStop());
        addStop(stops, s2.getBusStop());
        addStop(stops, s3.getBusStop());
        addStop(stops, s4.getBusStop());
        addStop(stops, s5.getBusStop());

        setPreferredSize(new Dimension(1000, 1000));
        setBackground(Color.WHITE);
    }

    /**
     * Getter for stops list.
     *
     * @return
     */
    public List<RouteStop> getStops() {
        return stops;
    }

    /**
     * Getter for id.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for ID.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    /**
     * paintComponent draws the graphics to JPanal.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < stops.size(); i++) {
            if (i < stops.size() - 1) {
                stops.get(i).drawConnection(g, stops.get(i).getBusStop(),
                        stops.get(i + 1).getBusStop());
            }
        }
        for (RouteStop s : stops) {
            s.drawStop(g);
        }

    }

    /**
     * populateStops retrieves stops from the NodeCollection and stores
     * them in the list for mapping.
     *
     * @param nodeCollection
     */
    public void populateStops(NodeColl nodeCollection) {
        NodeMapper mapper = new NodeMapper(nodeCollection);

        while (nodeCollection.iterator().hasNext()) {
            for (Node n : nodeCollection) {
                addStop(stops, mapper.getScaled(n));
            }
        }
    }

    /**
     * addStop adds a stop to the list for mapping.
     *
     * @param list
     * @param point
     */
    public void addStop(List<RouteStop> list, Point2D point) {
        list.add(new RouteStop(getId(), point));
        setId(getId() + 1);
        repaint();
    }

    /**
     * createPoint is a utility method for testing. It creates a Point2D point
     * from x and y coordinates.
     *
     * @param x
     * @param y
     * @return
     */
    public Point2D createPoint(double x, double y) {
        Point2D p = new Point2D.Double(x, y);
        return p;
    }

    /**
     * buildStopList is a utility method for testing. It loads data into
     * the stops list.
     *
     * @param stopList
     */
    public void buildStopList(List<RouteStop> stopList) {
        stops.clear();
        if (stopList != null) {
            for (int i = 0; i < stopList.size(); i++) {
                getStops().add(i, stopList.get(i));
            }
        }
    }

    /**
     * createNode is a utility method for testing. it creates a node.
     *
     * @param lat
     * @param lon
     * @return
     */
    public Node createNode(double lat, double lon) {
        Node n = new Node(1);
        n.setName("1");
        Coordinate c = new Coordinate(lat, lon);
        n.setCoords(c);
        return n;
    }
}