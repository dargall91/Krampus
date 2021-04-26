package main.java.memoranda.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.History;
import main.java.memoranda.Project;
import main.java.memoranda.ProjectListener;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.ui.htmleditor.HTMLEditor;
import main.java.memoranda.util.Configuration;
import main.java.memoranda.util.Context;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;
import main.java.memoranda.util.ProjectExporter;
import main.java.memoranda.util.ProjectPackager;
import main.java.memoranda.util.Util;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;


/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

/*$Id: AppFrame.java,v 1.33 2005/07/05 08:17:24 alexeya Exp $*/

public class AppFrame extends JFrame {

    JPanel contentPane;
    JMenuBar menuBar = new JMenuBar();
    JMenu menuFile = new JMenu();
    JMenuItem menuFileExit = new JMenuItem();

    JToolBar toolBar = new JToolBar();
    JButton button3 = new JButton();
    JLabel statusBar = new JLabel();
    BorderLayout borderLayout1 = new BorderLayout();
    JSplitPane splitPane = new JSplitPane();
    private final WorkPanel workPanel = new WorkPanel();
    private final DailyItemsPanel dailyItemsPanel = workPanel.getDailyItemsPanel();
    ProjectsPanel projectsPanel = new ProjectsPanel(dailyItemsPanel);
    boolean prPanelExpanded = false;

    JMenu menuFormat = new JMenu();
    JMenu menuInsert = new JMenu();

    static Vector exitListeners = new Vector();

    public Action prjPackAction = new AbstractAction("Pack current project") {
        public void actionPerformed(ActionEvent e) {
            doPrjPack();
        }
    };

    public Action prjUnpackAction = new AbstractAction("Unpack project") {
        public void actionPerformed(ActionEvent e) {
            doPrjUnPack();
        }
    };

    public Action minimizeAction = new AbstractAction("Close the window") {
        public void actionPerformed(ActionEvent e) {
            doMinimize();
        }
    };

    public Action preferencesAction = new AbstractAction("Preferences") {
        public void actionPerformed(ActionEvent e) {
            showPreferences();
        }
    };

    JMenuItem menuFileNewPrj = new JMenuItem();
    JMenuItem menuFilePackPrj = new JMenuItem(prjPackAction);
    JMenuItem menuFileUnpackPrj = new JMenuItem(prjUnpackAction);
    JMenuItem menuFileMin = new JMenuItem(minimizeAction);

    JMenuItem menuGoHBack = new JMenuItem(History.historyBackAction);
    JMenuItem menuGoFwd = new JMenuItem(History.historyForwardAction);

    JMenuItem menuGoDayBack = new JMenuItem(
            dailyItemsPanel.getCalendar().dayBackAction);
    JMenuItem menuGoDayFwd = new JMenuItem(
            dailyItemsPanel.getCalendar().dayForwardAction);
    JMenuItem menuGoToday = new JMenuItem(
            dailyItemsPanel.getCalendar().todayAction);

    JMenuItem menuEditPref = new JMenuItem(preferencesAction);

    JMenu menuInsertSpecial = new JMenu();

    JMenu menuHelp = new JMenu();

    JMenuItem menuHelpGuide = new JMenuItem();
    JMenuItem menuHelpWeb = new JMenuItem();
    JMenuItem menuHelpBug = new JMenuItem();
    JMenuItem menuHelpAbout = new JMenuItem();

