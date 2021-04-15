/**
 * Storage.java
 * Created on 12.02.2003, 0:58:42 Alex
 * Package: net.sf.memoranda.util
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import main.java.memoranda.*;

import java.io.IOException;

/**
 *
 */
/*$Id: Storage.java,v 1.4 2004/01/30 12:17:42 alexeya Exp $*/
public interface Storage {

    TaskList openTaskList(Project prj);

    void storeTaskList(TaskList tl, Project prj);

    NoteList openNoteList(Project prj);

    void storeNoteList(NoteList nl, Project prj);

    void storeNote(Note note, javax.swing.text.Document doc);

    javax.swing.text.Document openNote(Note note);

    void removeNote(Note note);

    String getNoteURL(Note note);

    void openProjectManager();

    void storeProjectManager();

    void openEventsManager();

    void storeEventsManager();

    void openMimeTypesList();

    void storeMimeTypesList();

    void createProjectStorage(Project prj);

    void removeProjectStorage(Project prj);

    ResourcesList openResourcesList(Project prj);

    void storeResourcesList(ResourcesList rl, Project prj);

    public void storeNodeList(Project prj, NodeColl nodeColl) throws JsonProcessingException, IOException;

    public NodeColl openNodeList(Project prj) throws JsonProcessingException, IOException, DuplicateKeyException;

    public BusColl openBusList(Project prj) throws JsonProcessingException, IOException, DuplicateKeyException;

    public void storeBusList(Project prj, BusColl busColl) throws JsonProcessingException, IOException;

    public RouteColl openRouteList(Project prj, NodeColl nodeList) throws JsonProcessingException, IOException, DuplicateKeyException;

    public void storeRouteList(Project prj, RouteColl routeColl) throws JsonProcessingException, IOException;

    public DriverColl openDriverList(Project prj, TourColl tourColl) throws JsonProcessingException, IOException, DuplicateKeyException;

    public void storeDriverList(Project prj, DriverColl driverColl) throws JsonProcessingException, IOException;

    public TourColl openTourList(Project prj, RouteColl routeColl, BusColl busColl) throws JsonProcessingException, IOException, DuplicateKeyException;

    public void storeTourList(Project prj, TourColl tourColl) throws JsonProcessingException, IOException;

    void restoreContext();

    void storeContext();

}
