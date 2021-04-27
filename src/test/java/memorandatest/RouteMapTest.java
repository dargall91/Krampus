package memorandatest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import main.java.memoranda.ui.RouteMap;
import main.java.memoranda.ui.RouteStop;
import main.java.memoranda.util.DuplicateKeyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RouteMapTest {
    private List<RouteStop> testRouteMapList;
    private RouteMap map;

    public RouteMapTest() {
        try {
            map = new RouteMap();
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all else");
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Check to be sure list is populated with the correct number of elements.
     */
    @Test
    void testCheckPopulatedListAmount() {
        testRouteMapList = new ArrayList<>();

        RouteStop p1 = new RouteStop(1, new Point(100, 100), "Stop");
        RouteStop p2 = new RouteStop(2, new Point(100, 100), "Stop");
        RouteStop p3 = new RouteStop(3, new Point(100, 100), "Stop");
        RouteStop p4 = new RouteStop(4, new Point(100, 100), "Stop");
        RouteStop p5 = new RouteStop(5, new Point(100, 100), "Stop");

        testRouteMapList.add(0, p1);
        testRouteMapList.add(1, p2);
        testRouteMapList.add(2, p3);
        testRouteMapList.add(3, p4);
        testRouteMapList.add(4, p5);

        map.buildStopList(testRouteMapList);

        int expected = map.getStops().size();

        assertEquals(expected, 5);
    }

    /**
     * Validate that Points are correctly created
     */
    @Test
    void testCreatePoint() {
        Point2D point = map.createPoint(100, 100);

        assertEquals(point.getX(), 100);
    }

    /**
     * Test that stops are loaded correctly by validating X.
     */
    @Test
    void testListPopulatedCorrectlyX() {
        testRouteMapList = new ArrayList<>();

        RouteStop p1 = new RouteStop(1, new Point(100, 100), "Stop");
        RouteStop p2 = new RouteStop(2, new Point(10, 10), "Stop");
        RouteStop p3 = new RouteStop(3, new Point(2, 300), "Stop");
        RouteStop p4 = new RouteStop(4, new Point(34, 233), "Stop");
        RouteStop p5 = new RouteStop(5, new Point(5, 100), "Stop");
        RouteStop p6 = new RouteStop(6, new Point(482, 597), "Stop");
        RouteStop p7 = new RouteStop(7, new Point(420, 420), "Stop");
        RouteStop p8 = new RouteStop(8, new Point(1, 999), "Stop");
        RouteStop p9 = new RouteStop(9, new Point(424, 43), "Stop");
        RouteStop p10 = new RouteStop(10, new Point(800, 420), "Stop");
        RouteStop p11 = new RouteStop(11, new Point(600, 732), "Stop");
        RouteStop p12 = new RouteStop(12, new Point(690, 89), "Stop");
        RouteStop p13 = new RouteStop(13, new Point(534, 585), "Stop");
        RouteStop p14 = new RouteStop(14, new Point(113, 333), "Stop");
        RouteStop p15 = new RouteStop(15, new Point(500, 500), "Stop");

        testRouteMapList.add(p1);
        testRouteMapList.add(p2);
        testRouteMapList.add(p3);
        testRouteMapList.add(p4);
        testRouteMapList.add(p5);
        testRouteMapList.add(p6);
        testRouteMapList.add(p7);
        testRouteMapList.add(p8);
        testRouteMapList.add(p9);
        testRouteMapList.add(p10);
        testRouteMapList.add(p11);
        testRouteMapList.add(p12);
        testRouteMapList.add(p13);
        testRouteMapList.add(p14);
        testRouteMapList.add(p15);

        map.buildStopList(testRouteMapList);

        double expected = map.getStops().get(6).getX();

        assertEquals(expected, 420);
    }

    /**
     * Test that stops are loaded correctly by validating Y.
     */
    @Test
    void testListPopulatedCorrectlyY() {
        testRouteMapList = new ArrayList<>();

        RouteStop p1 = new RouteStop(1, new Point(100, 100), "Stop");
        RouteStop p2 = new RouteStop(2, new Point(10, 10), "Stop");
        RouteStop p3 = new RouteStop(3, new Point(2, 300), "Stop");
        RouteStop p4 = new RouteStop(4, new Point(34, 233), "Stop");
        RouteStop p5 = new RouteStop(5, new Point(5, 100), "Stop");
        RouteStop p6 = new RouteStop(6, new Point(482, 597), "Stop");
        RouteStop p7 = new RouteStop(7, new Point(420, 420), "Stop");
        RouteStop p8 = new RouteStop(8, new Point(1, 999), "Stop");
        RouteStop p9 = new RouteStop(9, new Point(424, 43), "Stop");
        RouteStop p10 = new RouteStop(10, new Point(800, 420), "Stop");
        RouteStop p11 = new RouteStop(11, new Point(600, 732), "Stop");
        RouteStop p12 = new RouteStop(12, new Point(690, 89), "Stop");
        RouteStop p13 = new RouteStop(13, new Point(534, 585), "Stop");
        RouteStop p14 = new RouteStop(14, new Point(113, 333), "Stop");
        RouteStop p15 = new RouteStop(15, new Point(500, 500), "Stop");

        testRouteMapList.add(p1);
        testRouteMapList.add(p2);
        testRouteMapList.add(p3);
        testRouteMapList.add(p4);
        testRouteMapList.add(p5);
        testRouteMapList.add(p6);
        testRouteMapList.add(p7);
        testRouteMapList.add(p8);
        testRouteMapList.add(p9);
        testRouteMapList.add(p10);
        testRouteMapList.add(p11);
        testRouteMapList.add(p12);
        testRouteMapList.add(p13);
        testRouteMapList.add(p14);
        testRouteMapList.add(p15);

        map.buildStopList(testRouteMapList);

        double expected = map.getStops().get(11).getyPoint();

        assertEquals(expected, 89);
    }
}