package memoranda.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import main.java.memoranda.util.FileStorage;

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
    void testWrite() throws JsonProcessingException {
        FileStorage stg=new FileStorage();
        stg.storeNodeList();

    }



}