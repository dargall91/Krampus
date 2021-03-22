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
import java.util.Collection;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import main.java.memoranda.*;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.ui.ExceptionDialog;
import main.java.memoranda.ui.htmleditor.AltHTMLWriter;
import nu.xom.Builder;
import nu.xom.Document;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;


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
        }
        catch (IOException ex) {
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
        }
        catch (Exception ex) {
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
        /*DEBUG*/System.out.println("[DEBUG] Save note: "+ filename);

        try {
            OutputStreamWriter fw =
                new OutputStreamWriter(new FileOutputStream(filename), "UTF-8");
            AltHTMLWriter writer = new AltHTMLWriter(fw, (HTMLDocument) doc);
            writer.write();
            fw.flush();
            fw.close();
            //editorKit.write(new FileOutputStream(filename), doc, 0, doc.getLength());
            //editorKit.write(fw, doc, 0, doc.getLength());
        }
        catch (Exception ex) {
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
        if (note == null)
            return doc;
        /*
                String filename = JN_DOCPATH + note.getProject().getID() + File.separator;
                CalendarDate d = note.getDate();
                filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        */
        String filename = getNotePath(note);
        try {
            /*DEBUG*/

//            Util.debug("Open note: " + filename);
//        	Util.debug("Note Title: " + note.getTitle());
        	doc.setBase(new URL(getNoteURL(note)));
        	editorKit.read(
                new InputStreamReader(new FileInputStream(filename), "UTF-8"),
                doc,
                0);
        }
        catch (Exception ex) {
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
        for (int i = 0; i < files.length; i++)
            files[i].delete();
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
        }
        else {
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
        saveDocument(tasklistDoc,JN_DOCPATH + prj.getID() + File.separator + ".tasklist");
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
        }
        else {
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
            }
            catch (Exception e) {
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
        }
        else {
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
        }
        catch (Exception ex) {
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
        }
        catch (Exception ex) {
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
     *
     * @param prj
     * @return
     */
    private String getNodeFileName(Project prj){
        return JN_DOCPATH + prj.getID() + File.separator + "nodes.json";
    }

    /**
     *
     * @param prj
     * @return
     * @throws JsonProcessingException
     * @throws IOException
     */
    public NodeColl openNodeList(Project prj) throws JsonProcessingException, IOException {

        String fn = getNodeFileName(prj);

        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println("[DEBUG] Open node file: " + fn);

            return new NodeColl();
        }
        else {
            /*DEBUG*/
            System.out.println("[DEBUG] New empty node collction created");

            return new NodeColl();
        }
    }

    /**
     *
     * @param nodeColl
     * @param prj
     * @throws JsonProcessingException
     */
    public void storeNodeList(NodeColl nodeColl, Project prj) throws JsonProcessingException {

    }

    /**
     *
     */
    private class NodeJsonClassWrapper {
        private Collection nodes;

        /**
         *
         * @param nc
         */
        public NodeJsonClassWrapper(NodeColl nc){
            this.nodes=nc.getNodeList().values();
        }

        /**
         *
         * @return
         */
        public Collection getNodes(){
            return nodes;
        }

    }

    /**
     *
     * @param prj
     * @throws JsonProcessingException
     * @throws IOException
     */
    public void storeNodeList(Project prj) throws JsonProcessingException, IOException {
        String fn = getNodeFileName(prj);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        ArrayNode node = mapper.createArrayNode();

        NodeColl nc=new NodeColl();
        Node n=new Node(1, "busstop1", 1.23, 3.24);
        Node n2=new Node(2, "bus stop number 2", 2.34, -134.2331);
        nc.addNode(n);
        nc.addNode(n2);

        String js= mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new NodeJsonClassWrapper(nc));
        System.out.println("jsonString collection="+js);

//        node.add()
//        ObjectNode nodedata = mapper.createObjectNode();

        /*DEBUG*/
        System.out.println("[DEBUG] Save note list: " + fn);

//        saveDocument(
//                nl.getXMLContent(),
//                JN_DOCPATH + prj.getID() + File.separator + ".notes");
    }

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
        }
        catch (IOException ex) {
            new ExceptionDialog(
                    ex,
                    "Failed to write a document to " + filePath,
                    "");
        }
    }


}
