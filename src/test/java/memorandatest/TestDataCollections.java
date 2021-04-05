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

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all test methods");
        prj=ProjectManager.createProject("Test project", CalendarDate.today(), null);
        stg=new FileStorage();
        stg.createProjectStorage(prj);
    }

    @BeforeEach
    void beforeEach() throws DuplicateKeyException {
        createNodeColl();
        createBusColl();
        createRouteColl();
        createTourColl();
        createDriverColl();
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each test method");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all test methods");
//        stg.removeProjectStorage(prj);
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
     *
     * @param id
     * @return
     */
    Driver createGenericDriver(int id){
        return createNamedDriver(id, "test driver");
    }

    /**
     *
     * @param id
     * @param name
     * @return
     */
    Driver createNamedDriver(int id, String name){
        return new Driver(id, name, "555-555-1212");
    }

    /**
     *
     * @param id
     * @return
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
     * validate that adding a tour to a driver adds the driver to the tour
     */
    @Test
    void testAddTourToDriver() throws DuplicateKeyException {
        Driver driver=createNamedDriver(1, "Fred");
        Tour tour=createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);
        assertEquals(driver,tour.getDriver());
    }

    /**
     * validate that adding a tour to a driver adds the driver to the tour
     */
    @Test
    void testAddTourToTwoDrivers() throws DuplicateKeyException {
        Driver driver=createNamedDriver(1, "Fred");
        Driver driver2=createNamedDriver(1, "Jim");
        Tour tour=createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);
        assertThrows(DuplicateKeyException.class, () -> {driver2.addTour(tour);} );
    }

    /**
     * validate that removing a tour from a driver removes the driver from the tour
     */
    @Test
    void testRemoveTourFromDriver() throws DuplicateKeyException {
        Driver driver=createNamedDriver(1, "Fred");
        Tour tour=createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);
        driver.delTour(tour);
        assertNull(tour.getDriver());
    }
    /**
     * validate that removing a tour from a driver removes the driver from the tour
     */
    @Test
    void testRemoveTourFromDriver2() throws DuplicateKeyException {
        Driver driver=createNamedDriver(1, "Fred");
        Tour tour=createNamedTourAtTime("Tour 1", 13, 15);
        driver.addTour(tour);
        driver.delTour(tour);
        assertNull(driver.getTour(tour.getID()));
    }

    /**
     *
     */
    @Test
    void testNodeConstructor(){
        final int ID=1;
        Node n=new Node(ID, "busstop1", 1.23, 3.24);
        assertEquals(ID, n.getID());
    }


    /**
     *
     * @return
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
     *
     * @return
     * @throws DuplicateKeyException
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
     *
     */
    @Test
    void testCreateBus(){
        Bus b1=busColl.newItem();
        b1.setNumber(BUS1);

        assertEquals(BUS1, b1.getNumber());
    }


    /**
     *
     */
    @Test
    void testBusCollCreation(){
        assertNotNull(busColl);
    }


    /**
     *
     */
    @Test
    void testBusCollStatus(){
        assertEquals(2, busColl.size());
    }


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
     *
     * @throws DuplicateKeyException
     */
    @Test
    void testCreateDriverTourIDs() throws DuplicateKeyException{
        Driver d=createGenericDriverWithTour(DRIVER1);
        System.out.println("Tour ids="+d.getTourIDs());

//        assertEquals(2, d.getTours().size());
    }

    /**
     *
     * @throws DuplicateKeyException
     */
    @Test
    void testCreateDriverTours() throws DuplicateKeyException{
        Driver d=createGenericDriverWithTour(DRIVER1);

        assertEquals(2, d.getTours().size());
    }


    /**
     *
     * @return
     * @throws DuplicateKeyException
     */
    DriverColl createDriverColl() throws DuplicateKeyException {

        // tourColl created in @beforeEach

        System.out.println("TourColl="+tourColl);
        System.out.println("TourColl.size()="+tourColl.size());
        System.out.println("tour1="+tourColl.get(TOUR1));
        System.out.println("tour2="+tourColl.get(TOUR2));

        Driver d1=createNamedDriver(DRIVER1, "Driver 1");
        d1.addTour(tourColl.get(TOUR1));
        System.out.println("Tour1 driver="+tourColl.get(TOUR1).getDriver());

        Driver d2=createNamedDriver(DRIVER2, "Driver 2");
        d2.addTour(tourColl.get(TOUR2));
        System.out.println("Tour2 driver="+tourColl.get(TOUR2).getDriver());

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

        // driverColl created in @BeforeEach

        assertEquals(2, driverColl.size());
    }

    /**
     * Test ability to read and write JSON values
     *
     * @throws JsonProcessingException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    void testWriteDriverColl() throws JsonProcessingException, IOException, DuplicateKeyException, InterruptedException {

        stg.storeDriverList(prj, driverColl);

        // need to create all new collections so tours aren't already occupied by drivers
        createNodeColl();
        createBusColl();
        createRouteColl();
        createTourColl();


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
     * test ability to add a node
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
     * test ability to create a route.
     *
     * @throws DuplicateKeyException if route does not have unique ID
     */
    @Test
    void testCreateRouteColl() throws DuplicateKeyException {
        RouteColl rc=createRouteColl();

        assertEquals(2, rc.size());
    }


    /**
     * test ability to add a route to a collection.
     *
     * @throws DuplicateKeyException if route does not have unique ID
     */
    @Test
    void testAddRoute() throws DuplicateKeyException{
        RouteColl rc=createRouteColl();
        assertEquals(2, rc.size());
    }

    /**
     * Test ability to read and write JSON values
     *
     * @throws JsonProcessingException if json error occurs
     * @throws IOException if file read/write error occurs
     * @throws DuplicateKeyException if non-unique ID is encountered
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
     * Utility function to create a tour.
     *
     * @return
     * @throws DuplicateKeyException
     */
    Tour createTour() throws DuplicateKeyException {
        return createNamedTour("A tour");
    }

    /**
     * Utility functoin to create a named tour.
     *
     * @param name
     * @return
     */
    Tour createNamedTour(String name) throws DuplicateKeyException {
        return createNamedTourAtTime(name, 12, 0);
    }


    /**
     *
     * @param name
     * @param hour
     * @param minute
     * @return
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
     *
     * @throws JsonProcessingException
     * @throws IOException
     * @throws DuplicateKeyException
     */
    @Test
    void testCreateTourIDs(){
        assertNotNull(tourColl.get(TOUR1));
        assertNotNull(tourColl.get(TOUR2));
    }

    /**
     *
     * @throws JsonProcessingException
     * @throws IOException
     * @throws DuplicateKeyException
     */
    @Test
    void testCreateTour() throws DuplicateKeyException{
        assertNotNull(createTour());
    }


    /**
     *
     * @return
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


    @Test
    void testCreateTourColl(){
        assertNotNull(tourColl);
    }


    @Test
    void testWriteTour() throws IOException, DuplicateKeyException{

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
     * @throws JsonProcessingException
     * @throws IOException
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
     *
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
     *
     */
    @Test
    void testDistanceToNull(){
        Coordinate c1;
        c1=new Coordinate(1.0, 2.0);
        assertThrows(NullPointerException.class, ()-> {c1.distanceTo(null);});
    }

    /**
     *
     */
    @Test
    void testCoordinateEquality() {
        Coordinate c1, c2;
        c1 = new Coordinate(83.123456789, -128.987654321);
        c2 = new Coordinate(83.123456789, -128.987654321);
        assertTrue(c1.equals(c2));
    }

    /**
     *
     */
    @Test
    void testCoordinateInequalityDigits() {
        Coordinate c1, c2;
        c1 = new Coordinate(83.12345678, -128.987654321);
        c2 = new Coordinate(83.123456789, -128.987654321);
        assertFalse(c1.equals(c2));
    }

    /**
     *
     */
    @Test
    void testCoordinateInequalitySign(){
        Coordinate c1, c2;
        c1=new Coordinate(-83.123456789, -128.987654321);
        c2 =new Coordinate(83.123456789, -128.987654321);
        assertFalse(c1.equals(c2));
    }


}
