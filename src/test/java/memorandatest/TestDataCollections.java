package memorandatest;



import main.java.memoranda.*;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.FileStorage;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;

public class TestDataCollections {

    private static Project prj;
    private static FileStorage stg;

    private static NodeColl nodeColl;
    private static RouteColl routeColl;
    private static TourColl tourColl;
    private static DriverColl driverColl;
    private static BusColl busColl;

    private final static int NODE1=1;
    private final static int NODE2=2;

    private final static int DRIVER1=1;
    private final static int DRIVER2=2;

    private final static int ROUTE1=1;
    private final static int ROUTE2=2;

    private final static int TOUR1=1;
    private final static int TOUR2=2;

    private final static int BUS1=1;
    private final static int BUS2=2;

    /**
     * Run before all test methods
     *
     * We need to create a project and file storage object for use by all test methods
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all test methods");
        prj=ProjectManager.createProject("Test project", CalendarDate.today(), null);
        stg=new FileStorage();
        stg.createProjectStorage(prj);
    }

    /**
     * Run before each test method
     *
     * We need to have a known test fixture for most tests
     *
     * @throws DuplicateKeyException
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
     * Run after each test method
     *
     * No tear-down needed
     *
     */
    @AfterEach
    void afterEach() {
        System.out.println("After each test method");
    }

    /**
     *
     * Run after all test methods
     *
     * Need to remove temporary project storage
     */
    @AfterAll
    static void afterAll() {
        System.out.println("After all test methods");
        stg.removeProjectStorage(prj);
    }


    /**
     * test error handling for duplicate driver in collection
     */
    @Test
    void testAddDuplicateDriverToCollection() throws DuplicateKeyException {
        DriverColl nc=new DriverColl();
        Driver n=new Driver(DRIVER1, "test driver", "555-555-1212");
        nc.add(n);

        assertThrows(DuplicateKeyException.class, () -> {nc.add(n); });
    }


    /**
     * Utility method: Create a generic driver with a name
     *
     * @param id the id for the driver
     * @param name the name for the driver
     * @return a Driver object
     */
    Driver createNamedDriver(int id, String name){
        return new Driver(id, name, "555-555-1212");
    }

    /**
     * Utility method: Create a generic driver associated with a tour
     *
     * @param id A the id for the driver
     * @return the Driver object
     */
    Driver createGenericDriverWithTour(int id) throws DuplicateKeyException {
        // routeColl, busColl set in @BeforeAll

        TourColl tourColl=new TourColl();
        Tour tour=tourColl.newItem();
        tour.setName("A tour");
        tour.setTime(LocalTime.of(12,0));
        tour.setRoute(routeColl.get(ROUTE1));
        tour.setBus(busColl.get(BUS1));

        Tour tour2=tourColl.newItem();
        tour2.setName("Tour number 2");
        tour2.setTime(LocalTime.of(14,0));
        tour2.setRoute(routeColl.get(ROUTE2));
        tour2.setBus(busColl.get(BUS2));


        Driver d=createNamedDriver(id, "test driver");
        d.addTour(tour);
        d.addTour(tour2);

        assertNotNull(d);

        return d;
    }


    /**
     * Test the basic node constructor
     */
    @Test
    void testNodeConstructor(){
        final int ID=1;
        Node n=new Node(ID, "busstop1", 1.23, 3.24);
        assertEquals(ID, n.getID());
    }


    /**
     * Utility method: Create a node collection
     *
     * @return a node collection
     */
    NodeColl createNodeColl() throws DuplicateKeyException {
        NodeColl nc=new NodeColl();
        Node n=new Node(NODE1, "busstop1", 1.23, 3.24);
        Node n2=new Node(NODE2, "bus stop number 2", 2.34, -134.2331);
        nc.add(n);
        nc.add(n2);

        nodeColl=nc;
        return nodeColl;
    }


