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

public class TestMemoranda {

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all test methods");
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
        Project prj=ProjectManager.createProject("Test project", CalendarDate.today(), null);
        FileStorage stg=new FileStorage();
        stg.createProjectStorage(prj);

        DriverColl nc=new DriverColl();
        Driver n=new Driver(1, "driver 1", "555-555-1213");
        Driver n2=new Driver(2, "driver 2", "202-123-3482");
        nc.add(n);
        nc.add(n2);

        System.out.println("After adding two entries, list contains "+nc.size()+" elements.");

        stg.storeDriverList(nc, prj);

        System.out.println("Load driver list");
        DriverColl dl=stg.openDriverList(prj);

        int count=0;
        for (Driver nn:dl){
            count++;
            System.out.println("Found driver in list="+nn);
        }

        assertEquals(2, count);

        stg.removeProjectStorage(prj);
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
     * @throws InterruptedException
     */
    @Test
    void testWriteNode() throws JsonProcessingException, IOException, DuplicateKeyException, InterruptedException {
        Project prj=ProjectManager.createProject("Test project", CalendarDate.today(), null);
        FileStorage stg=new FileStorage();
        stg.createProjectStorage(prj);

        NodeColl nc=new NodeColl();
        Node n=new Node(1, "busstop1", 1.23, 3.24);
        Node n2=new Node(2, "bus stop number 2", 2.34, -134.2331);
        nc.add(n);
        nc.add(n2);

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

        stg.removeProjectStorage(prj);
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
        Coordinate c2=new Coordinate(38.504048, -98.315949);
        System.out.println("distance="+c1.distanceTo(c2));
        assertEquals(c1.distanceTo(c2), 347.3, 0.1);

        c2=new Coordinate(41.507483, -99.436554);
        c1=new Coordinate(38.504048, -98.315949);
        System.out.println("distance="+c1.distanceTo(c2));
        assertEquals(c1.distanceTo(c2), 347.3, 0.1);
    }


}
