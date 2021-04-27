package main.java.memoranda.ui;

import java.awt.*;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import main.java.memoranda.BuildVersion;
import main.java.memoranda.util.Configuration;

/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

/*$Id: App.java,v 1.28 2007/03/20 06:21:46 alexeya Exp $*/
public class App {
    private static final String VERSION_INFO = BuildVersion.getVersion();
    private static final String BUILD_INFO = BuildVersion.getBuild();
    
    private static AppFrame frame = new AppFrame();

    private static final String GUIDE_URL = "http://memoranda.sourceforge.net/guide.html";
    private static final String BUGS_TRACKER_URL = "http://sourceforge.net/tracker/?group_id=90997&atid=595566";
    private static final String WEBSITE_URL = "http://memoranda.sourceforge.net";
    private static int state;

    private JFrame splash = null;

    /*========================================================================*/

    public static AppFrame getFrame() {
        return frame;
    }

    public void show() {
        if (frame.isVisible()) {
            frame.toFront();
            frame.requestFocus();
        } else {
            init();
        }
    }

    public App(boolean fullmode) {
        super();

        final String LOOK_AND_FEEL="LOOK_AND_FEEL";
        if (fullmode) {
            fullmode = !Configuration.get("START_MINIMIZED").equals("yes");
        }
        /* DEBUG */
        if (!fullmode) {
            System.out.println("Minimized mode");
        }
        if (!Configuration.get("SHOW_SPLASH").equals("no")) {
            showSplash();
        }
        System.out.println(VERSION_INFO);
        System.out.println(Configuration.get(LOOK_AND_FEEL));
        try {
            if (Configuration.get(LOOK_AND_FEEL).equals("system")) {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } else if (Configuration.get(LOOK_AND_FEEL).equals("default")) {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
            } else if (
                    Configuration.get(LOOK_AND_FEEL).toString().length() > 0) {
                UIManager.setLookAndFeel(
                        Configuration.get(LOOK_AND_FEEL).toString());
            }

        } catch (Exception e) {
            new ExceptionDialog(e, "Error when initializing a pluggable look-and-feel. " 
                    + "Default LF will be used.", "Make sure that specified look-and-feel library "
                    + "classes are on the CLASSPATH.");
        }
        if (Configuration.get("FIRST_DAY_OF_WEEK").equals("")) {
            String fdow;
            if (Calendar.getInstance().getFirstDayOfWeek() == 2) {
                fdow = "mon";
            } else {
                fdow = "sun";
            }
            Configuration.put("FIRST_DAY_OF_WEEK", fdow);
            Configuration.saveConfig();
            /* DEBUG */
            System.out.println("[DEBUG] first day of week is set to " + fdow);
        }

        if (fullmode) {
            init();
        }
        if (!Configuration.get("SHOW_SPLASH").equals("no")) {
            splash.dispose();
        }
    }

    void init() {
        /*
         * if (packFrame) { frame.pack(); } else { frame.validate(); }
         *
         * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         *
         * Dimension frameSize = frame.getSize(); if (frameSize.height >
         * screenSize.height) { frameSize.height = screenSize.height; } if
         * (frameSize.width > screenSize.width) { frameSize.width =
         * screenSize.width; }
         *
         *
         * Make the window fullscreen - On Request of users This seems not to
         * work on sun's version 1.4.1_01 Works great with 1.4.2 !!! So update
         * your J2RE or J2SDK.
         */
        /* Used to maximize the screen if the JVM Version if 1.4 or higher */
        /* --------------------------------------------------------------- */
        double jvmver = Double.parseDouble(System.getProperty("java.version").substring(0, 3));

        frame.pack();
        if (jvmver >= 1.4) {
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(Frame.NORMAL);
        }
        /* --------------------------------------------------------------- */
        /* Added By Jeremy Whitlock (jcscoobyrs) 07-Nov-2003 at 15:54:24 */

        // Not needed ???
        frame.setVisible(true);
        frame.toFront();
        frame.requestFocus();

    }

    /**
     * Iconifies the application window when clicking the minimize button.
     */
    public static void closeWindow() {
        if (frame == null) {
            return;
        }

        state = frame.getExtendedState();
        frame.setExtendedState(JFrame.ICONIFIED);
    }

    /**
     * Defines what state the application window will take when the window is opened.
     */
    @SuppressWarnings("deprecation")
    public static void openWindow() {
        if (frame == null) {
            return;
        }
        
        if (state == Frame.NE_RESIZE_CURSOR) {
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(Frame.NORMAL);
        }
        
    }

    /**
     * Method showSplash.
     */
    private void showSplash() {
        splash = new JFrame();
        splash.setUndecorated(true);
        splash.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        ImageIcon spl =
                new ImageIcon(App.class.getResource("/ui/new_splash600x450.png"));
        JLabel l = new JLabel();
        l.setSize(600, 450);
        l.setIcon(spl);
        splash.getContentPane().add(l);
        splash.setSize(600, 450);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        splash.setLocation(
                (screenSize.width - 600) / 2,
                (screenSize.height - 450) / 2);
        splash.setUndecorated(true);
        splash.setVisible(true);
    }
    
    /**
     * Gets the Guide URL String.
     * 
     * @return The Guide URL
     */
    public static String getGuideUrl() {
        return GUIDE_URL;
    }
    
    /**
     * Gets the Bug Tracker URL.
     * 
     * @return The Bug Tracker URL
     */
    public static String getBugsTrackerUrl() {
        return BUGS_TRACKER_URL;
    }
    
    /**
     * Gets the Website URL.
     * 
     * @return The Website URL
     */
    public static String getWebsiteUrl() {
        return WEBSITE_URL;
    }
    
    /**
     * Gets the Version Number.
     * 
     * @return The Version Number
     */
    public static String getVersionInfo() {
        return VERSION_INFO;
    }
    
    /**
     * Gets the Build Number.
     * 
     * @return The Build Number
     */
    public static String getBuildInfo() {
        return BUILD_INFO;
    }
}