    /**
     * Utility method: create a bus collection
     *
     * @return a bus collection
     * @throws DuplicateKeyException if duplicate key is added to collection
     */
    BusColl createBusColl() throws DuplicateKeyException{
        busColl= new BusColl();

        Bus b1=busColl.newItem();
        b1.setNumber(BUS1);
        Bus b2=busColl.newItem();
        b2.setNumber(BUS2);

        busColl.add(b1);
        busColl.add(b2);

        System.out.println("In createBusColl: Bus list contains "+busColl.get(BUS1)+", "+busColl.get(BUS2));

        return busColl;
    }


    /**
     * test creating a bus object
     */
    @Test
    void testCreateBus(){
        Bus b1=busColl.newItem();
        b1.setNumber(BUS1);

        assertEquals(BUS1, b1.getNumber());
    }


    /**
     * test creating a bus collection
     */
    @Test
    void testBusCollCreation(){
        assertNotNull(busColl);
    }


    /**
     * test adding several buses to a bus collection
     */
    @Test
    void testBusCollStatus(){
        assertEquals(2, busColl.size());
    }


    /**
     * test serializing a bus collection
     *
     * @throws IOException if output file cannot be opened
     * @throws DuplicateKeyException if duplicate key is added to bus collection
     */
    @Test
    void testWriteBusColl() throws IOException, DuplicateKeyException {

        System.out.println("Bus list contains "+busColl.get(BUS1)+", "+busColl.get(BUS2));
        stg.storeBusList(prj, busColl);

        System.out.println("Load bus list");
        BusColl bl=stg.openBusList(prj);

        int count=0;
        for (Bus bb:bl){
            count++;
            System.out.println("Found bus in list="+bb);
        }

        assertEquals(2, count);
    }


    /**
     * test creating a driver with multiple tours
     *
     * @throws DuplicateKeyException if duplicate IDs are added to collection
     */
//    @Test
//    void testCreateDriverTourIDs() throws DuplicateKeyException{
//        Driver d=createGenericDriverWithTour(DRIVER1);
//        System.out.println("Tour ids="+d.getTourIDs());
//
//        assertEquals(2, d.getTours().size());
//    }

    /**
     * test creating a driver with multiple tours
     *
     * @throws DuplicateKeyException if duplicate IDs are added to collection
     */
    @Test
    void testCreateDriverTours() throws DuplicateKeyException{
        Driver d=createGenericDriverWithTour(DRIVER1);

        assertEquals(2, d.getTours().size());
    }


    /**
     * test creating a driver collection
     *
     * @return a driver collection
     * @throws DuplicateKeyException if duplicate IDs are added to collection
     */
    DriverColl createDriverColl() throws DuplicateKeyException {

        // tourColl created in @beforeEach

        Driver d1=createNamedDriver(DRIVER1, "Driver 1");

        System.out.println("TourColl="+tourColl);
        System.out.println("TourColl.size()="+tourColl.size());
        System.out.println("tour1="+tourColl.get(TOUR1));
        System.out.println("tour2="+tourColl.get(TOUR2));

        d1.addTour(tourColl.get(TOUR1));
        Driver d2=createNamedDriver(DRIVER2, "Driver 2");
        d2.addTour(tourColl.get(TOUR2));

        DriverColl dc=new DriverColl();
        dc.add(d1);
        dc.add(d2);

        driverColl=dc;
        return driverColl;
    }

    /**
     * test ability to add a node
     */
    @Test
    void testCreateDriverColl() throws DuplicateKeyException {
        DriverColl dc= createDriverColl();

        assertEquals(2, dc.size());
    }

    /**
     * Test ability to read and write JSON values (serialize/deserialize data)
     *
     * @throws JsonProcessingException if json error occurs
     * @throws IOException if output file cannot be written
     */
    @Test
    void testWriteDriverColl() throws JsonProcessingException, IOException, DuplicateKeyException {

        stg.storeDriverList(prj, driverColl);

        System.out.println("Load driver list");
        DriverColl dl=stg.openDriverList(prj, tourColl);

        int count=0;
        for (Driver nn:dl){
            count++;
            System.out.println("Found driver in list="+nn);
        }

        assertEquals(2, count);
    }


