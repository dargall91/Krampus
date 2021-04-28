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
            Node node1 = nodeColl.newItem();
            node1.setName("ASU NW");
            node1.setCoords(new Coordinate(33.431245, -111.943588));
            Node node2 = nodeColl.newItem();
            node2.setName("ASU SE");
            node2.setCoords(new Coordinate(33.411095, -111.926076));
            nodeColl.add(node1);
            nodeColl.add(node2);
            Route route = routeColl.newItem();
            route.setName("ASU Route");
            route.addNode(node1);
            route.addNode(node2);
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
