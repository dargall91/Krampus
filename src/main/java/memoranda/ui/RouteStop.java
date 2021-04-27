package main.java.memoranda.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


/**
 * RouteStop creates stops and connections for RouteMap.
 *
 * @author Kevin Dolan, John Thurstonson
 * @version 2021-04-25
 */
public class RouteStop {
    /**
     * RouteStop creates the graphical representation of bus stops.
     */

    protected static final double RADIUS = 10;
    private final double x;
    private final double y;
    private final Point2D busStop;
    private final int id;
    private final String name;

    /**
     * Constructor for RouteStop.
     *
     * @param point the coordinate
     */
    public RouteStop(int id, Point2D point, String name) {
        this.id = id;
        busStop = point;
        this.x = point.getX();
        this.y = point.getY();
        this.name = name;
    }

    /**
     * Draws the stop on the map.
     *
     * @param g graphics obj
     */
    public void drawStop(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, RADIUS, RADIUS);
        int xAxis = (int) x;
        int yaxis = (int) y;

        g2d.setColor(Color.BLUE);
        g2d.fill(circle);
        g2d.drawString(name, xAxis, yaxis);
    }

    /**
     * Draws the connections between stops.
     *
     * @param g  graphics object
     * @param p1 first point
     * @param p2 second point
     */
    public void drawConnection(Graphics g, Point2D p1, Point2D p2) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(new Line2D.Double(p1, p2));
        //Set the style of the line
    }

    /**
     * Getter for x.
     *
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for y.
     *
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * Getter for BusStop.
     *
     * @return point for bus stop
     */
    public Point2D getBusStop() {
        return busStop;
    }

    /**
     * Getter for id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }
}
