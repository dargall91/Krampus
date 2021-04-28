package main.java.memoranda;

import java.io.IOException;
import java.util.HashMap;

import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.Storage;

/**
 * Database control multi-singleton (per project) class.
 *
 * <p>
 * Use: Database db = Database.getDatabase(storage,project) when MTB opens to read persisted serialized data. After
 * making changes, commit them to disk with db.write(). All parts of of the program will get the same collection
 * instances because Database acts as a multi-singleton class - it will return one Database per (storage, project)
 * tuple.
 * </p>
 * This code is mildly thread-safe and will try to eliminate race conditions on read/write, but cannot compensate for
 * filesystem-level race conditions under hundreds of simultaneous reads/ writes.  Concurrent writes are safe.
 *
 * @author Brian Pape
 * @version 2021-04-06
 */
public class Database {
    /**
     * static var to hold database for open projects.
     */
    private static HashMap<String, Database> databases;
    private static int busy = 0;
    private final Storage stg;
    private final Project prj;
    private NodeColl nodeColl;
    private RouteColl routeColl;
    private TourColl tourColl;
    private DriverColl driverColl;
    private BusColl busColl;

    /**
     * Initializes the database for a given project and the default storage.
     *
     * @param prj Project to use (uses default Storage)
     */
    private Database(Project prj) throws InterruptedException, DuplicateKeyException {
        this(CurrentStorage.get(), prj);
    }

    /**
     * Initializes the database for a given project and storage.
     *
     * @param stg Storage to use
     * @param prj Project to use
     */
    private Database(Storage stg, Project prj) throws InterruptedException, DuplicateKeyException {
        if (databases == null) {
            databases = new HashMap<>();
        }
        this.prj = prj;
        this.stg = stg;
        load();
    }


    /**
     * gets database handle (singleton) for this Project, using default Storage.
     *
     * @param prj Project to use
     * @return handle to this database
     * @throws InterruptedException the thread was interrupted
     */
    public static Database getDatabase(Project prj) throws InterruptedException, DuplicateKeyException {
        return getDatabase(CurrentStorage.get(), prj);
    }


    /**
     * Gets database handle (singleton) for this Storage and Project.
     *
     * @param stg Storage to use
     * @param prj Project to use
     * @return handle to this database
     * @throws InterruptedException the thread was interrupted
     */
    public static Database getDatabase(Storage stg, Project prj) throws InterruptedException, DuplicateKeyException {
        if (!exists(prj)) {
            Database newdb = new Database(stg, prj);
            databases.put(prj.getID(), newdb);
        }
        return databases.get(prj.getID());
    }


    /**
     * lock access to database I/O before using it.
     */
    public static void lock() {
        busy++;
    }

    /**
     * unlock access to database I/O when we're done using it.
     */
    public static void unlock() {
        busy--;
    }

    /**
     * check to see if database I/O is busy.
     *
     * @return true if db I/O is busy
     */
    public static boolean isBusy() {
        return busy > 0;
    }

    /**
     * Checks if a database for a given project exists.
     *
     * @param prj check to see if database exists for this project.
     * @return whether db exists for this project
     */
    public static boolean exists(Project prj) {
        return (databases != null && databases.get(prj.getID()) != null);
    }


    /**
     * Write database files.
     *
     * @throws IOException          if disk i/o error
     * @throws InterruptedException the thread was interrupted
     */
    public void write() throws IOException, InterruptedException {
        while (isBusy()) {
            Thread.sleep(100);
        }

        lock();
        stg.storeNodeList(prj, nodeColl);
        stg.storeBusList(prj, busColl);
        stg.storeRouteList(prj, routeColl);
        stg.storeDriverList(prj, driverColl);
        stg.storeTourList(prj, tourColl);
        unlock();
    }


