package main.java.memoranda.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class RouteStop {
    /**
     * RouteStop creates the graphical representation of bus stops.
     */
    protected final double RADIUS = 10;
    private final double x;
    private final double y;
    private final Point2D busStop;

    /**
     * Constructor for RouteStop.
     * @param point the coordinate
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
        g2d.draw(new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
        //Set the style of the line
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point2D getBusStop() {
        return busStop;
    }
}
