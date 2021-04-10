package main.java.memoranda;

import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.FileStorage;
import main.java.memoranda.util.Storage;

import java.io.IOException;
import java.util.HashMap;

public class Database {
    private NodeColl nodeColl;
    private RouteColl routeColl;
    private TourColl tourColl;
    private DriverColl driverColl;
    private BusColl busColl;
    private Storage stg;
    private Project prj;

    /**
     * static var to hold database for open projects
     */
    private static HashMap<String, Database> databases;

    /**
     * @param prj
     */
    private Database(Project prj) {
        this(CurrentStorage.get(), prj);
    }

    /**
     * @param stg
     * @param prj
     */
    private Database(Storage stg, Project prj) {
        if (databases == null) {
            databases = new HashMap<>();
        }
        this.prj = prj;
        this.stg = stg;
        load();
    }


    /**
     * @param prj
     * @return
     */
    public static Database getDatabase(Project prj) {
        return getDatabase(CurrentStorage.get(), prj);
    }


    /**
     * @param stg
     * @param prj
     * @return
     */
    public static Database getDatabase(Storage stg, Project prj) {
        if (!exists(prj)) {
            Database newdb = new Database(prj);
            databases.put(prj.getID(), newdb);
        }
        return databases.get(prj.getID());
    }


    /**
     * @param prj
     * @return
     */
    public static boolean exists(Project prj) {
        return (databases != null && databases.get(prj.getID()) != null);
    }


    /**
     * @throws IOException
     */
    public void write() throws IOException {
        CurrentStorage.get().storeNodeList(prj, nodeColl);
        CurrentStorage.get().storeBusList(prj, busColl);
        CurrentStorage.get().storeRouteList(prj, routeColl);
        CurrentStorage.get().storeDriverList(prj, driverColl);
        CurrentStorage.get().storeTourList(prj, tourColl);
    }


    /**
     *
     */
    public void load() {
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
     *
     */
    private void initialize() {
        nodeColl = new NodeColl();
        routeColl = new RouteColl();
        tourColl = new TourColl();
        driverColl = new DriverColl();
        busColl = new BusColl();
    }


    public NodeColl getNodeColl() {
        return nodeColl;
    }

    public RouteColl getRouteColl() {
        return routeColl;
    }

    public TourColl getTourColl() {
        return tourColl;
    }

    public DriverColl getDriverColl() {
        return driverColl;
    }

    public BusColl getBusColl() {
        return busColl;
    }
}