    /**
     * test creating a route collection
     *
     * @return route collection
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    RouteColl createRouteColl() throws DuplicateKeyException {

        LinkedList<Integer> nli=new LinkedList<>(Arrays.asList(NODE1, NODE2));
        RouteLoader rl1= new RouteLoader(ROUTE1, "Route 1", nli);
        RouteLoader rl2= new RouteLoader(ROUTE2, "Route 2", nli);
        LinkedList<RouteLoader> rlc=new LinkedList<>(Arrays.asList(rl1, rl2));

        RouteColl rc=new RouteColl(nodeColl, rlc);

        routeColl=rc;
        return routeColl;
    }


    /**
     * test creating a route collection
     *
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testCreateRouteColl() throws DuplicateKeyException {
        RouteColl rc=createRouteColl();

        assertEquals(2, rc.size());
    }


    /**
     * test creating a route
     *
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testAddRoute() throws DuplicateKeyException{
        RouteColl rc=createRouteColl();
        assertEquals(2, rc.size());
    }


    /**
     * Test ability to read and write JSON values
     *
     * @throws JsonProcessingException if Json error occurs
     * @throws IOException if output file cannot be written
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testWriteRoute() throws JsonProcessingException, IOException, DuplicateKeyException{

        // nodeColl and routeColl created in @BeforeAll
        System.out.println("Save route list");
        stg.storeRouteList(prj, routeColl);

        System.out.println("Load route list");
        RouteColl rl=stg.openRouteList(prj, nodeColl);

        int count=0;
        for (Route rr:rl){
            count++;
            System.out.println("Found route in list="+rr);
        }

        assertEquals(2, count);

    }


    /**
     * Utility function: create a generic tour
     *
     * @return Tour object
     */
    Tour createTour() {
        return createNamedTour("A tour");
    }

    /**
     * Utility function: create a tour with a given name
     *
     * @param name the name for the tour
     * @return Tour object
     */
    Tour createNamedTour(String name) {
        return createNamedTourAtTime(name, 12, 0);
    }


    /**
     * Utility function: create a named tour at a particular time
     *
     * @param name the name for the tour
     * @param hour the hour of the tour
     * @param minute the minute for the tour
     * @return a Tour object
     */
    Tour createNamedTourAtTime(String name, int hour, int minute){

        // route and node collections are created in @BeforeEach

        Tour tour=tourColl.newItem();

        tour.setName(name);
        tour.setTime(LocalTime.of(hour,minute));
        tour.setRoute(routeColl.get(ROUTE1));
        tour.setBus(busColl.get(BUS1));

        return tour;
    }

    /**
     * test creating tours and valid IDs
     *
     * @throws JsonProcessingException if Json error occurs
     * @throws IOException if output file cannot be written
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testCreateTourIDs(){
        assertNotNull(tourColl.get(TOUR1));
        assertNotNull(tourColl.get(TOUR2));
    }

    /**
     * test creating a tour
     *
     * @throws JsonProcessingException if Json error occurs
     * @throws IOException if output file cannot be written
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testCreateTour() throws DuplicateKeyException{
        assertNotNull(createTour());
    }


    /**
     * Utility function: test creating a tour collection
     *
     * @return a tour collection
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    TourColl createTourColl() throws DuplicateKeyException {
        System.out.println("in createTourColl");
        tourColl=new TourColl();

        Tour tour1=createTour();
        Tour tour2=createNamedTourAtTime("A long tour", 17, 0);

        tourColl.add(tour1);
        tourColl.add(tour2);

        return tourColl;
    }


    /**
     * test creating a tour collection
     */
    @Test
    void testCreateTourColl(){
        assertNotNull(tourColl);
    }


