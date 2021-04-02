package memorandatest;



import main.java.memoranda.*;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.FileStorage;

import com.fasterxml.jackson.core.JsonProcessingException;

import main.java.memoranda.util.Local;
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
        createRouteColl();
        createTwoDriverColl();
        createTourColl();
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
        Driver n=new Driver(1, "test driver", "555-555-1212");
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
        NodeColl nc=createNodeColl();
        RouteColl rc=createRouteColl();

        TourColl tourColl=new TourColl();
        Tour tour=tourColl.newItem();
        tour.setName("A tour");
        tour.setTime(LocalTime.of(12,0));
        tour.setRoute(rc.get(1));

        Tour tour2=tourColl.newItem();
        tour2.setName("Tour number 2");
        tour2.setTime(LocalTime.of(14,0));
        tour.setRoute(rc.get(2));

        Driver d=createNamedDriver(id, "test driver");
        d.addTour(tour);
        d.addTour(tour2);

        assertNotNull(d);

        return d;



    }




    @Test
    void testNodeConstructor(){
        final int ID=1;
        Node n=new Node(ID, "busstop1", 1.23, 3.24);
        assertEquals(ID, n.getId());
    }


    /**
     *
     * @return
     */
    NodeColl createNodeColl() throws DuplicateKeyException {
        NodeColl nc=new NodeColl();
        Node n=new Node(1, "busstop1", 1.23, 3.24);
        Node n2=new Node(2, "bus stop number 2", 2.34, -134.2331);
        nc.add(n);
        nc.add(n2);

        nodeColl=nc;
        return nodeColl;
    }


    /**
     *
     * @throws DuplicateKeyException
     */
    @Test
    void testCreateDriverTourIDs() throws DuplicateKeyException{
        Driver d=createGenericDriverWithTour(1);
        System.out.println("Tour ids="+d.getTourIDs());

//        assertEquals(2, d.getTours().size());
    }

    /**
     *
     * @throws DuplicateKeyException
     */
    @Test
    void testCreateDriverTours() throws DuplicateKeyException{
        Driver d=createGenericDriverWithTour(1);

        assertEquals(2, d.getTours().size());
    }


    /**
     *
     * @return
     * @throws DuplicateKeyException
     */
    DriverColl createTwoDriverColl() throws DuplicateKeyException {
        Driver d1=createNamedDriver(1, "Driver 1");
        Driver d2=createNamedDriver(2, "Driver 2");
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
        DriverColl dc=createTwoDriverColl();

        assertEquals(2, dc.size());
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

        DriverColl dc=createTwoDriverColl();

        stg.storeDriverList(prj, dc);

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
        NodeColl nc=createNodeColl();

        LinkedList<Integer> nli=new LinkedList<>(Arrays.asList(1, 2));
        RouteLoader rl1= new RouteLoader(1, "Route 1", nli);
        RouteLoader rl2= new RouteLoader(2, "Route 2", nli);
        LinkedList<RouteLoader> rlc=new LinkedList<>(Arrays.asList(rl1, rl2));

        RouteColl rc=new RouteColl(nc, rlc);

        routeColl=rc;
        return routeColl;
    }


    /**
     *
     */
    @Test
    void testCreateRouteColl() throws DuplicateKeyException {
        RouteColl rc=createRouteColl();

        assertEquals(2, rc.size());
    }


    /**
     *
     * @throws DuplicateKeyException
     */
    @Test
    void testAddRoute() throws DuplicateKeyException{
        RouteColl rc=createRouteColl();
        assertEquals(2, rc.size());
    }

    /**
     * Test ability to read and write JSON values
     *
     * @throws JsonProcessingException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    void testWriteRoute() throws JsonProcessingException, IOException, DuplicateKeyException{

        NodeColl nc=createNodeColl();
        RouteColl rc=createRouteColl();

        System.out.println("Save route list");
        stg.storeRouteList(prj, rc);

        System.out.println("Load route list");
        RouteColl rl=stg.openRouteList(prj, nc);

        int count=0;
        for (Route rr:rl){
            count++;
            System.out.println("Found route in list="+rr);
        }

        assertEquals(2, count);

    }


    /**
     *
     * @return
     * @throws DuplicateKeyException
     */
    Tour createTour() throws DuplicateKeyException {
        return createNamedTour("A tour");
    }

    /**
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
    Tour createNamedTourAtTime(String name, int hour, int minute) throws DuplicateKeyException {

        // route and node collections are created in @BeforeEach

        TourColl tourColl=new TourColl();
        Tour tour=tourColl.newItem();

        tour.setName(name);
        tour.setTime(LocalTime.of(hour,minute));
        tour.setRoute(routeColl.get(1));

        return tour;

    }

    @Test
    void testCreateTour() throws JsonProcessingException, IOException, DuplicateKeyException{
        assertNotNull(createTour());
    }


    /**
     *
     * @return
     */
    TourColl createTourColl() throws DuplicateKeyException {
        Tour tour=createTour();

        TourColl tourColl=new TourColl();
        tourColl.add(tour);

        return tourColl;
    }


    @Test
    void testCreateTourColl() throws JsonProcessingException, IOException, DuplicateKeyException{
        assertNotNull(createTourColl());
    }


    @Test
    void testWriteTour() throws JsonProcessingException, IOException, DuplicateKeyException{

        // route and node collections are created in @BeforeEach

        TourColl tourColl=new TourColl();
        Tour tour=tourColl.newItem();
        tour.setName("A tour");
        tour.setTime(LocalTime.of(12,0));
        tour.setRoute(routeColl.get(1));

        tourColl.add(tour);

        System.out.println("Save tour list");
        stg.storeTourList(prj, tourColl);

        System.out.println("Load tour list");
        TourColl tl=stg.openTourList(prj, routeColl);

        int count=0;
        for (Tour tt:tl){
            count++;
            System.out.println("Found route in list="+tt);
        }

        assertEquals(1, count);
    }



    /**
     * test ability to add a node
     */
    @Test
    void testAddNode() throws DuplicateKeyException {
        NodeColl nc=new NodeColl();
        Node n=new Node(1, "test", 1.23, 3.45);
        nc.add(n);

        assertEquals(1, nc.size());
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
