/**
 * Storage.java
 * Created on 12.02.2003, 0:21:40 Alex
 * Package: net.sf.memoranda.util
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.List;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import main.java.memoranda.Bus;
import main.java.memoranda.BusColl;
import main.java.memoranda.Database;
import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.DriverLoader;
import main.java.memoranda.EventsManager;
import main.java.memoranda.Node;
import main.java.memoranda.NodeColl;
import main.java.memoranda.Note;
import main.java.memoranda.NoteList;
import main.java.memoranda.NoteListImpl;
import main.java.memoranda.Project;
import main.java.memoranda.ProjectManager;
import main.java.memoranda.ResourcesList;
import main.java.memoranda.ResourcesListImpl;
import main.java.memoranda.Route;
import main.java.memoranda.RouteColl;
import main.java.memoranda.RouteLoader;
import main.java.memoranda.TaskList;
import main.java.memoranda.TaskListImpl;
import main.java.memoranda.TourColl;
import main.java.memoranda.TourLoader;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.ui.ExceptionDialog;
import main.java.memoranda.ui.htmleditor.AltHTMLWriter;
import nu.xom.Builder;
import nu.xom.Document;


/**
 *
 */
/*$Id: FileStorage.java,v 1.15 2006/10/09 23:31:58 alexeya Exp $*/
public class FileStorage implements Storage {

    public static String JN_DOCPATH = Util.getEnvDir();
    private HTMLEditorKit editorKit = new HTMLEditorKit();

    public FileStorage() {
        /*The 'MEMORANDA_HOME' key is an undocumented feature for 
          hacking the default location (Util.getEnvDir()) of the memoranda 
          storage dir. Note that memoranda.config file is always placed at fixed 
          location (Util.getEnvDir()) anyway */
        String mHome = (String) Configuration.get("MEMORANDA_HOME");
        if (mHome.length() > 0) {
            JN_DOCPATH = mHome;
            /*DEBUG*/
            System.out.println("[DEBUG]***Memoranda storage path has set to: " +
                    JN_DOCPATH);
        }
    }

    public static void saveDocument(Document doc, String filePath) {
        /**
         * @todo: Configurable parameters
         */
        try {
            /*The XOM bug: reserved characters are not escaped*/
            //Serializer serializer = new Serializer(new FileOutputStream(filePath), "UTF-8");
            //serializer.write(doc);

            OutputStreamWriter fw =
                    new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
            fw.write(doc.toXML());
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            new ExceptionDialog(
                    ex,
                    "Failed to write a document to " + filePath,
                    "");
        }
    }

    public static Document openDocument(InputStream in) throws Exception {
        Builder builder = new Builder();
        return builder.build(new InputStreamReader(in, "UTF-8"));
    }

    public static Document openDocument(String filePath) {
        try {
            return openDocument(new FileInputStream(filePath));
        } catch (Exception ex) {
            new ExceptionDialog(
                    ex,
                    "Failed to read a document from " + filePath,
                    "");
        }
        return null;
    }

    public static boolean documentExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * @see main.java.memoranda.util.Storage#storeNote(main.java.memoranda.Note)
     */
    public void storeNote(Note note, javax.swing.text.Document doc) {
        String filename =
                JN_DOCPATH + note.getProject().getID() + File.separator;
        doc.putProperty(
                javax.swing.text.Document.TitleProperty,
                note.getTitle());
        CalendarDate d = note.getDate();

        filename += note.getId();//d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        /*DEBUG*/
        System.out.println("[DEBUG] Save note: " + filename);

        try {
            OutputStreamWriter fw =
                    new OutputStreamWriter(new FileOutputStream(filename), "UTF-8");
            AltHTMLWriter writer = new AltHTMLWriter(fw, (HTMLDocument) doc);
            writer.write();
            fw.flush();
            fw.close();
            //editorKit.write(new FileOutputStream(filename), doc, 0, doc.getLength());
            //editorKit.write(fw, doc, 0, doc.getLength());
        } catch (Exception ex) {
            new ExceptionDialog(
                    ex,
                    "Failed to write a document to " + filename,
                    "");
        }
        /*String filename = JN_DOCPATH + note.getProject().getID() + "/";
        doc.putProperty(javax.swing.text.Document.TitleProperty, note.getTitle());
        CalendarDate d = note.getDate();
        filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        try {
            long t1 = new java.util.Date().getTime();
            FileOutputStream ostream = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(ostream);
        
            oos.writeObject((HTMLDocument)doc);
        
            oos.flush();
            oos.close();
            ostream.close();
            long t2 = new java.util.Date().getTime();
            System.out.println(filename+" save:"+ (t2-t1) );
        }
            catch (Exception ex) {
                ex.printStackTrace();
            }*/

    }

