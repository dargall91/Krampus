package memorandatest;

import main.java.memoranda.ui.RouteMap;

import static org.junit.jupiter.api.Assertions.*;

import main.java.memoranda.ui.RouteStop;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;



class RouteMapTest {
    private List<RouteStop> testRouteMapList;
    private RouteMap map;

    public RouteMapTest(){
        map = new RouteMap();
    }

    @BeforeAll
    static void beforeAll(){
        System.out.println("Before all else");
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    /**
     * Check to be sure list is populated with the correct number of elements.
     */
    void testCheckPopulatedListAmount(){
        testRouteMapList = new ArrayList<>();

        RouteStop p1 = new RouteStop(new Point(100,100));
        RouteStop p2 = new RouteStop(new Point(100,100));
        RouteStop p3 = new RouteStop(new Point(100,100));
        RouteStop p4 = new RouteStop(new Point(100,100));
        RouteStop p5 = new RouteStop(new Point(100,100));

        testRouteMapList.add(0, p1);
        testRouteMapList.add(1, p2);
        testRouteMapList.add(2, p3);
        testRouteMapList.add(3, p4);
        testRouteMapList.add(4, p5);

        map.buildStopList(testRouteMapList);

        int expected = map.getStops().size();

        assertEquals(expected, 5);
    }

    @Test
    /**
     * Validate that Points are correctly created
     */
    void testCreatePoint(){
        Point2D point = map.createPoint(100,100);

        assertEquals(point.getX(), 100);
    }

    @Test
    /**
     * Test that stops are loaded correctly by validating X.
     */
    void testListPopulatedCorrectlyX(){
        testRouteMapList = new ArrayList<>();

        RouteStop p1 = new RouteStop(new Point(100,100));
        RouteStop p2 = new RouteStop(new Point(10,10));
        RouteStop p3 = new RouteStop(new Point(2,300));
        RouteStop p4 = new RouteStop(new Point(34,233));
        RouteStop p5 = new RouteStop(new Point(5,100));
        RouteStop p6 = new RouteStop(new Point(482,597));
        RouteStop p7 = new RouteStop(new Point(420,420));
        RouteStop p8 = new RouteStop(new Point(1,999));
        RouteStop p9 = new RouteStop(new Point(424,43));
        RouteStop p10 = new RouteStop(new Point(800,420));
        RouteStop p11 = new RouteStop(new Point(600,732));
        RouteStop p12 = new RouteStop(new Point(690,89));
        RouteStop p13 = new RouteStop(new Point(534,585));
        RouteStop p14 = new RouteStop(new Point(113,333));
        RouteStop p15 = new RouteStop(new Point(500,500));

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

    @Test
    /**
     * Test that stops are loaded correctly by validating Y.
     */
    void testListPopulatedCorrectlyY(){
        testRouteMapList = new ArrayList<>();

        RouteStop p1 = new RouteStop(new Point(100,100));
        RouteStop p2 = new RouteStop(new Point(10,10));
        RouteStop p3 = new RouteStop(new Point(2,300));
        RouteStop p4 = new RouteStop(new Point(34,233));
        RouteStop p5 = new RouteStop(new Point(5,100));
        RouteStop p6 = new RouteStop(new Point(482,597));
        RouteStop p7 = new RouteStop(new Point(420,420));
        RouteStop p8 = new RouteStop(new Point(1,999));
        RouteStop p9 = new RouteStop(new Point(424,43));
        RouteStop p10 = new RouteStop(new Point(800,420));
        RouteStop p11 = new RouteStop(new Point(600,732));
        RouteStop p12 = new RouteStop(new Point(690,89));
        RouteStop p13 = new RouteStop(new Point(534,585));
        RouteStop p14 = new RouteStop(new Point(113,333));
        RouteStop p15 = new RouteStop(new Point(500,500));

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

        double expected = map.getStops().get(11).getY();

        assertEquals(expected, 89);
    }
}