    /**
     * Constructor for AppFrame
     */
    public AppFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            new ExceptionDialog(e);
        }
    }

    private void jbInit() {
        try {
            this.setIconImage(new ImageIcon(Objects.requireNonNull(AppFrame.class.getResource(
                    "/ui/logo/logo_icon.png")))
                    .getImage());
        } catch (NullPointerException e){
        }
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setTitle("Magic Tour Bus - " + CurrentProject.get().getTitle());
        statusBar.setText(" Version: " + App.getVersionInfo() + " (Build: "
                + App.getBuildInfo() + ")");
        
        System.out.println(statusBar.getText());

        menuFile.setText(Local.getString("File"));
        menuFileExit.setText(Local.getString("Exit"));
        menuFileExit.addActionListener(e -> doExit());
        menuHelp.setText(Local.getString("Help"));

        menuHelpGuide.setText(Local.getString("Online user's guide"));
        menuHelpGuide.setIcon(new ImageIcon(AppFrame.class.getResource(
                "/ui/icons/help.png")));


        menuHelpWeb.setText(Local.getString("Memoranda web site"));
        menuHelpWeb.setIcon(new ImageIcon(AppFrame.class.getResource(
                "/ui/icons/web.png")));


        button3.setToolTipText(Local.getString("Help"));
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(3);

        splitPane.setDividerLocation(28);

        projectsPanel.setMinimumSize(new Dimension(10, 28));
        projectsPanel.setPreferredSize(new Dimension(10, 28));
        splitPane.setDividerLocation(28);

        menuFileNewPrj.setAction(projectsPanel.newProjectAction);

        menuFileUnpackPrj.setText(Local.getString("Unpack project") + "...");
        menuFilePackPrj.setText(Local.getString("Pack project") + "...");
        menuFileMin.setText(Local.getString("Close the window"));
        menuFileMin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10,
                InputEvent.ALT_MASK));

        toolBar.add(button3);
        menuFile.add(menuFileNewPrj);
        menuFile.addSeparator();
        menuFile.add(menuFilePackPrj);
        menuFile.add(menuFileUnpackPrj);
        menuFile.addSeparator();
        menuFile.add(menuEditPref);
        menuFile.addSeparator();
        menuFile.add(menuFileMin);
        menuFile.addSeparator();
        menuFile.add(menuFileExit);

        menuHelp.add(menuHelpGuide);
        menuHelp.add(menuHelpWeb);
        menuHelp.add(menuHelpBug);
        menuHelp.addSeparator();
        menuHelp.add(menuHelpAbout);

        menuBar.add(menuFile);
        menuBar.add(menuHelp);
        this.setJMenuBar(menuBar);
        //contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(statusBar, BorderLayout.SOUTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
        splitPane.add(projectsPanel, JSplitPane.TOP);
        splitPane.add(workPanel, JSplitPane.BOTTOM);

        splitPane.setBorder(null);
        workPanel.setBorder(null);

        setEnabledEditorMenus(false);

        projectsPanel.AddExpandListener(e -> {
            if (prPanelExpanded) {
                prPanelExpanded = false;
                splitPane.setDividerLocation(28);
            } else {
                prPanelExpanded = true;
                splitPane.setDividerLocation(0.2);
            }
        });

        java.awt.event.ActionListener setMenusDisabled = e -> setEnabledEditorMenus(false);

        workPanel.getBusButton().addActionListener(setMenusDisabled);
        workPanel.getTourButton().addActionListener(setMenusDisabled);
        workPanel.getMapButton().addActionListener(setMenusDisabled);
        workPanel.getDriverButton().addActionListener(setMenusDisabled);

        Object fwo = Context.get("FRAME_WIDTH");
        Object fho = Context.get("FRAME_HEIGHT");
        if ((fwo != null) && (fho != null)) {
            int w = Integer.parseInt((String) fwo);
            int h = Integer.parseInt((String) fho);

            this.setSize(w, h);
        } else {
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        }

        Object xo = Context.get("FRAME_XPOS");
        Object yo = Context.get("FRAME_YPOS");
        if ((xo != null) && (yo != null)) {
            int x = Integer.parseInt((String) xo);
            int y = Integer.parseInt((String) yo);
            this.setLocation(x, y);
        }

        String pan = (String) Context.get("CURRENT_PANEL");
        if (pan != null) {
            workPanel.selectPanel(pan);
        }

        CurrentProject.addProjectListener(new ProjectListener() {

            public void projectChange(Project prj) {
            }

            public void projectWasChanged() {
                setTitle("Memoranda - " + CurrentProject.get().getTitle());
            }
        });

    }

    protected void menuHelpBug_actionPerformed(ActionEvent e) {
        Util.runBrowser(App.getBugsTrackerUrl());
    }

    protected void menuHelpWeb_actionPerformed(ActionEvent e) {
        Util.runBrowser(App.getWebsiteUrl());
    }

    protected void menuHelpGuide_actionPerformed(ActionEvent e) {
        Util.runBrowser(App.getGuideUrl());
    }

    //File | Exit action performed
    public void doExit() {
        if (Configuration.get("ASK_ON_EXIT").equals("yes")) {
            Dimension frmSize = this.getSize();
            Point loc = this.getLocation();

            ExitConfirmationDialog dlg = new ExitConfirmationDialog(this, Local.getString("Exit"));
            dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x, 
                    (frmSize.height - dlg.getSize().height) / 2 + loc.y);
            dlg.setVisible(true);
            if (dlg.CANCELLED) {
                return;
            }
        }

        Context.put("FRAME_WIDTH", getWidth());
        Context.put("FRAME_HEIGHT",getHeight());
        Context.put("FRAME_XPOS", getLocation().x);
        Context.put("FRAME_YPOS", getLocation().y);

        exitNotify();
        System.exit(0);
    }

    public void doMinimize() {
        exitNotify();
        App.closeWindow();
    }

    public void doMaximize() {
        exitNotify();
        App.openWindow();
    }

    //Help | About action performed
    public void menuHelpAbout_actionPerformed(ActionEvent e) {
        AppFrameAboutBox dlg = new AppFrameAboutBox(this);
        Dimension dlgSize = dlg.getSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.setVisible(true);
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (Configuration.get("ON_CLOSE").equals("exit")) {
                doExit();
            } else {
                doMinimize();
            }
        } else if ((e.getID() == WindowEvent.WINDOW_ICONIFIED)) {
            super.processWindowEvent(new WindowEvent(this,
                    WindowEvent.WINDOW_ICONIFIED));
            doMinimize();
        } else if ((e.getID() == WindowEvent.WINDOW_DEICONIFIED)) {
            super.processWindowEvent(new WindowEvent(this,
                    WindowEvent.WINDOW_ICONIFIED));
            doMaximize();
        }  else {
            super.processWindowEvent(e);
        }
    }

    public static void addExitListener(ActionListener al) {
        exitListeners.add(al);
    }

    public static Collection getExitListeners() {
        return exitListeners;
    }

    private static void exitNotify() {
        for (int i = 0; i < exitListeners.size(); i++) {
            ((ActionListener) exitListeners.get(i)).actionPerformed(null);
        }
    }

    public void setEnabledEditorMenus(boolean enabled) {
        this.menuFormat.setEnabled(enabled);
        this.menuInsert.setEnabled(enabled);
    }

    public void doPrjPack() {
        // Fix until Sun's JVM supports more locales...
        UIManager.put("FileChooser.saveInLabelText", Local
                .getString("Save in:"));
        UIManager.put("FileChooser.upFolderToolTipText", Local.getString(
                "Up One Level"));
        UIManager.put("FileChooser.newFolderToolTipText", Local.getString(
                "Create New Folder"));
        UIManager.put("FileChooser.listViewButtonToolTipText", Local
                .getString("List"));
        UIManager.put("FileChooser.detailsViewButtonToolTipText", Local
                .getString("Details"));
        UIManager.put("FileChooser.fileNameLabelText", Local.getString(
                "File Name:"));
        UIManager.put("FileChooser.filesOfTypeLabelText", Local.getString(
                "Files of Type:"));
        UIManager.put("FileChooser.saveButtonText", Local.getString("Save"));
        UIManager.put("FileChooser.saveButtonToolTipText", Local.getString(
                "Save selected file"));
        UIManager
                .put("FileChooser.cancelButtonText", Local.getString("Cancel"));
        UIManager.put("FileChooser.cancelButtonToolTipText", Local.getString(
                "Cancel"));

        JFileChooser chooser = new JFileChooser();
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle(Local.getString("Pack project"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.RTF));
        chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.ZIP));
        // fixes XP style look cosmetical problems JVM 1.4.2 and 1.4.2_01
        chooser.setPreferredSize(new Dimension(550, 375));

        //Added to fix the problem with packing a file then deleting that file.
        //(jcscoobyrs) 17-Nov-2003 at 14:57:06 PM
        //---------------------------------------------------------------------
        File lastSel = null;

        try {
            lastSel = (java.io.File) Context.get("LAST_SELECTED_PACK_FILE");
        } catch (ClassCastException cce) {
            lastSel = new File(System.getProperty("user.dir") + File.separator);
        }
        //---------------------------------------------------------------------

        if (lastSel != null) {
            chooser.setCurrentDirectory(lastSel);
        }
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Context.put("LAST_SELECTED_PACK_FILE", chooser.getSelectedFile());
        java.io.File f = chooser.getSelectedFile();
        ProjectPackager.pack(CurrentProject.get(), f);
    }

    public void doPrjUnPack() {
        // Fix until Sun's JVM supports more locales...
        UIManager.put("FileChooser.lookInLabelText", Local
                .getString("Look in:"));
        UIManager.put("FileChooser.upFolderToolTipText", Local.getString(
                "Up One Level"));
        UIManager.put("FileChooser.newFolderToolTipText", Local.getString(
                "Create New Folder"));
        UIManager.put("FileChooser.listViewButtonToolTipText", Local
                .getString("List"));
        UIManager.put("FileChooser.detailsViewButtonToolTipText", Local
                .getString("Details"));
        UIManager.put("FileChooser.fileNameLabelText", Local.getString(
                "File Name:"));
        UIManager.put("FileChooser.filesOfTypeLabelText", Local.getString(
                "Files of Type:"));
        UIManager.put("FileChooser.openButtonText", Local.getString("Open"));
        UIManager.put("FileChooser.openButtonToolTipText", Local.getString(
                "Open selected file"));
        UIManager
                .put("FileChooser.cancelButtonText", Local.getString("Cancel"));
        UIManager.put("FileChooser.cancelButtonToolTipText", Local.getString(
                "Cancel"));

        JFileChooser chooser = new JFileChooser();
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle(Local.getString("Unpack project"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.ZIP));
        //chooser.addChoosableFileFilter(new AllFilesFilter(AllFilesFilter.RTF));
        // fixes XP style look cosmetical problems JVM 1.4.2 and 1.4.2_01
        chooser.setPreferredSize(new Dimension(550, 375));

        //Added to fix the problem with packing a file then deleting that file.
        //(jcscoobyrs) 17-Nov-2003 at 14:57:06 PM
        //---------------------------------------------------------------------
        File lastSel = null;

        try {
            lastSel = (java.io.File) Context.get("LAST_SELECTED_PACK_FILE");
        } catch (ClassCastException cce) {
            lastSel = new File(System.getProperty("user.dir") + File.separator);
        }
        //---------------------------------------------------------------------

        if (lastSel != null) {
            chooser.setCurrentDirectory(lastSel);
        }
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Context.put("LAST_SELECTED_PACK_FILE", chooser.getSelectedFile());
        java.io.File f = chooser.getSelectedFile();
        ProjectPackager.unpack(f);
        projectsPanel.prjTablePanel.updateUI();
    }

    public void showPreferences() {
        PreferencesDialog dlg = new PreferencesDialog(this);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }
    
    /**
     * Gets the frame's WorkPanel.
     * 
     * @return The WorkPanel
     */
    public WorkPanel getWorkPanel() {
        return workPanel;
    }

    /**
     * Gets the frame's DailyItemsPanel.
     * 
     * @return The DailyItemsPanel
     */
    public DailyItemsPanel getDailyItemsPanel() {
        return dailyItemsPanel;
    }
}