    /**
     * @see main.java.memoranda.util.Storage#openNote(main.java.memoranda.Note)
     */
    public javax.swing.text.Document openNote(Note note) {

        HTMLDocument doc = (HTMLDocument) editorKit.createDefaultDocument();
        if (note == null) {
            return doc;
        }
        /*
                String filename = JN_DOCPATH + note.getProject().getID() + File.separator;
                CalendarDate d = note.getDate();
                filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        */
        String filename = getNotePath(note);
        try {
            /*DEBUG*/

//            Util.debug("Open note: " + filename);
//            Util.debug("Note Title: " + note.getTitle());
            doc.setBase(new URL(getNoteURL(note)));
            editorKit.read(
                    new InputStreamReader(new FileInputStream(filename), "UTF-8"),
                    doc,
                    0);
        } catch (Exception ex) {
            //ex.printStackTrace();
            // Do nothing - we've got a new empty document!
        }

        return doc;
        /*HTMLDocument doc = (HTMLDocument)editorKit.createDefaultDocument();
        if (note == null) return doc;
        String filename = JN_DOCPATH + note.getProject().getID() + "/";
        CalendarDate d = note.getDate();
        filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        try {
            long t1 = new java.util.Date().getTime();
            FileInputStream istream = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(istream);
            doc = (HTMLDocument)ois.readObject();
            ois.close();
            istream.close();
            long t2 = new java.util.Date().getTime();
            System.out.println(filename+" open:"+ (t2-t1) );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return doc;*/
    }

    public String getNoteURL(Note note) {
        return "file:" + JN_DOCPATH + note.getProject().getID() + "/" + note.getId();
    }

    public String getNotePath(Note note) {
        String filename = JN_DOCPATH + note.getProject().getID() + File.separator;
//        CalendarDate d = note.getDate();
        filename += note.getId();//d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        return filename;
    }


    public void removeNote(Note note) {
        File f = new File(getNotePath(note));
        /*DEBUG*/
        System.out.println("[DEBUG] Remove note:" + getNotePath(note));
        f.delete();
    }

    /**
     * @see main.java.memoranda.util.Storage#openProjectManager()
     */
    public void openProjectManager() {
        if (!new File(JN_DOCPATH + ".projects").exists()) {
            ProjectManager._doc = null;
            return;
        }
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Open project manager: " + JN_DOCPATH + ".projects");
        ProjectManager._doc = openDocument(JN_DOCPATH + ".projects");
    }

    /**
     * @see main.java.memoranda.util.Storage#storeProjectManager(nu.xom.Document)
     */
    public void storeProjectManager() {
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Save project manager: " + JN_DOCPATH + ".projects");
        saveDocument(ProjectManager._doc, JN_DOCPATH + ".projects");
    }

    /**
     * @see main.java.memoranda.util.Storage#removeProject(main.java.memoranda.Project)
     */
    public void removeProjectStorage(Project prj) {
        String id = prj.getID();

        /*DEBUG*/
        System.out.println(
                "[DEBUG] Remove/Delete project dir: " + JN_DOCPATH + prj.getID());

        File f = new File(JN_DOCPATH + id);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        f.delete();
    }

