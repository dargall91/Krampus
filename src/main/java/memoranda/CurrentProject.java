/**
 * CurrentProject.java Created on 13.02.2003, 13:16:52 Alex Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */

package main.java.memoranda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import main.java.memoranda.ui.AppFrame;
import main.java.memoranda.ui.ExceptionDialog;
import main.java.memoranda.util.Context;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.Storage;

/**
 * CurrentProject holds the various data collections required for the software to run.
 * Used in a static context, you can get a reference to these collections
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net, Derek Argall
 * @version 04/05/2020
 */
public class CurrentProject {

    private static Project _project = null;
    private static final Vector projectListeners = new Vector();
    private static DriverColl _drivers = null;
    private static TourColl _tours = null;
    private static RouteColl _routes = null;
    private static BusColl _buses = null;
    private static NodeColl _nodes = null;
    private static Database db;

    static {
        String prjId = (String) Context.get("LAST_OPENED_PROJECT_ID");
        if (prjId == null) {
            prjId = "__default";
            Context.put("LAST_OPENED_PROJECT_ID", prjId);
        }
        //ProjectManager.init();
        _project = ProjectManager.getProject(prjId);

        if (_project == null) {
            // alexeya: Fixed bug with NullPointer when LAST_OPENED_PROJECT_ID
            // references to missing project
            _project = ProjectManager.getProject("__default");
            if (_project == null) {
                _project = (Project) ProjectManager.getActiveProjects().get(0);
            }
            Context.put("LAST_OPENED_PROJECT_ID", _project.getID());

        }

        try {
            db = Database.getDatabase(_project);
            _nodes = db.getNodeColl();
            _buses = db.getBusColl();
            _routes = db.getRouteColl();
            _tours = db.getTourColl();
            _drivers = db.getDriverColl();
        } catch (InterruptedException | DuplicateKeyException e) {
            new ExceptionDialog(e);
        }
        AppFrame.addExitListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
    }


    /**
     * Gets the project.
     *
     * @return the current project
     */
    public static Project get() {
        return _project;
    }

    /**
     * Gets this project's DriverColl.
     *
     * @return the DriverColl
     */
    public static DriverColl getDriverColl() {
        return _drivers;
    }

    /**
     * Gets this project's DriverColl.
     *
     * @return the TourColl
     */
    public static TourColl getTourColl() {
        return _tours;
    }

    /**
     * Gets this project's RouteColl.
     *
     * @return the RouteColl
     */
    public static RouteColl getRouteColl() {
        return _routes;
    }

    /**
     * Gets this project's NodeColl.
     *
     * @return the NodeColl
     */
    public static NodeColl getNodeColl() {
        return _nodes;
    }

    /**
     * Gets this project's BusColl.
     *
     * @return the BusColl
     */
    public static BusColl getBusColl() {
        return _buses;
    }


    /**
     * Sets the current project.
     *
     * @param project The project to set
     */
    public static void set(Project project) {
        if (project.getID().equals(_project.getID())) {
            return;
        }
        try {
            db = Database.getDatabase(project);
        } catch (InterruptedException|DuplicateKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DriverColl newDriverColl = null;
        TourColl newTourColl = null;
        NodeColl newNodeColl = null;
        RouteColl newRouteColl = null;
        BusColl newBusColl = null;
        try {
            newNodeColl = db.getNodeColl();
            newRouteColl = db.getRouteColl();
            newBusColl = db.getBusColl();
            newTourColl = db.getTourColl();
            newDriverColl = db.getDriverColl();
        } catch (Exception e) {
            new ExceptionDialog(e);
        }

        notifyListenersBefore(project);
        _project = project;
        _drivers = newDriverColl;
        _tours = newTourColl;
        _routes = newRouteColl;
        _nodes = newNodeColl;
        _buses = newBusColl;
        Context.put("LAST_OPENED_PROJECT_ID", project.getID());
    }

    /**
     * Adds an event listener to this project.
     *
     * @param pl The ProjectListener to add
     */
    public static void addProjectListener(ProjectListener pl) {
        projectListeners.add(pl);
    }

    private static Collection getChangeListeners() {
        return projectListeners;
    }

    private static void notifyListenersBefore(Project project) {
        for (int i = 0; i < projectListeners.size(); i++) {
            ((ProjectListener) projectListeners.get(i)).projectChange(project);
        }
    }

    /**
     * Saves all the Database's collections.
     */
    public static void save() {
        Storage storage = CurrentStorage.get();

        try {
            db.write();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        storage.storeProjectManager();
    }

    /**
     * Empties this project of all data.
     */
    public static void free() {
        _project = null;
        _nodes = null;
        _routes = null;
        _buses = null;
        _tours = null;
        _drivers = null;
    }
}
