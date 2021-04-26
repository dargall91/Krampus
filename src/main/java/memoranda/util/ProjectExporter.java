/*
 * ProjectExporter.java Package: net.sf.memoranda.util Created on 19.01.2004
 * 16:44:05 @author Alex
 */
package main.java.memoranda.util;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.Collections;

import javax.swing.text.html.HTMLDocument;

import main.java.memoranda.*;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.ui.*;
import main.java.memoranda.ui.htmleditor.AltHTMLWriter;

/**
 *
 */
/* $Id: ProjectExporter.java,v 1.7 2005/07/05 08:17:28 alexeya Exp $ */
public class ProjectExporter {

    static boolean _chunked = false;
    static boolean _num = false;
    static boolean _xhtml = false;
    static boolean _copyImages = false;
    static File output = null;
    static String _charset = null;
    static boolean _titlesAsHeaders = false;
    static boolean _navigation = false;

    static String charsetString = "\n";

    public static void export(Project prj, File f, String charset,
                              boolean xhtml, boolean chunked, boolean navigation, boolean num,
                              boolean titlesAsHeaders, boolean copyImages) {

        _num = num;
        _chunked = chunked;
        _charset = charset;
        _xhtml = xhtml;
        _titlesAsHeaders = titlesAsHeaders;
        _copyImages = copyImages;
        _navigation = navigation;
        if (f.isDirectory()) {
            output = new File(f.getPath() + "/index.html");
        } else {
            output = f;
        }

        Writer fw;

        if (output.getName().indexOf(".htm") == -1) {
            String dir = output.getPath();
            String ext = ".html";

            String nfile = dir + ext;

            output = new File(nfile);
        }
        try {
            if (charset != null) {
                fw = new OutputStreamWriter(new FileOutputStream(output),
                    charset);
                charsetString = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset="
                    + charset + "\" />";
            } else {
                fw = new FileWriter(output);
            }
        } catch (Exception ex) {
            new ExceptionDialog(ex, "Failed to write to " + output, "");
            return;
        }
        write(fw, "<html>\n<head>\n" + charsetString + "<title>"
                + prj.getTitle()
                + "</title>\n</head>\n<body>\n<h1 class=\"projecttitle\">"
                + prj.getTitle() + "</h1><br></br>\n");
        write(fw, "\n<hr></hr><a href=\"http://memoranda.sf.net\">Memoranda</a> "
                + App.getVersionInfo() + "\n<br></br>\n" + new Date().toString()
                + "\n</body>\n</html>");
        try {
            fw.flush();
            fw.close();
        } catch (Exception ex) {
            new ExceptionDialog(ex, "Failed to write to " + output, "");
        }
    }

    private static void write(Writer w, String s) {
        try {
            w.write(s);
        } catch (Exception ex) {
            new ExceptionDialog(ex, "Failed to write to " + output, "");
        }
    }
}