    /**
     * test serializing/deserializing a Tour collection
     *
     * @throws IOException if output file cannot be written
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testWriteTour() throws IOException, DuplicateKeyException {

        // route and node collections are created in @BeforeEach

        System.out.println("Save tour list");
        stg.storeTourList(prj, tourColl);

        System.out.println("Load tour list");
        TourColl tl=stg.openTourList(prj, routeColl, busColl);

        int count=0;
        for (Tour tt:tl){
            count++;
            System.out.println("Found route in list="+tt);
        }

        assertEquals(2, count);
    }



    /**
     * test ability to add a node
     */
    @Test
    void testAddNode() throws DuplicateKeyException {
        NodeColl nc=new NodeColl();
        Node n=new Node(NODE1, "test", 1.23, 3.45);
        nc.add(n);

        assertEquals(NODE1, nc.size());
    }

    /**
     * Test ability to read and write JSON values
     *
     * @throws JsonProcessingException if Json error occurs
     * @throws IOException if output file cannot be written
     * @throws DuplicateKeyException if duplicate id is added to collection
     */
    @Test
    void testWriteNode() throws JsonProcessingException, IOException, DuplicateKeyException{

        // node collection create in @BeforeEach

        System.out.println("After adding two entries, list contains "+nodeColl.size()+" elements.");

        stg.storeNodeList(nodeColl, prj);

        System.out.println("Load node list");
        nodeColl=stg.openNodeList(prj);

        int count=0;
        for (Node nn:nodeColl){
            count++;
            System.out.println("Found node in list="+nn);
        }

        assertEquals(2, count);

    }

    /**
     * Test ability to create and remove project storage directory in $HOME/.memoranda
     */
    @Test
    void testProjectStorage(){
        Project prj=ProjectManager.createProject("Test project", CalendarDate.today(), null);
//        Element el = new Element("project");
//        el.addAttribute(new Attribute("id", id));
        FileStorage stg=new FileStorage();
//        Project prj=new ProjectImpl(el);
        stg.createProjectStorage(prj);
        stg.removeProjectStorage(prj);
    }


    /**
     * Test haversine distance formula calculations
     */
    @Test
    void testHaversine(){
        Coordinate c1=new Coordinate(41.507483, -99.436554);
        Coordinate c2 =new Coordinate(38.504048, -98.315949);
        System.out.println("distance="+c1.distanceTo(c2));
        assertEquals(c1.distanceTo(c2), 347.3, 0.1);

        c1=new Coordinate(38.504048, -98.315949);
        c2 =new Coordinate(41.507483, -99.436554);
        System.out.println("distance="+c1.distanceTo(c2));
        assertEquals(c1.distanceTo(c2), 347.3, 0.1);
    }


    /**
     *A Test distance to a null coordinate
     */
    @Test
    void testDistanceToNull(){
        Coordinate c1;
        c1=new Coordinate(1.0, 2.0);
        assertThrows(NullPointerException.class, ()-> {c1.distanceTo(null);});
    }

    /**
     * test coordinate equality
     */
    @Test
    void testCoordinateEquality() {
        Coordinate c1, c2;
        c1 = new Coordinate(83.123456789, -128.987654321);
        c2 = new Coordinate(83.123456789, -128.987654321);
        assertTrue(c1.equals(c2));
    }

    /**
     * test coordinate inequality based upon significant digit difference
     */
    @Test
    void testCoordinateInequalityDigits() {
        Coordinate c1, c2;
        c1 = new Coordinate(83.12345678, -128.987654321);
        c2 = new Coordinate(83.123456789, -128.987654321);
        assertFalse(c1.equals(c2));
    }

    /**
     * check for coordinate inequality based on sign
     */
    @Test
    void testCoordinateInequalitySign(){
        Coordinate c1, c2;
        c1=new Coordinate(-83.123456789, -128.987654321);
        c2 =new Coordinate(83.123456789, -128.987654321);
        assertFalse(c1.equals(c2));
    }


}
