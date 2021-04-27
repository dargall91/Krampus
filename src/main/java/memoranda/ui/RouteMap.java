package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import main.java.memoranda.*;
import main.java.memoranda.util.DuplicateKeyException;

/**
 * RouteMap plots the stops on the map to visualize the nodes.
 *
 * @author Kevin Dolan, John Thurstonson, Brian Pape
 * @version 2021-04-25
 */
public class RouteMap extends JPanel {
    private List<RouteStop> stops;
    private int id;
    private Route route;
    private RouteMapPanel parentPanel;
    private final NodeMapper nodeMapper;
    private Image defaultMap;

    /**
     * Constructor for TESTING ONLY.
     */
    public RouteMap() throws DuplicateKeyException {
        id = 1;
        stops = new ArrayList<>();

        nodeMapper = new NodeMapper(CurrentProject.getNodeColl());
    }

    /**
     * Constructor for RouteMap.
     *
     * @param parentPanel parent panel for this route map
     */
    public RouteMap(RouteMapPanel parentPanel) {
        this.parentPanel = parentPanel;

        this.addComponentListener(new ResizeListener());
        nodeMapper = new NodeMapper(CurrentProject.getNodeColl());

        try{
            defaultMap = ImageIO.read(new File("src/main/resources/ui/map_background.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        initMap();

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println(e.getPoint());
                NodeColl nodeColl = CurrentProject.getNodeColl();

                Node node = CurrentProject.getNodeColl().newItem();
                node.setName("New stop");
                node = nodeMapper.newScaledNode(node, e.getPoint());

                try {
                    nodeColl.add(node);
                    route.addNode(node);
                    CurrentProject.save();
                    refresh();
                } catch (DuplicateKeyException duplicateKeyException) {
                    duplicateKeyException.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }


    /**
     * adjust scaling on resize.
     */
    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            initMap();
        }
    }

    /**
     * Initiate RouteMap.
     */
    private void initMap() {
        id = 1;
        stops = new ArrayList<>();

        if (parentPanel.getRouteTable().getRoute() != null) {
            route = parentPanel.getRouteTable().getRoute();
        } else {
            //throw new IllegalArgumentException("Parent must own a route");
        }

        setPreferredSize(new Dimension(800, 600));

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
     * @return list of stops
     */
    public List<RouteStop> getStops() {
        return stops;
    }

    /**
     * Getter for id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for ID.
     *
     * @param id id to set
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * paintComponent draws the graphics to JPanel.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(defaultMap, 0,0, getWidth(), getHeight(), this);

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
     * populateStops retrieves stops from the Route and stores them in the list for mapping. Updated to accept linked
     * list of nodes
     *
     * @param nodes list od noes to populate
     */
    public void populateStops(LinkedList<Node> nodes) {

        Dimension dim = this.getSize();
        if (!dim.equals(new Dimension(0, 0))) {
            nodeMapper.setMapSize(dim);
        }

        nodeMapper.setInsets(new Insets(40, 40, 40, 40));

        for (Node n : nodes) {
            addStop(stops, nodeMapper.getScaled(n), n.getName());
        }
        repaint();

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
     * @param list  list of routes
     * @param point point to add
     * @param name  name of stop
     */
    public void addStop(List<RouteStop> list, Point2D point, String name) {
        list.add(new RouteStop(getId(), point, name));
        setId(getId() + 1);
        //repaint();
    }

    /**
     * createPoint is a utility method for testing. It creates a Point2D point from x and y coordinates.
     *
     * @param x x point
     * @param y y point
     * @return new point2D
     */
    public Point2D createPoint(double x, double y) {
        return new Point2D.Double(x, y);
    }

    /**
     * buildStopList is a utility method for testing. It loads data into the stops list.
     *
     * @param stopList list of stops to add
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
     * @param lat latitude
     * @param lon longitude
     * @return new node
     */
    public Node createNode(double lat, double lon) {
        Node n = new Node(1);
        n.setName("1");
        Coordinate c = new Coordinate(lat, lon);
        n.setCoords(c);
        return n;
    }

}