    private void createDefaultRoute() throws DuplicateKeyException {
        if (nodeColl.size() < 1) {
            Node start = nodeColl.newItem();
            start.setName("Start");
            start.setCoords(new Coordinate(41.9543,-87.66899));
            Node node1 = nodeColl.newItem();
            node1.setName("Stop 1");
            node1.setCoords(new Coordinate(41.9543,-87.6633));
            Node node2 = nodeColl.newItem();
            node2.setName("Stop 2");
            node2.setCoords(new Coordinate(41.94905,-87.6597));
            Node node3 = nodeColl.newItem();
            node3.setName("Stop 3");
            node3.setCoords(new Coordinate(41.94728,-87.656));
            Node node4 = nodeColl.newItem();
            node4.setName("Stop 4");
            node4.setCoords(new Coordinate(41.94363,-87.65373));
            Node node5 = nodeColl.newItem();
            node5.setName("Stop 5");
            node5.setCoords(new Coordinate(41.94,-87.65087));
            Node node6 = nodeColl.newItem();
            node6.setName("Stop 6");
            node6.setCoords(new Coordinate(41.94,-87.64451));
            Node node7 = nodeColl.newItem();
            node7.setName("Stop 7");
            node7.setCoords(new Coordinate(41.94,-87.6396));
            Node node8 = nodeColl.newItem();
            node8.setName("Stop 8");
            node8.setCoords(new Coordinate(41.94127,-87.63958));
            Node node9 = nodeColl.newItem();
            node9.setName("Stop 9");
            node9.setCoords(new Coordinate(41.94335,-87.64051));
            Node node10 = nodeColl.newItem();
            node10.setName("Stop 10");
            node10.setCoords(new Coordinate(41.9486,-87.64338));
            Node node11 = nodeColl.newItem();
            node11.setName("Stop 11");
            node11.setCoords(new Coordinate(41.95042,-87.64454));
            Node node12 = nodeColl.newItem();
            node12.setName("Stop 12");
            node12.setCoords(new Coordinate(41.95235,-87.64503));
            Node node13 = nodeColl.newItem();
            node13.setName("Stop 13");
            node13.setCoords(new Coordinate(41.95306,-87.64501));
            Node terminus = nodeColl.newItem();
            terminus.setName("End");
            terminus.setCoords(new Coordinate(41.9543,-87.64521));
            nodeColl.add(start);
            nodeColl.add(node1);
            nodeColl.add(node2);
            nodeColl.add(node3);
            nodeColl.add(node4);
            nodeColl.add(node5);
            nodeColl.add(node6);
            nodeColl.add(node7);
            nodeColl.add(node8);
            nodeColl.add(node9);
            nodeColl.add(node10);
            nodeColl.add(node11);
            nodeColl.add(node12);
            nodeColl.add(node13);
            nodeColl.add(terminus);
            Route route = routeColl.newItem();
            route.setName("Krampus Route");
            route.addNode(start);
            route.addNode(node1);
            route.addNode(node2);
            route.addNode(node11);
            route.addNode(node3);
            route.addNode(node4);
            route.addNode(node5);
            route.addNode(node6);
            route.addNode(node9);
            route.addNode(node8);
            route.addNode(node7);
            route.addNode(node10);
            route.addNode(node12);
            route.addNode(node13);
            route.addNode(terminus);
            routeColl.add(route);
        }

    }

    /**
     * Load database files.
     *
     * @throws InterruptedException the thread was interrupted
     */
    public void load() throws InterruptedException, DuplicateKeyException {

        while (isBusy()) {
            Thread.sleep(100);
        }
        try {
            nodeColl = stg.openNodeList(prj);
            busColl = stg.openBusList(prj);
            routeColl = stg.openRouteList(prj, nodeColl);
            tourColl = stg.openTourList(prj, routeColl, busColl);
            driverColl = stg.openDriverList(prj, tourColl);
        } catch (IOException | DuplicateKeyException e) {
            initialize();
        }
        createDefaultRoute();
    }

    /**
     * Initialize empty collections.
     */
    private void initialize() throws DuplicateKeyException {
        nodeColl = new NodeColl();
        routeColl = new RouteColl();
        tourColl = new TourColl();
        driverColl = new DriverColl();
        busColl = new BusColl();
        createDefaultRoute();
    }

    /**
     * Return node collection for this database.
     *
     * @return node collection
     */
    public NodeColl getNodeColl() {
        return nodeColl;
    }

    /**
     * Return route collection for this database.
     *
     * @return route collection
     */
    public RouteColl getRouteColl() {
        return routeColl;
    }

    /**
     * Return tour collection for this database.
     *
     * @return tour collection
     */
    public TourColl getTourColl() {
        return tourColl;
    }

    /**
     * Return driver collection for this database.
     *
     * @return driver collection
     */
    public DriverColl getDriverColl() {
        return driverColl;
    }

    /**
     * Return bus collection for this database.
     *
     * @return bus collection
     */
    public BusColl getBusColl() {
        return busColl;
    }
}
