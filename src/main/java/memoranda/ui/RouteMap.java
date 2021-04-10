package main.java.memoranda.ui;

import main.java.memoranda.Node;
import main.java.memoranda.NodeColl;
import main.java.memoranda.NodeMapper;
import main.java.memoranda.util.DuplicateKeyException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class RouteMap extends JPanel {
    private NodeColl nodes = new NodeColl(); //will need to implement after you look at TourLoader
    private List<RouteStop> stops = new LinkedList<>();

    public RouteMap() {
        try {
            nodes = createNodeColl();
            populateStops(nodes);
        }
        catch (DuplicateKeyException e){
            System.out.println("Duplicate key exception");
        }

        setPreferredSize( new Dimension(1000, 1000) );
        setBackground( Color.WHITE );
    }

    @Override
    public void paintComponent ( Graphics g ) {
        super.paintComponent( g );
        for (RouteStop s : stops) {
            s.drawStop(g);
        }
        for (int i = 0; i < stops.size(); i++) {
            if (i < stops.size() - 1){
                stops.get(i).drawConnection(g, stops.get(i).busStop,
                        stops.get(i + 1).busStop);
            }
        }
    }

    public void populateStops(NodeColl nodeCollection) {
        NodeMapper mapper = new NodeMapper(nodeCollection);

        while(nodeCollection.iterator().hasNext()){
            for (Node n : nodeCollection) {
                addStop(mapper.getScaled(n));
            }
        }
    }

    public void addStop(Point2D point) {
        stops.add(new RouteStop(point));
        repaint();
    }

    NodeColl createNodeColl() throws DuplicateKeyException {
        NodeColl nc = new NodeColl();
        Node n = new Node(1, "busstop1", 1.23, 3.24);
        Node n2 = new Node(2, "bus stop number 2", 2.34, -134.2331);
        nc.add(n);
        nc.add(n2);

        nodes = nc;
        return nodes;
    }

    /**
     * RouteStop creates the graphical representation of bus stops.
     */
    private class RouteStop {
        protected final double RADIUS = 10;
        private final double x;
        private final double y;
        private final Point2D busStop;

        /**
         * Constructor for RouteStop.
         * @param x the x coordinate
         * @param y the y coordinate
         */
        public RouteStop(Point2D point) {
            busStop = point;
            this.x = point.getX();
            this.y = point.getY();
        }

        /**
         * Draws the stop on the map.
         * @param g
         */
        public void drawStop(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            Ellipse2D.Double circle = new Ellipse2D.Double(x, y, RADIUS, RADIUS);

            g2d.setColor(Color.BLUE);
            g2d.fill(circle);
        }

        public void drawConnection(Graphics g, Point2D p1, Point2D p2){
            Graphics2D g2d = (Graphics2D) g;
            g2d.draw(new Line2D.Double(p1, p2));
            //Set the style of the line
        }
    }
}