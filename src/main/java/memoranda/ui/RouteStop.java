package main.java.memoranda.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.String;

/**
 * RouteStop creates stops and connections for RouteMap.
 *
 * @author Kevin Dolan
 * @version 1.0
 */
public class RouteStop {

    static final double RADIUS = 10;
    private final double x;
    private final double y;
    private final Point2D busStop;
    private final int id;

    /**
     * Constructor for RouteStop.
     *
     * @param point the coordinate
     */
    public RouteStop(int id, Point2D point) {
        this.id = id;
        busStop = point;
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * Draws the stop on the map.
     *
     * @param g
     */
    public void drawStop(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Ellipse2D.Double circle = new Ellipse2D.Double(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
        int xAxis = (int) x;
        int yaxis = (int) y;

        g2d.setColor(Color.BLUE);
        g2d.fill(circle);
        g2d.drawString("Stop", xAxis, yaxis);
    }

    /**
     * Draws the connections between stops.
     *
     * @param g
     * @param p1
     * @param p2
     */
    public void drawConnection(Graphics g, Point2D p1, Point2D p2) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(new Line2D.Double(p1, p2));
        //Set the style of the line
    }

    /**
     * Getter for x.
     *
     * @return
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for y.
     *
     * @return
     */
    public double getY() {
        return y;
    }

    /**
     * Getter for BusStop.
     *
     * @return
     */
    public Point2D getBusStop() {
        return busStop;
    }

    /**
     * Getter for id.
     *
     * @return
     */
    public int getId() {
        return id;
    }
}
