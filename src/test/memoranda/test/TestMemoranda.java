package memoranda.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import main.java.memoranda.*;
import main.java.memoranda.date.CalendarDate;
import nu.xom.Attribute;
import nu.xom.Element;
import org.junit.jupiter.api.*;
import main.java.memoranda.util.FileStorage;

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

    @Test
    void testAddNode(){
        NodeColl nc=new NodeColl();
        Node n=new Node(1, "test", 1.23, 3.45);
        nc.addNode(n);
    }

    @Test
    void testWrite() throws JsonProcessingException, IOException, InterruptedException {
        Project prj=ProjectManager.createProject("Test project", CalendarDate.today(), null);
        FileStorage stg=new FileStorage();
        stg.createProjectStorage(prj);

        NodeColl nc=new NodeColl();
        Node n=new Node(1, "busstop1", 1.23, 3.24);
        Node n2=new Node(2, "bus stop number 2", 2.34, -134.2331);
        nc.addNode(n);
        nc.addNode(n2);

        stg.storeNodeList(nc, prj);

        System.out.println("Load node list");
        stg.loadNodeList(prj);
//        Thread.sleep(20000);

//        stg.removeProjectStorage(prj);

    }

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



}