    public TaskList openTaskList(Project prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".tasklist";

        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println(
                    "[DEBUG] Open task list: "
                            + JN_DOCPATH
                            + prj.getID()
                            + File.separator
                            + ".tasklist");

            Document tasklistDoc = openDocument(fn);
            /*DocType tasklistDoctype = tasklistDoc.getDocType();
            String publicId = null;
            if (tasklistDoctype != null) {
                publicId = tasklistDoctype.getPublicID();
            }
            boolean upgradeOccurred = TaskListVersioning.upgradeTaskList(publicId);
            if (upgradeOccurred) {
                // reload from new file
                tasklistDoc = openDocument(fn);
            }*/
            return new TaskListImpl(tasklistDoc, prj);
        } else {
            /*DEBUG*/
            System.out.println("[DEBUG] New task list created");
            return new TaskListImpl(prj);
        }
    }

    public void storeTaskList(TaskList tasklist, Project prj) {
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Save task list: "
                        + JN_DOCPATH
                        + prj.getID()
                        + File.separator
                        + ".tasklist");
        Document tasklistDoc = tasklist.getXMLContent();
        //tasklistDoc.setDocType(TaskListVersioning.getCurrentDocType());
        saveDocument(tasklistDoc, JN_DOCPATH + prj.getID() + File.separator + ".tasklist");
    }

    /**
     * @see main.java.memoranda.util.Storage#createProjectStorage(main.java.memoranda.Project)
     */
    public void createProjectStorage(Project prj) {
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Create project dir: " + JN_DOCPATH + prj.getID());
        File dir = new File(JN_DOCPATH + prj.getID());
        dir.mkdirs();

        try {
            Database.getDatabase(prj).write();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            new ExceptionDialog(e);
        }
    }

    /**
     * @see main.java.memoranda.util.Storage#openNoteList(main.java.memoranda.Project)
     */
    public NoteList openNoteList(Project prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".notes";
        //System.out.println(fn);
        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println(
                    "[DEBUG] Open note list: "
                            + JN_DOCPATH
                            + prj.getID()
                            + File.separator
                            + ".notes");
            return new NoteListImpl(openDocument(fn), prj);
        } else {
            /*DEBUG*/
            System.out.println("[DEBUG] New note list created");
            return new NoteListImpl(prj);
        }
    }

    /**
     * @see main.java.memoranda.util.Storage#storeNoteList(main.java.memoranda.NoteList, main.java.memoranda.Project)
     */
    public void storeNoteList(NoteList nl, Project prj) {
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Save note list: "
                        + JN_DOCPATH
                        + prj.getID()
                        + File.separator
                        + ".notes");
        saveDocument(
                nl.getXMLContent(),
                JN_DOCPATH + prj.getID() + File.separator + ".notes");
    }

    /**
     * @see main.java.memoranda.util.Storage#openEventsList()
     */
    public void openEventsManager() {
        if (!new File(JN_DOCPATH + ".events").exists()) {
            EventsManager._doc = null;
            return;
        }
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Open events manager: " + JN_DOCPATH + ".events");
        EventsManager._doc = openDocument(JN_DOCPATH + ".events");
    }

    /**
     * @see main.java.memoranda.util.Storage#storeEventsList()
     */
    public void storeEventsManager() {
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Save events manager: " + JN_DOCPATH + ".events");
        saveDocument(EventsManager._doc, JN_DOCPATH + ".events");
    }

    /**
     * @see main.java.memoranda.util.Storage#openMimeTypesList()
     */
    public void openMimeTypesList() {
        if (!new File(JN_DOCPATH + ".mimetypes").exists()) {
            try {
                MimeTypesList._doc =
                        openDocument(
                                FileStorage.class.getResourceAsStream(
                                        "/util/default.mimetypes"));
            } catch (Exception e) {
                new ExceptionDialog(
                        e,
                        "Failed to read default mimetypes config from resources",
                        "");
            }
            return;
        }
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Open mimetypes list: " + JN_DOCPATH + ".mimetypes");
        MimeTypesList._doc = openDocument(JN_DOCPATH + ".mimetypes");
    }

    /**
     * @see main.java.memoranda.util.Storage#storeMimeTypesList()
     */
    public void storeMimeTypesList() {
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Save mimetypes list: " + JN_DOCPATH + ".mimetypes");
        saveDocument(MimeTypesList._doc, JN_DOCPATH + ".mimetypes");
    }

    /**
     * @see main.java.memoranda.util.Storage#openResourcesList(main.java.memoranda.Project)
     */
    public ResourcesList openResourcesList(Project prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".resources";
        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println("[DEBUG] Open resources list: " + fn);
            return new ResourcesListImpl(openDocument(fn), prj);
        } else {
            /*DEBUG*/
            System.out.println("[DEBUG] New note list created");
            return new ResourcesListImpl(prj);
        }
    }

    /**
     * @see main.java.memoranda.util.Storage#storeResourcesList(main.java.memoranda.ResourcesList, main.java.memoranda.Project)
     */
    public void storeResourcesList(ResourcesList rl, Project prj) {
        /*DEBUG*/
        System.out.println(
                "[DEBUG] Save resources list: "
                        + JN_DOCPATH
                        + prj.getID()
                        + File.separator
                        + ".resources");
        saveDocument(
                rl.getXMLContent(),
                JN_DOCPATH + prj.getID() + File.separator + ".resources");
    }

    /**
     * @see main.java.memoranda.util.Storage#restoreContext()
     */
    public void restoreContext() {
        try {
            /*DEBUG*/
            System.out.println(
                    "[DEBUG] Open context: " + JN_DOCPATH + ".context");
            Context.context.load(new FileInputStream(JN_DOCPATH + ".context"));
        } catch (Exception ex) {
            /*DEBUG*/
            System.out.println("Context created.");
        }
    }

    /**
     * @see main.java.memoranda.util.Storage#storeContext()
     */
    public void storeContext() {
        try {
            /*DEBUG*/
            System.out.println(
                    "[DEBUG] Save context: " + JN_DOCPATH + ".context");
            Context.context.save(new FileOutputStream(JN_DOCPATH + ".context"));
        } catch (Exception ex) {
            new ExceptionDialog(
                    ex,
                    "Failed to store context to " + JN_DOCPATH + ".context",
                    "");
        }
    }


    /*
    // Required output format from @amehlhase

    {"nodes":[
    {"id": "1", "lat":"33.304682", "lon": "-111.680727"},
    {"id": "2",  "lat": "33.303659", "lon": "-111.680792"},
    {"id": "3", "lat": "33.302548", "lon": "-111.675674"},
    {"id": "4", "lat": "33.303597", "lon": "-111.673625"},
    {"id": "5", "lat": "33.304628", "lon": "-111.675663"},
    {"id": "6", "lat": "33.303175", "lon": "-111.678185"},
    {"id": "7", "lat": "33.305103", "lon": "-111.677734"},
    {"id": "8", "lat": "33.306529", "lon": "-111.680695"}
   ]}
     */


    /**
     * gets node filename.
     *
     * @param prj project to use (for obtaining file path)
     * @return filename
     */
    private String getNodeFileName(Project prj) {
        return getFileName(prj, "nodes.json");
    }

    /**
     * gets route filename.
     *
     * @param prj project to use (for obtaining file path)
     * @return filename
     */
    private String getRouteFileName(Project prj) {
        return getFileName(prj, "route.json");
    }

    /**
     * gets driver filename.
     *
     * @param prj project to use (for obtaining file path)
     * @return filename
     */
    private String getDriverFileName(Project prj) {
        return getFileName(prj, "driver.json");
    }

    /**
     * gets bus filename.
     *
     * @param prj project to use (for obtaining file path)
     * @return filename
     */
    private String getBusFileName(Project prj) {
        return getFileName(prj, "bus.json");
    }


    /**
     * gets tour filename.
     *
     * @param prj project to use (for obtaining file path)
     * @return filename
     */
    private String getTourFileName(Project prj) {
        return getFileName(prj, "tour.json");
    }

    /**
     * @param prj      project to use (for obtaining file path)
     * @param filename composes filename for given data collection
     * @return filesystem pathname for file
     */
    private String getFileName(Project prj, String filename) {
        return JN_DOCPATH + prj.getID() + File.separator + filename;
    }


    /**
     * Save a node list to JSON file in specified project
     *
     * @param prj      project to use (for obtaining file path)
     * @param nodeColl node collection to write to disk
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     */
    public void storeNodeList(Project prj, NodeColl nodeColl) throws JsonProcessingException,
            IOException {
        String fn = getNodeFileName(prj);

        // create new object mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);

        // annotation is used so Jackson knows which method to use for output
//        String js= mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeColl);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fn), nodeColl);

        /*DEBUG*/
//        System.out.println("[DEBUG] Save note list: " + fn);
//        System.out.println("jsonString collection="+js);
    }

    /**
     * Load a node list from JSON file in specified project
     *
     * @param prj project to use (for obtaining file path)
     * @return node list
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     */
    public NodeColl openNodeList(Project prj) throws JsonProcessingException, IOException,
            DuplicateKeyException {
        String fn = getNodeFileName(prj);

        ObjectMapper mapper = new ObjectMapper();

        // create new mapper object
        JsonNode jsonNode = mapper.readTree(new File(fn));

        String all = jsonNode.toString();
        System.out.println("all=" + all);
//            System.out.println("jsonNode is object:"+jsonNode.isObject());
//            JsonNode next=jsonNode.get("nodes");
//            System.out.println("next="+next+" is object:"+next.isObject());
//            System.out.println("next as string:"+next.toString());

        // find value of "nodes" object (which is an array) and create list of Node objects
        List<Node> nodeList;
        try {
            nodeList = mapper.readValue(jsonNode.get("nodes").toString(), new TypeReference<>() {
            });
        } catch (NullPointerException e) {
            nodeList = null;
        }

        // create new nodeColl based on read data/objects
        NodeColl nodeColl = new NodeColl(nodeList);

//            for (Node n:nodeColl){
//                System.out.println("Found node in list="+n);
//            }
        return nodeColl;
    }

    /**
     * @param prj      project to use (for obtaining file path)
     * @param nodeList node collection to use for building route
     * @return route collection
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     */
    public RouteColl openRouteList(Project prj, NodeColl nodeList) throws JsonProcessingException,
            IOException, DuplicateKeyException {
        String fn = getRouteFileName(prj);

        ObjectMapper mapper = new ObjectMapper();

        // create new mapper object
        JsonNode jsonNode = mapper.readTree(new File(fn));

        // find value of "nodes" object (which is an array) and create list of Route objects
        List<RouteLoader> routeList;
        try {
            routeList = mapper.readValue(jsonNode.get("routes").toString(),
                    new TypeReference<>() {
                    });
        } catch (NullPointerException e) {
            routeList = null;
        }

        // create new nodeColl based on read data/objects
        RouteColl routeColl = new RouteColl(nodeList, routeList);

        System.out.println("[DEBUG] RouteColl has " + routeColl.size() + " in openRouteList");
        return routeColl;
    }


    /**
     * @param prj       project to use (for obtaining file path)
     * @param routeColl route collection to write to disk
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     */
    public void storeRouteList(Project prj, RouteColl routeColl) throws JsonProcessingException,
            IOException {
        String fn = getRouteFileName(prj);

        // create new object mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);

        // annotation is used so Jackson knows which method to use for output
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fn), routeColl);

    }


    /**
     * @param prj       project to use (for obtaining file path)
     * @param routeColl route collection to read from disk
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     * @throws DuplicateKeyException
     */
    public TourColl openTourList(Project prj, RouteColl routeColl, BusColl busColl) throws
            JsonProcessingException, IOException, DuplicateKeyException {
        String fn = getTourFileName(prj);

        ObjectMapper mapper = new ObjectMapper();

        // create new mapper object
        JsonNode jsonNode = mapper.readTree(new File(fn));

        // find value of "nodes" object (which is an array) and create list of Route objects
        List<TourLoader> tourList;
        try {
            tourList = mapper.readValue(jsonNode.get("tours").toString(), new TypeReference<>() {
            });
        } catch (NullPointerException e) {
            tourList = null;
        }

        // create new nodeColl based on read data/objects
        TourColl tourColl = new TourColl(routeColl, busColl, tourList);

        System.out.println("[DEBUG] RouteColl has " + tourColl.size() + " in openRouteList");
        return tourColl;
    }

    /**
     * @param prj      project to use (for obtaining file path)
     * @param tourColl tour collection to write to disk
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     */
    public void storeTourList(Project prj, TourColl tourColl) throws JsonProcessingException,
            IOException {
        String fn = getTourFileName(prj);

        // create new object mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);

        // annotation is used so Jackson knows which method to use for output
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fn), tourColl);

    }

    /**
     * @param prj      project to use (for obtaining file path)
     * @param tourColl tour collection to read from disk
     * @return bus collection
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     */
    public DriverColl openDriverList(Project prj, TourColl tourColl) throws JsonProcessingException,
            IOException, DuplicateKeyException {
        String fn = getDriverFileName(prj);

        ObjectMapper mapper = new ObjectMapper();

        // create new mapper object
        JsonNode jsonNode = mapper.readTree(new File(fn));

        // find value of "nodes" object (which is an array) and create list of Driver objects
        List<DriverLoader> driverList = null;
        try {
            driverList = mapper.readValue(jsonNode.get("drivers").toString(), new TypeReference<>() {
            });
        } catch (NullPointerException e) {
            driverList = null;
        }

        // create new driverColl based on read data/objects
        DriverColl driverColl = new DriverColl(tourColl, driverList);

        System.out.println("[DEBUG] DriverColl has " + driverColl.size() + " in openDriverList");
        return driverColl;
    }


    /**
     * @param driverColl driver collection to write to disk
     * @param prj        project to use (for obtaining file path)
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     */
    public void storeDriverList(Project prj, DriverColl driverColl) throws JsonProcessingException,
            IOException {
        String fn = getDriverFileName(prj);

        // create new object mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);

        // annotation is used so Jackson knows which method to use for output
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fn), driverColl);

    }


    /**
     * @param prj project to use (for obtaining file path)
     * @return bus collection
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     * @throws DuplicateKeyException
     */
    public BusColl openBusList(Project prj) throws JsonProcessingException, IOException,
            DuplicateKeyException {
        String fn = getBusFileName(prj);

        ObjectMapper mapper = new ObjectMapper();

        // create new mapper object
        JsonNode jsonNode = mapper.readTree(new File(fn));

        List<Bus> busList = null;
        // find value of "nodes" object (which is an array) and create list of Bus objects
        try {
            busList = mapper.readValue(jsonNode.get("buses").toString(), new TypeReference<>() {
            });
        } catch (NullPointerException e) {
            busList = null;
        }

        // create new busColl based on read data/objects
        BusColl busColl = new BusColl(busList);

        System.out.println("[DEBUG] BusColl has " + busColl.size() + " in openBusList");
        return busColl;


    }

    /**
     * @param prj     project to use (for obtaining file path)
     * @param busColl bus collection to write to disk
     * @throws JsonProcessingException in case of json error
     * @throws IOException             in case of disk i/o error
     */
    public void storeBusList(Project prj, BusColl busColl) throws JsonProcessingException, IOException {
        String fn = getBusFileName(prj);

        // create new object mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);

        // annotation is used so Jackson knows which method to use for output
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fn), busColl);
    }


    /**
     * @param doc
     * @param filePath
     */
    public static void saveList(Document doc, String filePath) {
        /**
         * @todo: Configurable parameters
         */
        try {
            /*The XOM bug: reserved characters are not escaped*/
            //Serializer serializer = new Serializer(new FileOutputStream(filePath), "UTF-8");
            //serializer.write(doc);

            OutputStreamWriter fw =
                    new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
            fw.write(doc.toXML());
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            new ExceptionDialog(
                    ex,
                    "Failed to write a document to " + filePath,
                    "");
        }
    }


}
