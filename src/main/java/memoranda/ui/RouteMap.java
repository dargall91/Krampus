/**
 * RouteMap plots the stops on the map to visualize the nodes.
 *
 * @author Kevin Dolan, John Thurstonson
 * @version 2021-04-25
 */
package main.java.memoranda.ui;

import main.java.memoranda.Coordinate;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.Node;
import main.java.memoranda.NodeMapper;
import main.java.memoranda.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RouteMap extends JPanel {
    //private NodeColl nodes;
    private List<RouteStop> stops;
    private int id;
    private Route route;
    private RouteMapPanel parentPanel;
    
    /**
     * Constructor for TESTING ONLY.
     */
    public RouteMap() {
        id = 1;
        //nodes = new NodeColl();
        stops = new ArrayList<>();
    }

    /**
     * Constructor for RouteMap.
     *
     * @param parentPanel
     */
    public RouteMap(RouteMapPanel parentPanel) {
        this.parentPanel = parentPanel;
        
        initMap();
    }
    
    /**
     * Initiate RouteMap.
     */
    private void initMap() {
        id = 1;
        //nodes = new NodeColl();
        stops = new ArrayList<>();
        
        if (parentPanel.getRouteTable().getRoute() != null) {
            route = parentPanel.getRouteTable().getRoute();
        } else {
            return;
        }

        setPreferredSize(new Dimension(1000, 1000));
        setBackground(Color.WHITE);
        
        populateStops(route.getRoute());
    }
    
    /**
     * Refresh RouteMap.
     */
    public void refresh() {
        initMap();
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
     * populateStops retrieves stops from the Route and stores
     * them in the list for mapping.
     * Updated to accept linked list of nodes
     *
     * @param nodes
     */
    public void populateStops(LinkedList<Node> nodes) {
        NodeMapper mapper = new NodeMapper(CurrentProject.getNodeColl());
        
        for (Node n : nodes) {
            addStop(stops, mapper.getScaled(n), n.getName());
        }

        //while (nodeCollection.iterator().hasNext()) {
        //    for (Node n : nodeCollection) {
        //        System.out.println("adding stops");
        //        addStop(stops, mapper.getScaled(n));
        //    }
        //}
    }

    /**
     * addStop adds a stop to the list for mapping.
     *
     * @param list
     * @param point
     * @param name
     */
    public void addStop(List<RouteStop> list, Point2D point, String name) {
        list.add(new RouteStop(getId(), point, name));
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