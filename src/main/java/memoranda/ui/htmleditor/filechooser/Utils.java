package main.java.memoranda.ui.htmleditor.filechooser;

import java.io.File;

public class Utils {

    public static final String jpeg = "jpeg";
    public static final String jpg = "jpg";
    public static final String gif = "gif";
    public static final String png = "png";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
