package memorandatest;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;

import main.java.memoranda.Bus;
import main.java.memoranda.BusColl;
import main.java.memoranda.Coordinate;
import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.Node;
import main.java.memoranda.NodeColl;
import main.java.memoranda.NodeMapper;
import main.java.memoranda.Project;
import main.java.memoranda.ProjectManager;
import main.java.memoranda.Route;
import main.java.memoranda.RouteColl;
import main.java.memoranda.RouteLoader;
import main.java.memoranda.Tour;
import main.java.memoranda.TourColl;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.FileStorage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

/**
 * TestDataCollection unit test suite.
 *
 * @author Brian Pape
 * @version 2021-04-06
 */
public class TestDataCollections {

    private static Project prj;
    private static FileStorage stg;

    private static NodeColl nodeColl;
    private static RouteColl routeColl;
    private static TourColl tourColl;
    private static DriverColl driverColl;
    private static BusColl busColl;

    private static NodeMapper nodeMapper;


    // constants used for consistent testing
    private static final int NODE1 = 1;
    private static final int NODE2 = 2;
    private static final int NODE3 = 3;
    private static final int NODE4 = 4;

    private static final int DRIVER1 = 1;
    private static final int DRIVER2 = 2;

    private static final int ROUTE1 = 1;
    private static final int ROUTE2 = 2;

    private static final int TOUR1 = 1;
    private static final int TOUR2 = 2;

    private static final int BUS1 = 1;
    private static final int BUS2 = 2;


    /**
     * Run before all test methods.
     *
     * <p>We need to create a project and file storage object for use by all test methods
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all test methods");
        prj = ProjectManager.createProject("Test project", CalendarDate.today(), null);
        stg = new FileStorage();
        stg.createProjectStorage(prj);
    }

    /**
     * Run before each test method.
     *
     * <p>We need to have a known test fixture for most tests
     *
     * @throws DuplicateKeyException if duplicate key encountered
     */
    @BeforeEach
    void beforeEach() throws DuplicateKeyException {
        createNodeColl();
        createBusColl();
        createRouteColl();
        createTourColl();
        createDriverColl();
    }

    /**
     * Run after each test method.
     *
     * <p>No tear-down needed
     */
    @AfterEach
    void afterEach() {
        System.out.println("After each test method");
    }

    /**
     * Run after all test methods.
     *
     * <p>Need to remove temporary project storage
     */
    @AfterAll
    static void afterAll() {
        System.out.println("After all test methods");
        //stg.removeProjectStorage(prj);
    }


    /**
     * test error handling for duplicate driver in collection.
     */
    @Test
    void testAddDuplicateDriverToCollection() throws DuplicateKeyException {
        DriverColl nc = new DriverColl();
        Driver n = new Driver(DRIVER1, "test driver", "555-555-1212");
        nc.add(n);

        assertThrows(DuplicateKeyException.class, () -> {
            nc.add(n);
        });
    }


    /**
     * Utility method: Create a generic driver with a name.
     *
     * @param id   the id for the driver
     * @param name the name for the driver
     * @return a Driver object
     */
    Driver createNamedDriver(int id, String name) {
        return new Driver(id, name, "555-555-1212");
    }

    /**
     * Utility method: Create a generic driver associated with a tour.
     *
     * @param id A the id for the driver
     * @return the Driver object
     */
    Driver createGenericDriverWithTour(int id) throws DuplicateKeyException {
        // routeColl, busColl set in @BeforeAll

        TourColl tourColl = new TourColl();
        Tour tour = tourColl.newItem();
        tour.setName("A tour");
        tour.setTime(LocalTime.of(12, 0));
        tour.setRoute(routeColl.get(ROUTE1));
        tour.setBus(busColl.get(BUS1));

        Tour tour2 = tourColl.newItem();
        tour2.setName("Tour number 2");
        tour2.setTime(LocalTime.of(14, 0));
        tour2.setRoute(routeColl.get(ROUTE2));
        tour2.setBus(busColl.get(BUS2));


        Driver d = createNamedDriver(id, "test driver");
        d.addTour(tour);
        d.addTour(tour2);

        assertNotNull(d);

        return d;
    }


