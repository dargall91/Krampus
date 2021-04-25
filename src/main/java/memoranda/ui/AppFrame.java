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
import main.java.memoranda.ResourcesList;
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
    ImageIcon image1;
    ImageIcon image2;
    ImageIcon image3;
    JLabel statusBar = new JLabel();
    BorderLayout borderLayout1 = new BorderLayout();
    JSplitPane splitPane = new JSplitPane();
    private WorkPanel workPanel = new WorkPanel();
    private DailyItemsPanel dailyItemsPanel = workPanel.getDailyItemsPanel();
    ProjectsPanel projectsPanel = new ProjectsPanel(dailyItemsPanel);
    boolean prPanelExpanded = false;

    JMenu menuEdit = new JMenu();
    JMenu menuFormat = new JMenu();
    JMenu menuInsert = new JMenu();


    HTMLEditor editor = dailyItemsPanel.editorPanel.editor;

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
    JMenuItem menuFileNewNote = new JMenuItem(dailyItemsPanel.editorPanel.newAction);
    JMenuItem menuFilePackPrj = new JMenuItem(prjPackAction);
    JMenuItem menuFileUnpackPrj = new JMenuItem(prjUnpackAction);
    JMenuItem menuFileMin = new JMenuItem(minimizeAction);

    JMenuItem menuItem1 = new JMenuItem();
    JMenuItem menuEditUndo = new JMenuItem(editor.undoAction);
    JMenuItem menuEditRedo = new JMenuItem(editor.redoAction);
    JMenuItem menuEditCut = new JMenuItem(editor.cutAction);
    JMenuItem menuEditCopy = new JMenuItem(editor.copyAction);
    JMenuItem menuEditPaste = new JMenuItem(editor.pasteAction);
    JMenuItem menuEditPasteSpec = new JMenuItem(editor.stylePasteAction);
    JMenuItem menuEditSelectAll = new JMenuItem(editor.selectAllAction);
    JMenuItem menuEditFind = new JMenuItem(editor.findAction);

    JMenu menuGo = new JMenu();
    JMenuItem menuInsertImage = new JMenuItem(editor.imageAction);
    JMenuItem menuInsertTable = new JMenuItem(editor.tableAction);
    JMenuItem menuInsertLink = new JMenuItem(editor.linkAction);
    JMenu menuInsertList = new JMenu();
    JMenuItem menuInsertListUL = new JMenuItem(editor.ulAction);
    JMenuItem menuInsertListOL = new JMenuItem(editor.olAction);
    JMenuItem menuInsertBR = new JMenuItem(editor.breakAction);
    JMenuItem menuInsertHR = new JMenuItem(editor.insertHRAction);
    JMenuItem menuInsertChar = new JMenuItem(editor.insCharAction);
    JMenuItem menuInsertDate = new JMenuItem(
            dailyItemsPanel.editorPanel.insertDateAction);
    JMenuItem menuInsertTime = new JMenuItem(
            dailyItemsPanel.editorPanel.insertTimeAction);
    JMenuItem menuInsertFile = new JMenuItem(
            dailyItemsPanel.editorPanel.importAction);

    JMenu menuFormatPStyle = new JMenu();
    JMenuItem menuFormatP = new JMenuItem(editor.new BlockAction(editor.T_P,
            ""));
    JMenuItem menuFormatH1 = new JMenuItem(editor.new BlockAction(editor.T_H1,
            ""));
    JMenuItem menuFormatH2 = new JMenuItem(editor.new BlockAction(editor.T_H2,
            ""));
    JMenuItem menuFormatH3 = new JMenuItem(editor.new BlockAction(editor.T_H3,
            ""));
    JMenuItem menuFormatH4 = new JMenuItem(editor.new BlockAction(editor.T_H4,
            ""));
    JMenuItem menuFormatH5 = new JMenuItem(editor.new BlockAction(editor.T_H5,
            ""));
    JMenuItem menuFormatH6 = new JMenuItem(editor.new BlockAction(editor.T_H6,
            ""));
    JMenuItem menuFormatPre = new JMenuItem(editor.new BlockAction(
            editor.T_PRE, ""));
    JMenuItem menuFormatBlcq = new JMenuItem(editor.new BlockAction(
            editor.T_BLOCKQ, ""));
    JMenu jjMenuFormatChStyle = new JMenu();
    JMenuItem menuFormatChNorm = new JMenuItem(editor.new InlineAction(
            editor.I_NORMAL, ""));
    JMenuItem menuFormatChEM = new JMenuItem(editor.new InlineAction(
            editor.I_EM, ""));
    JMenuItem menuFormatChStrong = new JMenuItem(editor.new InlineAction(
            editor.I_STRONG, ""));
    JMenuItem menuFormatChCode = new JMenuItem(editor.new InlineAction(
            editor.I_CODE, ""));
    JMenuItem menuFormatChCite = new JMenuItem(editor.new InlineAction(
            editor.I_CITE, ""));
    JMenuItem menuFormatChSup = new JMenuItem(editor.new InlineAction(
            editor.I_SUPERSCRIPT, ""));
    JMenuItem menuFormatChSub = new JMenuItem(editor.new InlineAction(
            editor.I_SUBSCRIPT, ""));
    JMenuItem menuFormatChCustom = new JMenuItem(editor.new InlineAction(
            editor.I_CUSTOM, ""));
    JMenuItem menuFormatChB = new JMenuItem(editor.boldAction);
    JMenuItem menuFormatChI = new JMenuItem(editor.italicAction);
    JMenuItem menuFormatChU = new JMenuItem(editor.underAction);
    JMenu menuFormatAlign = new JMenu();
    JMenuItem menuFormatAlignL = new JMenuItem(editor.lAlignAction);
    JMenuItem menuFormatAlignC = new JMenuItem(editor.cAlignAction);
    JMenuItem menuFormatAlignR = new JMenuItem(editor.rAlignAction);
    JMenu menuFormatTable = new JMenu();
    JMenuItem menuFormatTableInsR = new JMenuItem(editor.insertTableRowAction);
    JMenuItem menuFormatTableInsC = new JMenuItem(editor.insertTableCellAction);
    JMenuItem menuFormatProperties = new JMenuItem(editor.propsAction);
    JMenuItem menuGoHBack = new JMenuItem(History.historyBackAction);
    JMenuItem menuGoFwd = new JMenuItem(History.historyForwardAction);

    JMenuItem menuGoDayBack = new JMenuItem(
            dailyItemsPanel.calendar.dayBackAction);
    JMenuItem menuGoDayFwd = new JMenuItem(
            dailyItemsPanel.calendar.dayForwardAction);
    JMenuItem menuGoToday = new JMenuItem(
            dailyItemsPanel.calendar.todayAction);

    JMenuItem menuEditPref = new JMenuItem(preferencesAction);

    JMenu menuInsertSpecial = new JMenu();

    JMenu menuHelp = new JMenu();

    JMenuItem menuHelpGuide = new JMenuItem();
    JMenuItem menuHelpWeb = new JMenuItem();
    JMenuItem menuHelpBug = new JMenuItem();
    JMenuItem menuHelpAbout = new JMenuItem();

    //Construct the frame
    public AppFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            new ExceptionDialog(e);
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
        this.setIconImage(new ImageIcon(AppFrame.class.getResource(
                "/ui/logo/logo_icon.png"))
                .getImage());
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        //this.setSize(new Dimension(800, 500));
        this.setTitle("Magic Tour Bus - " + CurrentProject.get().getTitle());
        statusBar.setText(" Version: " + App.getVersionInfo() + " (Build: "
                + App.getBuildInfo() + ")");
        
        System.out.println(statusBar.getText());

        menuFile.setText(Local.getString("File"));
        menuFileExit.setText(Local.getString("Exit"));
        menuFileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doExit();
            }
        });
        menuHelp.setText(Local.getString("Help"));

        menuHelpGuide.setText(Local.getString("Online user's guide"));
        menuHelpGuide.setIcon(new ImageIcon(AppFrame.class.getResource(
                "/ui/icons/help.png")));
        menuHelpGuide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuHelpGuide_actionPerformed(e);
            }
        });

        menuHelpWeb.setText(Local.getString("Memoranda web site"));
        menuHelpWeb.setIcon(new ImageIcon(AppFrame.class.getResource(
                "/ui/icons/web.png")));
        menuHelpWeb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuHelpWeb_actionPerformed(e);
            }
        });

        menuHelpBug.setText(Local.getString("Report a bug"));
        menuHelpBug.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuHelpBug_actionPerformed(e);
            }
        });

        menuHelpAbout.setText(Local.getString("About Memoranda"));
        menuHelpAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuHelpAbout_actionPerformed(e);
            }
        });

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

        menuEdit.setText(Local.getString("Edit"));

        menuEditUndo.setText(Local.getString("Undo"));
        menuEditUndo.setToolTipText(Local.getString("Undo"));
        menuEditRedo.setText(Local.getString("Redo"));
        menuEditRedo.setToolTipText(Local.getString("Redo"));
        menuEditCut.setText(Local.getString("Cut"));
        menuEditCut.setToolTipText(Local.getString("Cut"));
        menuEditCopy.setText((String) Local.getString("Copy"));
        menuEditCopy.setToolTipText(Local.getString("Copy"));
        menuEditPaste.setText(Local.getString("Paste"));
        menuEditPaste.setToolTipText(Local.getString("Paste"));
        menuEditPasteSpec.setText(Local.getString("Paste special"));
        menuEditPasteSpec.setToolTipText(Local.getString("Paste special"));
        menuEditSelectAll.setText(Local.getString("Select all"));

        menuEditFind.setText(Local.getString("Find & replace") + "...");

        menuEditPref.setText(Local.getString("Preferences") + "...");

        menuInsert.setText(Local.getString("Insert"));

        menuInsertImage.setText(Local.getString("Image") + "...");
        menuInsertImage.setToolTipText(Local.getString("Insert Image"));
        menuInsertTable.setText(Local.getString("Table") + "...");
        menuInsertTable.setToolTipText(Local.getString("Insert Table"));
        menuInsertLink.setText(Local.getString("Hyperlink") + "...");
        menuInsertLink.setToolTipText(Local.getString("Insert Hyperlink"));
        menuInsertList.setText(Local.getString("List"));

        menuInsertListUL.setText(Local.getString("Unordered"));
        menuInsertListUL.setToolTipText(Local.getString("Insert Unordered"));
        menuInsertListOL.setText(Local.getString("Ordered"));

        menuInsertSpecial.setText(Local.getString("Special"));
        menuInsertBR.setText(Local.getString("Line break"));
        menuInsertHR.setText(Local.getString("Horizontal rule"));

        menuInsertListOL.setToolTipText(Local.getString("Insert Ordered"));

        menuInsertChar.setText(Local.getString("Special character") + "...");
        menuInsertChar.setToolTipText(Local.getString(
                "Insert Special character"));
        menuInsertDate.setText(Local.getString("Current date"));
        menuInsertTime.setText(Local.getString("Current time"));
        menuInsertFile.setText(Local.getString("File") + "...");

        menuFormat.setText(Local.getString("Format"));
        menuFormatPStyle.setText(Local.getString("Paragraph style"));
        menuFormatP.setText(Local.getString("Paragraph"));
        menuFormatH1.setText(Local.getString("Header") + " 1");
        menuFormatH2.setText(Local.getString("Header") + " 2");
        menuFormatH3.setText(Local.getString("Header") + " 3");
        menuFormatH4.setText(Local.getString("Header") + " 4");
        menuFormatH5.setText(Local.getString("Header") + " 5");
        menuFormatH6.setText(Local.getString("Header") + " 6");
        menuFormatPre.setText(Local.getString("Preformatted text"));
        menuFormatBlcq.setText(Local.getString("Blockquote"));
        jjMenuFormatChStyle.setText(Local.getString("Character style"));
        menuFormatChNorm.setText(Local.getString("Normal"));
        menuFormatChEM.setText(Local.getString("Emphasis"));
        menuFormatChStrong.setText(Local.getString("Strong"));
        menuFormatChCode.setText(Local.getString("Code"));
        menuFormatChCite.setText(Local.getString("Cite"));
        menuFormatChSup.setText(Local.getString("Superscript"));
        menuFormatChSub.setText(Local.getString("Subscript"));
        menuFormatChCustom.setText(Local.getString("Custom style") + "...");
        menuFormatChB.setText(Local.getString("Bold"));
        menuFormatChB.setToolTipText(Local.getString("Bold"));
        menuFormatChI.setText(Local.getString("Italic"));
        menuFormatChI.setToolTipText(Local.getString("Italic"));
        menuFormatChU.setText(Local.getString("Underline"));
        menuFormatChU.setToolTipText(Local.getString("Underline"));
        menuFormatAlign.setText(Local.getString("Alignment"));
        menuFormatAlignL.setText(Local.getString("Left"));
        menuFormatAlignL.setToolTipText(Local.getString("Left"));
        menuFormatAlignC.setText(Local.getString("Center"));
        menuFormatAlignC.setToolTipText(Local.getString("Center"));
        menuFormatAlignR.setText(Local.getString("Right"));
        menuFormatAlignR.setToolTipText(Local.getString("Right"));
        menuFormatTable.setText(Local.getString("Table"));
        menuFormatTableInsR.setText(Local.getString("Insert row"));
        menuFormatTableInsC.setText(Local.getString("Insert cell"));
        menuFormatProperties.setText(Local.getString("Object properties")
                + "...");
        menuFormatProperties.setToolTipText(Local.getString(
                "Object properties"));

        menuGo.setText(Local.getString("Go"));
        menuGoHBack.setText(Local.getString("History back"));
        menuGoHBack.setToolTipText(Local.getString("History back"));
        menuGoFwd.setText(Local.getString("History forward"));
        menuGoFwd.setToolTipText(Local.getString("History forward"));
        menuGoDayBack.setText(Local.getString("One day back"));
        menuGoDayFwd.setText(Local.getString("One day forward"));
        menuGoToday.setText(Local.getString("To today"));

        menuInsertSpecial.setText(Local.getString("Special"));
        menuInsertBR.setText(Local.getString("Line break"));
        menuInsertBR.setToolTipText(Local.getString("Insert break"));
        menuInsertHR.setText(Local.getString("Horizontal rule"));
        menuInsertHR.setToolTipText(Local.getString("Insert Horizontal rule"));

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
        menuBar.add(menuEdit);
        menuBar.add(menuInsert);
        menuBar.add(menuFormat);
        menuBar.add(menuGo);
        menuBar.add(menuHelp);
        this.setJMenuBar(menuBar);
        //contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(statusBar, BorderLayout.SOUTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
        splitPane.add(projectsPanel, JSplitPane.TOP);
        splitPane.add(workPanel, JSplitPane.BOTTOM);
        menuEdit.add(menuEditUndo);
        menuEdit.add(menuEditRedo);
        menuEdit.addSeparator();
        menuEdit.add(menuEditCut);
        menuEdit.add(menuEditCopy);
        menuEdit.add(menuEditPaste);
        menuEdit.add(menuEditPasteSpec);
        menuEdit.addSeparator();
        menuEdit.add(menuEditSelectAll);
        menuEdit.addSeparator();
        menuEdit.add(menuEditFind);

        menuInsert.add(menuInsertImage);
        menuInsert.add(menuInsertTable);
        menuInsert.add(menuInsertLink);
        menuInsert.add(menuInsertList);
        //jMenuInsert.add(jMenuInsertSpecial);
        menuInsertList.add(menuInsertListUL);
        menuInsertList.add(menuInsertListOL);
        menuInsert.addSeparator();
        menuInsert.add(menuInsertBR);
        menuInsert.add(menuInsertHR);
        menuInsert.add(menuInsertChar);
        menuInsert.addSeparator();
        menuInsert.add(menuInsertDate);
        menuInsert.add(menuInsertTime);
        menuInsert.addSeparator();
        menuInsert.add(menuInsertFile);

        menuFormat.add(menuFormatPStyle);
        menuFormat.add(jjMenuFormatChStyle);
        menuFormat.add(menuFormatAlign);
        menuFormat.addSeparator();
        menuFormat.add(menuFormatTable);
        menuFormat.addSeparator();
        menuFormat.add(menuFormatProperties);
        menuFormatPStyle.add(menuFormatP);
        menuFormatPStyle.addSeparator();
        menuFormatPStyle.add(menuFormatH1);
        menuFormatPStyle.add(menuFormatH2);
        menuFormatPStyle.add(menuFormatH3);
        menuFormatPStyle.add(menuFormatH4);
        menuFormatPStyle.add(menuFormatH5);
        menuFormatPStyle.add(menuFormatH6);
        menuFormatPStyle.addSeparator();
        menuFormatPStyle.add(menuFormatPre);
        menuFormatPStyle.add(menuFormatBlcq);
        jjMenuFormatChStyle.add(menuFormatChNorm);
        jjMenuFormatChStyle.addSeparator();
        jjMenuFormatChStyle.add(menuFormatChB);
        jjMenuFormatChStyle.add(menuFormatChI);
        jjMenuFormatChStyle.add(menuFormatChU);
        jjMenuFormatChStyle.addSeparator();
        jjMenuFormatChStyle.add(menuFormatChEM);
        jjMenuFormatChStyle.add(menuFormatChStrong);
        jjMenuFormatChStyle.add(menuFormatChCode);
        jjMenuFormatChStyle.add(menuFormatChCite);
        jjMenuFormatChStyle.addSeparator();
        jjMenuFormatChStyle.add(menuFormatChSup);
        jjMenuFormatChStyle.add(menuFormatChSub);
        jjMenuFormatChStyle.addSeparator();
        jjMenuFormatChStyle.add(menuFormatChCustom);
        menuFormatAlign.add(menuFormatAlignL);
        menuFormatAlign.add(menuFormatAlignC);
        menuFormatAlign.add(menuFormatAlignR);
        menuFormatTable.add(menuFormatTableInsR);
        menuFormatTable.add(menuFormatTableInsC);
        menuGo.add(menuGoHBack);
        menuGo.add(menuGoFwd);
        menuGo.addSeparator();
        menuGo.add(menuGoDayBack);
        menuGo.add(menuGoDayFwd);
        menuGo.add(menuGoToday);

        splitPane.setBorder(null);
        workPanel.setBorder(null);

        setEnabledEditorMenus(false);

        projectsPanel.AddExpandListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (prPanelExpanded) {
                    prPanelExpanded = false;
                    splitPane.setDividerLocation(28);
                } else {
                    prPanelExpanded = true;
                    splitPane.setDividerLocation(0.2);
                }
            }
        });

        java.awt.event.ActionListener setMenusDisabled = new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setEnabledEditorMenus(false);
            }
        };

        dailyItemsPanel.taskB
                .addActionListener(setMenusDisabled);
        dailyItemsPanel.alarmB.addActionListener(
                setMenusDisabled);

        workPanel.getBusButton().addActionListener(setMenusDisabled);
        workPanel.getTourButton().addActionListener(setMenusDisabled);
        workPanel.getMapButton().addActionListener(setMenusDisabled);
        workPanel.getDriverButton().addActionListener(setMenusDisabled);

        Object fwo = Context.get("FRAME_WIDTH");
        Object fho = Context.get("FRAME_HEIGHT");
        if ((fwo != null) && (fho != null)) {
            int w = Integer.parseInt((String) fwo);//.intValue();
            int h = Integer.parseInt((String) fho);//.intValue();
            this.setSize(w, h);
        } else {
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        }

        Object xo = Context.get("FRAME_XPOS");
        Object yo = Context.get("FRAME_YPOS");
        if ((xo != null) && (yo != null)) {
            int x = Integer.parseInt((String) xo);//.intValue();
            int y = Integer.parseInt((String) yo);//.intValue();
            this.setLocation(x, y);
        }

        String pan = (String) Context.get("CURRENT_PANEL");
        if (pan != null) {
            workPanel.selectPanel(pan);
        }

        CurrentProject.addProjectListener(new ProjectListener() {

            public void projectChange(Project prj, ResourcesList rl) {
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
        this.menuEdit.setEnabled(enabled);
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