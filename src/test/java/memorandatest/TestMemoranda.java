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

public class TestMemoranda {

    private static Project prj;
    private static FileStorage stg;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all test methods");
        prj=ProjectManager.createProject("Test project", CalendarDate.today(), null);
        stg=new FileStorage();
        stg.createProjectStorage(prj);
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("Before each test method");
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
     * test ability to add a node
     */
    @Test
    void testAddDuplicateDriver() throws DuplicateKeyException {
        DriverColl nc=new DriverColl();
        Driver n=new Driver(1, "test driver", "555-555-1212");
        nc.add(n);

        assertThrows(DuplicateKeyException.class, () -> {nc.add(n); });
    }
    /**
     * test ability to add a node
     */
    @Test
    void testAddDriver() throws DuplicateKeyException {
        DriverColl nc=new DriverColl();
        Driver n=new Driver(1, "test driver", "555-555-1212");
        nc.add(n);

        assertEquals(1, nc.size());
    }

    /**
     * Test ability to read and write JSON values
     *
     * @throws JsonProcessingException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    void testWriteDriver() throws JsonProcessingException, IOException, DuplicateKeyException, InterruptedException {

        DriverColl nc=new DriverColl();
        Driver n=new Driver(1, "driver 1", "555-555-1213");
        Driver n2=new Driver(2, "driver 2", "202-123-3482");
        nc.add(n);
        nc.add(n2);

        System.out.println("After adding two entries, list contains "+nc.size()+" elements.");

        stg.storeDriverList(prj, nc);

        System.out.println("Load driver list");
        DriverColl dl=stg.openDriverList(prj);

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
    @Test
    RouteColl testAddRoute() throws DuplicateKeyException {
        NodeColl nc=createNodeColl();

        LinkedList<Integer> nli=new LinkedList<>(Arrays.asList(1, 2));
        RouteLoader rl1= new RouteLoader(1, "Route 1", nli);
        RouteLoader rl2= new RouteLoader(2, "Route 2", nli);
        LinkedList<RouteLoader> rlc=new LinkedList<>(Arrays.asList(rl1, rl2));

        RouteColl rc=new RouteColl(nc, rlc);

        assertEquals(2, rc.size());
        return rc;
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
        RouteColl rc=testAddRoute();

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


    @Test
    Tour testCreateTour() throws JsonProcessingException, IOException, DuplicateKeyException{
        NodeColl nc=createNodeColl();
        RouteColl rc=testAddRoute();

        TourColl tourColl=new TourColl();
        Tour tour=tourColl.newItem();
        tour.setName("A tour");
        tour.setTime(LocalTime.of(12,0));
        tour.setRoute(rc.get(1));

        assertNotNull(tour);

        return tour;
    }


    @Test
    TourColl testCreateTourColl() throws JsonProcessingException, IOException, DuplicateKeyException{

        Tour tour=testCreateTour();

        TourColl tourColl=new TourColl();
        tourColl.add(tour);

        assertNotNull(tourColl);

        return tourColl;
    }


    @Test
    void testWriteTour() throws JsonProcessingException, IOException, DuplicateKeyException{
        NodeColl nc=createNodeColl();
        RouteColl rc=testAddRoute();

        TourColl tourColl=new TourColl();
        Tour tour=tourColl.newItem();
        tour.setName("A tour");
        tour.setTime(LocalTime.of(12,0));
        tour.setRoute(rc.get(1));

        tourColl.add(tour);

        System.out.println("Save tour list");
        stg.storeTourList(prj, tourColl);

        System.out.println("Load tour list");
        TourColl tl=stg.openTourList(prj, rc);

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

    @Test
    private NodeColl createNodeColl() throws DuplicateKeyException {
        NodeColl nc=new NodeColl();
        Node n=new Node(1, "busstop1", 1.23, 3.24);
        Node n2=new Node(2, "bus stop number 2", 2.34, -134.2331);
        nc.add(n);
        nc.add(n2);
        return nc;
    }

    /**
     * Test ability to read and write JSON values
     *
     * @throws JsonProcessingException
     * @throws IOException
     */
    @Test
    void testWriteNode() throws JsonProcessingException, IOException, DuplicateKeyException{
//        FileStorage stg=new FileStorage();
//        stg.createProjectStorage(prj);

        NodeColl nc=createNodeColl();
        System.out.println("After adding two entries, list contains "+nc.size()+" elements.");

        stg.storeNodeList(nc, prj);

        System.out.println("Load node list");
        nc=stg.openNodeList(prj);

        int count=0;
        for (Node nn:nc){
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


    @Test
    void testDistanceToNull(){
        Coordinate c1;
        c1=new Coordinate(1.0, 2.0);
        assertThrows(NullPointerException.class, ()-> {c1.distanceTo(null);});
    }

    @Test
    void testCoordinateEquality(){
        Coordinate c1, c2;
        c1=new Coordinate(83.123456789, -128.987654321);
        c2 =new Coordinate(83.123456789, -128.987654321);
        assertTrue(c1.equals(c2));
        c1=new Coordinate(83.12345678, -128.987654321);
        c2 =new Coordinate(83.123456789, -128.987654321);
        assertFalse(c1.equals(c2));
        c1=new Coordinate(-83.123456789, -128.987654321);
        c2 =new Coordinate(83.123456789, -128.987654321);
        assertFalse(c1.equals(c2));
    }


}