    /**
     * validate that adding a tour to a driver adds the driver to the tour.
     */
    @Test
    void testAddTourToDriver() throws DuplicateKeyException {
        Driver driver = createNamedDriver(1, "Fred");
        Tour tour = createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);
        assertEquals(driver, tour.getDriver());
    }

    /**
     * validate that adding a tour to a driver adds the driver to the tour.
     */
    @Test
    void testAddTourToTwoDrivers() throws DuplicateKeyException {
        Driver driver = createNamedDriver(1, "Fred");
        Driver driver2 = createNamedDriver(1, "Jim");
        Tour tour = createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);
        assertThrows(DuplicateKeyException.class, () -> {
            driver2.addTour(tour);
        });
    }

    /**
     * validate that removing a tour from a driver removes the driver from the tour.
     */
    @Test
    void testRemoveTourFromDriver() throws DuplicateKeyException {
        Driver driver = createNamedDriver(1, "Fred");
        Tour tour = createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);
        driver.delTour(tour);
        assertNull(tour.getDriver());
    }

    /**
     * validate that removing a tour from a driver removes the driver from the tour.
     */
    @Test
    void testRemoveTourFromDriver2() throws DuplicateKeyException {
        Driver driver = createNamedDriver(1, "Fred");
        Tour tour = createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);
        driver.delTour(tour);
        assertNull(driver.getTour(tour.getID()));
    }

    /**
     * validate that an invalid driver cannot be removed from tour.
     *
     * @throws DuplicateKeyException if non-unique key encountered
     */
    @Test
    void testInvalidDriverRemovalFromTour() throws DuplicateKeyException {
        Driver driver = createNamedDriver(1, "Fred");
        Tour tour = createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);

        Driver driver2 = createNamedDriver(2, "Jim");
        assertThrows(UnsupportedOperationException.class, () -> {
            tour.delDriver(driver2);
        });
    }

    /**
     * validate that a null driver cannot be removed from tour.
     *
     * @throws DuplicateKeyException if non-unique key encountered
     */
    @Test
    void testNullDriverRemovalFromTour() throws DuplicateKeyException {
        Driver driver = createNamedDriver(1, "Fred");
        Tour tour = createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);
        assertThrows(UnsupportedOperationException.class, () -> {
            tour.delDriver(null);
        });
    }


    /**
     * Test the basic node constructor.
     */
    @Test
    void testNodeConstructor() {
        final int ID = 1;
        Node n = new Node(ID, "busstop1", 1.23, 3.24);
        assertEquals(ID, n.getID());
    }


    /**
     * Utility method: Create a node collection.
     *
     * @return a node collection
     */
    NodeColl createNodeColl() throws DuplicateKeyException {
        NodeColl nc = new NodeColl();
        Node n = new Node(NODE1, "busstop1", 1.23, 3.24);
        Node n2 = new Node(NODE2, "bus stop number 2", 2.34, -134.2331);
        nc.add(n);
        nc.add(n2);

        nodeColl = nc;
        return nodeColl;
    }

    /**
     * Utility method: create a bus collection.
     *
     * @return a bus collection
     * @throws DuplicateKeyException if duplicate key is added to collection
     */
    BusColl createBusColl() throws DuplicateKeyException {
        busColl = new BusColl();

        Bus b1 = busColl.newItem();
        b1.setNumber(BUS1);
        Bus b2 = busColl.newItem();
        b2.setNumber(BUS2);

        busColl.add(b1);
        busColl.add(b2);

        System.out.println("In createBusColl: Bus list contains " + busColl.get(BUS1) + ", " + busColl.get(BUS2));

        return busColl;
    }


    /**
     * test creating a bus object.
     */
    @Test
    void testCreateBus() {
        Bus b1 = busColl.newItem();
        b1.setNumber(BUS1);

        assertEquals(BUS1, b1.getNumber());
    }


    /**
     * test creating a bus collection.
     */
    @Test
    void testBusCollCreation() {
        assertNotNull(busColl);
    }


    /**
     * test adding several buses to a bus collection.
     */
    @Test
    void testBusCollStatus() {
        assertEquals(2, busColl.size());
    }


    /**
     * test serializing a bus collection.
     *
     * @throws IOException           if output file cannot be opened
     * @throws DuplicateKeyException if duplicate key is added to bus collection
     */
    @Test
    void testWriteBusColl() throws IOException, DuplicateKeyException {

        System.out.println("Bus list contains " + busColl.get(BUS1) + ", " + busColl.get(BUS2));
        stg.storeBusList(prj, busColl);

        System.out.println("Load bus list");
        BusColl bl = stg.openBusList(prj);

        int count = 0;
        for (Bus bb : bl) {
            count++;
            System.out.println("Found bus in list=" + bb);
        }

        assertEquals(2, count);
    }


    /**
     * test creating a driver with multiple tours.
     *
     * @throws DuplicateKeyException if duplicate IDs are added to collection
     */
    /*
    @Test
    void testCreateDriverTourIDs() throws DuplicateKeyException{
        Driver d=createGenericDriverWithTour(DRIVER1);
        System.out.println("Tour ids="+d.getTourIDs());

        assertEquals(2, d.getTours().size());
    }
     */

    /**
     * test creating a driver with multiple tours.
     *
     * @throws DuplicateKeyException if duplicate IDs are added to collection
     */
    @Test
    void testCreateDriverTours() throws DuplicateKeyException {
        Driver d = createGenericDriverWithTour(DRIVER1);

        assertEquals(2, d.getTours().size());
    }


    /**
     * test creating a driver collection.
     *
     * @return a driver collection
     * @throws DuplicateKeyException if duplicate IDs are added to collection
     */
    DriverColl createDriverColl() throws DuplicateKeyException {

        // tourColl created in @beforeEach

        System.out.println("TourColl=" + tourColl);
        System.out.println("TourColl.size()=" + tourColl.size());
        System.out.println("tour1=" + tourColl.get(TOUR1));
        System.out.println("tour2=" + tourColl.get(TOUR2));

        Driver d1 = createNamedDriver(DRIVER1, "Driver 1");
        d1.addTour(tourColl.get(TOUR1));
        System.out.println("Tour1 driver=" + tourColl.get(TOUR1).getDriver());

        Driver d2 = createNamedDriver(DRIVER2, "Driver 2");
        d2.addTour(tourColl.get(TOUR2));
        System.out.println("Tour2 driver=" + tourColl.get(TOUR2).getDriver());

        DriverColl dc = new DriverColl();
        dc.add(d1);
        dc.add(d2);

        driverColl = dc;
        return driverColl;
    }

    /**
     * test ability to add a node.
     */
    @Test
    void testCreateDriverColl() throws DuplicateKeyException {

        // driverColl created in @BeforeEach

        assertEquals(2, driverColl.size());
    }

    /**
     * Test ability to read and write JSON values (serialize/deserialize data).
     *
     * @throws JsonProcessingException if json error occurs
     * @throws IOException             if output file cannot be written
     */
    @Test
    void testWriteDriverColl() throws JsonProcessingException, IOException, DuplicateKeyException {

        Driver d = driverColl.newItem();
        driverColl.add(d);

        stg.storeDriverList(prj, driverColl);

        // need to create all new collections so tours aren't already occupied by drivers
        createNodeColl();
        createBusColl();
        createRouteColl();
        createTourColl();


        System.out.println("Load driver list");
        DriverColl dl = stg.openDriverList(prj, tourColl);

        int count = 0;
        for (Driver nn : dl) {
            count++;
            System.out.println("Found driver in list=" + nn);
        }

        assertEquals(3, count);
    }


    /**
     * test creating a route collection.
     *
     * @return route collection
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    RouteColl createRouteColl() throws DuplicateKeyException {

        LinkedList<Integer> nli = new LinkedList<>(Arrays.asList(NODE1, NODE2));
        RouteLoader rl1 = new RouteLoader(ROUTE1, "Route 1", nli);
        RouteLoader rl2 = new RouteLoader(ROUTE2, "Route 2", nli);
        LinkedList<RouteLoader> rlc = new LinkedList<>(Arrays.asList(rl1, rl2));

        RouteColl rc = new RouteColl(nodeColl, rlc);

        routeColl = rc;
        return routeColl;
    }


    /**
     * test ability to create a route.
     *
     * @throws DuplicateKeyException if route does not have unique ID
     */
    @Test
    void testCreateRouteColl() throws DuplicateKeyException {
        RouteColl rc = createRouteColl();

        assertEquals(2, rc.size());
    }


    /**
     * test ability to add a route to a collection.
     *
     * @throws DuplicateKeyException if route does not have unique ID
     */
    @Test
    void testAddRoute() throws DuplicateKeyException {
        RouteColl rc = createRouteColl();
        assertEquals(2, rc.size());
    }

    /**
     * test the start and end times of a tour
     */
    @Test
    void testTourLength() {
        Tour tour = tourColl.get(1);
        tour.setSpeed(100);

        System.out.println("route length=" + tour.getRoute().length());
        System.out.println("tour start time=" + tour.getStartTime());
        System.out.println("tour end time=" + tour.getEndTime());
        assertEquals(LocalTime.of(20, 42, 24), tour.getEndTime());
    }


    /**
     * Test ability to read and write JSON values.
     *
     * @throws JsonProcessingException if json error occurs
     * @throws IOException             if file read/write error occurs
     * @throws DuplicateKeyException   if non-unique ID is encountered
     */
    @Test
    void testWriteRoute() throws JsonProcessingException, IOException, DuplicateKeyException {

        // nodeColl and routeColl created in @BeforeAll
        System.out.println("Save route list");
        stg.storeRouteList(prj, routeColl);

        System.out.println("Load route list");
        RouteColl rl = stg.openRouteList(prj, nodeColl);

        int count = 0;
        for (Route rr : rl) {
            count++;
            System.out.println("Found route in list=" + rr);
        }

        assertEquals(2, count);

    }


    /**
     * Utility function to create a tour.
     *
     * @return Tour object
     */
    Tour createTour() {
        return createNamedTour("A tour");
    }

    /**
     * Utility functoin to create a named tour.
     *
     * @param name the name for the tour
     * @return Tour object
     */
    Tour createNamedTour(String name) {
        return createNamedTourAtTime(name, 12, 0);
    }


    /**
     * Utility function: create a named tour at a particular time.
     *
     * @param name   the name for the tour
     * @param hour   the hour of the tour
     * @param minute the minute for the tour
     * @return a Tour object
     */
    Tour createNamedTourAtTime(String name, int hour, int minute) {

        // route and node collections are created in @BeforeEach

        Tour tour = tourColl.newItem();

        tour.setName(name);
        tour.setTime(LocalTime.of(hour, minute));
        tour.setRoute(routeColl.get(ROUTE1));
        tour.setBus(busColl.get(BUS1));

        return tour;
    }

    /**
     * test creating tours and valid IDs.
     *
     * @throws JsonProcessingException if Json error occurs
     * @throws IOException             if output file cannot be written
     * @throws DuplicateKeyException   if duplicate id is added to collection
     */
    @Test
    void testCreateTourIDs() {
        assertNotNull(tourColl.get(TOUR1));
        assertNotNull(tourColl.get(TOUR2));
    }

    /**
     * test creating a tour.
     *
     * @throws JsonProcessingException if Json error occurs
     * @throws IOException             if output file cannot be written
     * @throws DuplicateKeyException   if duplicate id is added to collection
     */
    @Test
    void testCreateTour() throws DuplicateKeyException {
        assertNotNull(createTour());
    }


    /**
     * Utility function: test creating a tour collection.
     *
     * @return a tour collection
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    TourColl createTourColl() throws DuplicateKeyException {
        System.out.println("in createTourColl");
        tourColl = new TourColl();

        Tour tour1 = createTour();
        tour1.setSpeed(60);
        Tour tour2 = createNamedTourAtTime("A long tour", 17, 0);
        tour2.setSpeed(100);

        tourColl.add(tour1);
        tourColl.add(tour2);

        return tourColl;
    }


    /**
     * test creating a tour collection.
     */
    @Test
    void testCreateTourColl() {
        assertNotNull(tourColl);
    }


    /**
     * test serializing/deserializing a Tour collection.
     *
     * @throws IOException           if output file cannot be written
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testWriteTour() throws IOException, DuplicateKeyException {

        // route and node collections are created in @BeforeEach

        System.out.println("Save tour list");
        stg.storeTourList(prj, tourColl);

        System.out.println("Load tour list");
        TourColl tl = stg.openTourList(prj, routeColl, busColl);

        int count = 0;
        for (Tour tt : tl) {
            count++;
            System.out.println("Found route in list=" + tt);
        }

        assertEquals(2, count);
    }


    /**
     * test serializing/deserializing a Tour collection with no route.
     *
     * @throws IOException           if output file cannot be written
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testWriteTourWithNoRoute() throws DuplicateKeyException, IOException {

        Tour t = tourColl.newItem();

        // add a driver
        Driver d = driverColl.newItem();
        d.addTour(t);

        // add a time
        t.setTime(LocalTime.of(6, 0));

        // add a bus
        Bus b = busColl.newItem();
        busColl.add(b);
        b.addTour(t);

        // but no route
        tourColl.add(t);

        System.out.println("Save tour list");
        stg.storeTourList(prj, tourColl);

        System.out.println("Load tour list");
        TourColl tl = stg.openTourList(prj, routeColl, busColl);

        int count = 0;
        for (Tour tt : tl) {
            count++;
            System.out.println("Found route in list=" + tt);
        }

        assertEquals(3, count);


    }

    /**
     * test serializing/deserializing a Tour collection with no bus.
     *
     * @throws IOException           if output file cannot be written
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testWriteTourWithNoBus() throws DuplicateKeyException, IOException {

        Tour t = tourColl.newItem();

        // add a driver
        Driver d = driverColl.newItem();
        d.addTour(t);

        // add a time
        t.setTime(LocalTime.of(6, 0));

        // add a route
        Route r = routeColl.newItem();
        routeColl.add(r);
        t.setRoute(r);

        // but no bus
        tourColl.add(t);

        System.out.println("Save tour list");
        stg.storeTourList(prj, tourColl);

        System.out.println("Load tour list");
        TourColl tl = stg.openTourList(prj, routeColl, busColl);

        int count = 0;
        for (Tour tt : tl) {
            count++;
            System.out.println("Found route in list=" + tt);
        }

        assertEquals(3, count);

    }


    /**
     * test serializing/deserializing a Tour collection with no time.
     *
     * @throws IOException           if output file cannot be written
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testWriteTourWithNoTime() throws DuplicateKeyException, IOException {

        Tour t = tourColl.newItem();

        // add a driver
        Driver d = driverColl.newItem();
        d.addTour(t);

        // add a route
        Route r = routeColl.newItem();
        routeColl.add(r);
        t.setRoute(r);

        // add a bus
        Bus b = busColl.newItem();
        busColl.add(b);
        b.addTour(t);

        // but no time
        tourColl.add(t);

        System.out.println("Save tour list");
        stg.storeTourList(prj, tourColl);

        System.out.println("Load tour list");
        TourColl tl = stg.openTourList(prj, routeColl, busColl);

        int count = 0;
        for (Tour tt : tl) {
            count++;
            System.out.println("Found route in list=" + tt);
        }

        assertEquals(3, count);

    }


    /**
     * test ability to add a node.
     */
    @Test
    void testAddNode() throws DuplicateKeyException {
        NodeColl nc = new NodeColl();
        Node n = new Node(NODE1, "test", 1.23, 3.45);
        nc.add(n);

        assertEquals(NODE1, nc.size());
    }

    /**
     * Test ability to read and write JSON values.
     *
     * @throws JsonProcessingException if Json error occurs
     * @throws IOException             if output file cannot be written
     * @throws DuplicateKeyException   if duplicate id is added to collection
     */
    @Test
    void testWriteNode() throws JsonProcessingException, IOException, DuplicateKeyException {

        // node collection create in @BeforeEach

        System.out.println("After adding two entries, list contains " + nodeColl.size() + " elements.");

        stg.storeNodeList(nodeColl, prj);

        System.out.println("Load node list");
        NodeColl loadedNodeColl = stg.openNodeList(prj);

        int count = 0;
        for (Node nn : loadedNodeColl) {
            count++;
            System.out.println("Found node in list=" + nn);
        }

        assertEquals(2, count);

    }

    /**
     * Test ability to create and remove project storage directory in $HOME/.memoranda.
     */
    @Test
    void testProjectStorage() {
        Project prj = ProjectManager.createProject("Test project", CalendarDate.today(), null);
        //Element el = new Element("project");
        //el.addAttribute(new Attribute("id", id));
        FileStorage stg = new FileStorage();
        //Project prj=new ProjectImpl(el);
        stg.createProjectStorage(prj);
        stg.removeProjectStorage(prj);
    }


    /**
     * Test haversine distance formula calculations.
     */
    @Test
    void testHaversine() {
        Coordinate c1 = new Coordinate(41.507483, -99.436554);
        Coordinate c2 = new Coordinate(38.504048, -98.315949);
        System.out.println("distance=" + c1.distanceTo(c2));
        assertEquals(c1.distanceTo(c2), 347.3, 0.1);

        c1 = new Coordinate(38.504048, -98.315949);
        c2 = new Coordinate(41.507483, -99.436554);
        System.out.println("distance=" + c1.distanceTo(c2));
        assertEquals(c1.distanceTo(c2), 347.3, 0.1);
    }


    /**
     * A Test distance to a null coordinate.
     */
    @Test
    void testDistanceToNull() {
        Coordinate c1;
        c1 = new Coordinate(1.0, 2.0);
        assertThrows(NullPointerException.class, () -> {
            c1.distanceTo(null);
        });
    }

    /**
     * test coordinate equality.
     */
    @Test
    void testCoordinateEquality() {
        Coordinate c1;
        Coordinate c2;
        c1 = new Coordinate(83.123456789, -128.987654321);
        c2 = new Coordinate(83.123456789, -128.987654321);
        assertTrue(c1.equals(c2));
    }

    /**
     * test coordinate inequality based upon significant digit difference.
     */
    @Test
    void testCoordinateInequalityDigits() {
        Coordinate c1;
        Coordinate c2;
        c1 = new Coordinate(83.12345678, -128.987654321);
        c2 = new Coordinate(83.123456789, -128.987654321);
        assertFalse(c1.equals(c2));
    }

    /**
     * check for coordinate inequality based on sign.
     */
    @Test
    void testCoordinateInequalitySign() {
        Coordinate c1;
        Coordinate c2;
        c1 = new Coordinate(-83.123456789, -128.987654321);
        c2 = new Coordinate(83.123456789, -128.987654321);
        assertFalse(c1.equals(c2));
    }

    public static final int MAPPER_TOP = 1;
    public static final int MAPPER_RIGHT = 4;
    public static final int MAPPER_BOTTOM = 3;
    public static final int MAPPER_LEFT = 2;

    /**
     * utility function to set up nodes/coordinates for scale testing.
     *
     * @return NodeMapper with coordinates set up
     * @throws DuplicateKeyException if duplicate key encountered
     */
    NodeMapper setupMapperNodes() throws DuplicateKeyException {

        NodeColl nc = new NodeColl();

        // total lat range = -0.1 - 0.9 = 1.0
        // total lon range = -1.0 - 1.0 = 2.0
        Coordinate c1 = new Coordinate(-0.1, 0.5);
        Node n1 = nc.newItem();
        n1.setCoords(c1);

        Coordinate c2 = new Coordinate(0.1, -1.0);
        Node n2 = nc.newItem();
        n2.setCoords(c2);

        Coordinate c3 = new Coordinate(0.9, 0.5);
        Node n3 = nc.newItem();
        n3.setCoords(c3);

        Coordinate c4 = new Coordinate(0.1, 1.0);
        Node n4 = nc.newItem();
        n4.setCoords(c4);

        Coordinate c5 = new Coordinate(0.4, 0.5);
        Node n5 = nc.newItem();
        n5.setCoords(c5);

        nc.add(n1);
        nc.add(n2);
        nc.add(n3);
        nc.add(n4);
        nc.add(n5);

        nodeMapper = new NodeMapper(nc);
        nodeMapper.setMapSize(new Dimension(1000, 600));

        return nodeMapper;

        /*
        System.out.println("node 1="+nm.getScaled(n1));
        System.out.println("node 2="+nm.getScaled(n2));
        System.out.println("node 3="+nm.getScaled(n3));
         */
    }


    /**
     * test rightmost node.
     *
     * @throws DuplicateKeyException if duplicate key used
     */
    @Test
    void testNodeMapperRight() throws DuplicateKeyException {
        NodeMapper nm = setupMapperNodes();
        NodeColl nc = nm.getNodeColl();

        assertEquals(new Point(999, 119), nm.getScaled(nc.get(MAPPER_RIGHT)));
    }

    /**
     * test bottommost node.
     *
     * @throws DuplicateKeyException if duplicate key used
     */
    @Test
    void testNodeMapperBottom() throws DuplicateKeyException {
        NodeMapper nm = setupMapperNodes();
        NodeColl nc = nm.getNodeColl();

        assertEquals(new Point(749, 599), nm.getScaled(nc.get(MAPPER_BOTTOM)));
    }

    /**
     * test topmost node.
     *
     * @throws DuplicateKeyException if duplicate key used
     */
    @Test
    void testNodeMapperTop() throws DuplicateKeyException {
        NodeMapper nm = setupMapperNodes();
        NodeColl nc = nm.getNodeColl();

        assertEquals(new Point(749, 0), nm.getScaled(nc.get(MAPPER_TOP)));
    }

    /**
     * test leftmost node.
     *
     * @throws DuplicateKeyException if duplicate key used
     */
    @Test
    void testNodeMapperLeft() throws DuplicateKeyException {
        NodeMapper nm = setupMapperNodes();
        NodeColl nc = nm.getNodeColl();

        assertEquals(new Point(0, 119), nm.getScaled(nc.get(MAPPER_LEFT)));
    }

    /**
     * test behavior with node not in node collection.
     */
    @Test
    void testOutOfRangeNode() throws DuplicateKeyException {
        NodeMapper nm = setupMapperNodes();
        Node nmissing = nm.getNodeColl().newItem();
        nmissing.setCoords(new Coordinate(-10.0, 10.0));

        assertThrows(IllegalArgumentException.class, () -> {
            nm.getScaled(nmissing);
        });
    }

    /**
     * test behavior with empty node collection.
     */
    @Test
    void testEmptyNodeColl() {
        NodeColl nc = new NodeColl();

        assertThrows(IllegalStateException.class, () -> {
            new NodeMapper(nc);
        });
    }

    /**
     * test node collection with 0 range.
     *
     * @throws DuplicateKeyException if duplicate key encountered
     */
    @Test
    void testRangelessNodeColl() throws DuplicateKeyException {

        NodeColl nc = new NodeColl();

        Coordinate c1 = new Coordinate(0.0, 0.0);
        Node n1 = nc.newItem();
        Node n2 = nc.newItem();
        n1.setCoords(c1);
        n2.setCoords(c1);

        nc.add(n1);
        nc.add(n2);

        NodeMapper nm = new NodeMapper(nc);
        nm.setMapSize(new Dimension(1000, 600));

        assertEquals(new Point(0, 0), nm.getScaled(n1));
    }


    /**
     * Test that the route contains all original nodes with no losses after optimization
     */
    @Test
    void testRouteOptimizerForDataIntegrity() {
        LinkedList<Node> nodes = (LinkedList<Node>) routeColl.get(ROUTE1).getRoute().clone();
        int size = nodes.size();
        routeColl.get(ROUTE1).optimize();
        assertEquals(size, routeColl.get(ROUTE1).getRoute().size());
        for (Node n : nodes) {
            assertTrue(routeColl.get(ROUTE1).getRoute().contains(n));
        }
    }


    /**
     * Test that the optimization results in a shorter length route
     */
    @Test
    void testRouteOptimizerSuccess() {
        Route r = routeColl.get(ROUTE1);

        Node n3 = new Node(NODE3, "node3", 20.0, 0.0);
        routeColl.get(ROUTE1).addNode(n3);

        Node n4 = new Node(NODE4, "node4", 10.0, 0.0);
        r.addNode(n4);

        double originalDistance = r.length();
        r.optimize();
        assertTrue(r.length() <= originalDistance);
    }


    /**
     * Test that the optimization results in a deterministic output for a given set
     */
    @Test
    void testRouteOptimizerSpecificOutcome() {
        Route r = routeColl.get(ROUTE1);

        r.getRoute().get(0).setCoords(new Coordinate (0.0, 0.0));
        Node n1 = r.getRoute().get(0);
        r.getRoute().get(1).setCoords(new Coordinate (40.0, 0.0));
        Node n2 = r.getRoute().get(1);

        Node n3 = new Node(NODE3, "node3", 20.0, 0.0);
        r.addNode(n3);
        Node n4 = new Node(NODE4, "node4", 10.0, 0.0);
        r.addNode(n4);

        r.optimize();

        // Expected order: n1, n4, n3, n2
        assertEquals(n1, r.getRoute().get(0)); //start unchanged
        assertEquals(n4, r.getRoute().get(1));
        assertEquals(n3, r.getRoute().get(2));
        assertEquals(n2, r.getRoute().get(3));
    }


    /**
     * Test that the route contains all original nodes with no losses after changing the start
     */
    @Test
    void testSetStartForIntegrity() {
        LinkedList<Node> nodes = (LinkedList<Node>) routeColl.get(ROUTE1).getRoute().clone();
        int size = nodes.size();
        routeColl.get(ROUTE1).setStart(routeColl.get(ROUTE1).getRoute().get(1));
        assertEquals(size, routeColl.get(ROUTE1).getRoute().size());
        for (Node n : nodes) {
            assertTrue(routeColl.get(ROUTE1).getRoute().contains(n));
        }
    }


    /**
     * Test that the route starts with the correct node after changing the start
     */
    @Test
    void testSetStartSuccess() {
        Node n = routeColl.get(ROUTE1).getRoute().get(1);
        routeColl.get(ROUTE1).setStart(routeColl.get(ROUTE1).getRoute().get(1));
        assertEquals(n, routeColl.get(ROUTE1).getRoute().get(0));
    }

    /**
     * Test that a node must be in the route to be set as the start
     */
    @Test
    void testStartFail() {
        Node n = new Node(NODE3, "node3", 20.0, 0.0);
        assertFalse(routeColl.get(ROUTE1).setStart(n));
    }
}
