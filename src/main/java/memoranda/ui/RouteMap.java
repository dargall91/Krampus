package main.java.memoranda.ui;

import main.java.memoranda.Coordinate;
import main.java.memoranda.Node;
import main.java.memoranda.NodeColl;
import main.java.memoranda.NodeMapper;
import main.java.memoranda.util.DuplicateKeyException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RouteMap extends JPanel {
    private NodeColl nodes = new NodeColl(); //will need to implement after you look at TourLoader
    private List<RouteStop> stops = new ArrayList<>();


    public RouteMap() {
        setPreferredSize( new Dimension(1000, 1000) );
        setBackground( Color.WHITE );

        addStop(stops, createPoint(100,100));
        addStop(stops, createPoint(110,110));
        addStop(stops, createPoint(120,120));
        addStop(stops, createPoint(80, 80));
        addStop(stops, createPoint(75, 60));
        addStop(stops, createPoint(250,10));
        addStop(stops, createPoint(300,400));
    }

    public List<RouteStop> getStops() {
        return stops;
    }

    @Override
    public void paintComponent ( Graphics g ) {
        super.paintComponent( g );
        for (int i = 0; i < stops.size(); i++) {
            if (i < stops.size() - 1){
                stops.get(i).drawConnection(g, stops.get(i).getBusStop(),
                        stops.get(i + 1).getBusStop());
            }
        }
        for (RouteStop s : stops) {
            s.drawStop(g);
        }

    }

    public void populateStops(NodeColl nodeCollection) {
        NodeMapper mapper = new NodeMapper(nodeCollection);

        while(nodeCollection.iterator().hasNext()){
            for (Node n : nodeCollection) {
                addStop(stops, mapper.getScaled(n));
            }
        }
    }

    public void addStop(List<RouteStop> list, Point2D point) {
        list.add(new RouteStop(point));
        repaint();
    }

    public Point2D createPoint(double x, double y) {
        Point2D p = new Point2D.Double(x, y);
        return p;
    }

    public void buildStopList(List<RouteStop> stopList) {
        stops.clear();
        if (stopList != null) {
            for (int i = 0; i < stopList.size(); i++) {
                getStops().add(i, stopList.get(i));
            }
        }
    }

    /*public Node createNode(double lat, double lon){
        Node n = new Node(1);
        n.setName("1");
        Coordinate c = new Coordinate(lat, lon);
        n.setCoords(c);
        return n;
    }*/
}