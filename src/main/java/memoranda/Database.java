package main.java.memoranda;

import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.Storage;

import java.io.IOException;
import java.util.HashMap;


/**
 * Database control multi-singleton (per project) class.
 *
 * @author Brian Pape
 * @version 2021-04-06
 */
public class Database {
    private NodeColl nodeColl;
    private RouteColl routeColl;
    private TourColl tourColl;
    private DriverColl driverColl;
    private BusColl busColl;
    private Storage stg;
    private Project prj;

    /**
     * static var to hold database for open projects.
     */
    private static HashMap<String, Database> databases;
    private static int busy = 0;

    /**
     * @param prj Project to use (uses default Storage)
     */
    private Database(Project prj) throws InterruptedException {
        this(CurrentStorage.get(), prj);
    }

    /**
     * @param stg Storage to use
     * @param prj Project to use
     */
    private Database(Storage stg, Project prj) throws InterruptedException {
        if (databases == null) {
            databases = new HashMap<>();
        }
        this.prj = prj;
        this.stg = stg;
        load();
    }


    /**
     * gets database handle (singleton) for this Project, using default Storage
     *
     * @param prj Project to use
     * @return handle to this database
     */
    public static Database getDatabase(Project prj) throws InterruptedException {
        return getDatabase(CurrentStorage.get(), prj);
    }


    /**
     * gets database handle (singleton) for this Storage and Project.
     *
     * @param stg Storage to use
     * @param prj Project to use
     * @return handle to this database
     */
    public static Database getDatabase(Storage stg, Project prj) throws InterruptedException {
        if (!exists(prj)) {
            Database newdb = new Database(prj);
            databases.put(prj.getID(), newdb);
        }
        return databases.get(prj.getID());
    }


    /**
     * @param prj check to see if database exists for this project.
     *
     * @return whether db exists for this project
     */
    public static boolean exists(Project prj) {
        return (databases != null && databases.get(prj.getID()) != null);
    }


    /**
     * write database files.
     *
     * @throws IOException if disk i/o error
     */
    public void write() throws IOException, InterruptedException {
        while (busy > 0) {
            Thread.sleep(100);
        }
        busy++;
        CurrentStorage.get().storeNodeList(prj, nodeColl);
        CurrentStorage.get().storeBusList(prj, busColl);
        CurrentStorage.get().storeRouteList(prj, routeColl);
        CurrentStorage.get().storeDriverList(prj, driverColl);
        CurrentStorage.get().storeTourList(prj, tourColl);
        busy--;
    }


    /**
     * load database files.
     */
    public void load() throws InterruptedException {

        while (busy > 0) {
            Thread.sleep(100);
        }
        try {
            nodeColl = CurrentStorage.get().openNodeList(prj);
            busColl = CurrentStorage.get().openBusList(prj);
            routeColl = CurrentStorage.get().openRouteList(prj, nodeColl);
            tourColl = CurrentStorage.get().openTourList(prj, routeColl, busColl);
            driverColl = CurrentStorage.get().openDriverList(prj, tourColl);
        } catch (IOException | DuplicateKeyException e) {
            initialize();
        }
    }


    /**
     * initialize empty collections.
     */
    private void initialize() {
        nodeColl = new NodeColl();
        routeColl = new RouteColl();
        tourColl = new TourColl();
        driverColl = new DriverColl();
        busColl = new BusColl();
    }


    /**
     * return node collection for this database.
     *
     * @return node collection
     */
    public NodeColl getNodeColl() {
        return nodeColl;
    }

    /**
     * return route collection for this database.
     *
     * @return route collection
     */
    public RouteColl getRouteColl() {
        return routeColl;
    }

    /**
     * return tour collection for this database.
     *
     * @return tour collection
     */
    public TourColl getTourColl() {
        return tourColl;
    }

    /**
     * return driver collection for this database.
     *
     * @return driver collection
     */
    public DriverColl getDriverColl() {
        return driverColl;
    }

    /**
     * return bus collection for this database.
     *
     * @return bus collection
     */
    public BusColl getBusColl() {
        return busColl;
    }
}
