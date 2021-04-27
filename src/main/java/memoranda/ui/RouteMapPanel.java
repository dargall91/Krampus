package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import main.java.memoranda.Coordinate;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.Node;
import main.java.memoranda.NodeColl;
import main.java.memoranda.Route;
import main.java.memoranda.RouteOptimizer;
import main.java.memoranda.Tour;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.Local;

/**
 * RouteMapPanel is the panel for accessing the RouteMap to see the visualization of the Route Map.
 *
 * @version 2021-04-25
 * @autor alexeya, Kevin Dolan, Chris Boveda, John Thurstonson
 */
public class RouteMapPanel extends JPanel {
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 150;

    private BorderLayout borderLayout1 = new BorderLayout();
    private JToolBar toolBar = new JToolBar();
    //private RouteMap map = new RouteMap(this);
    private JScrollPane scrollPane = new JScrollPane();
    private RouteTable routeTable = new RouteTable(this);
    private JScrollPane rScrollPane = new JScrollPane();
    private RouteMap map = new RouteMap(this);

    private JButton newRouteB = new JButton();
    private JButton removeRouteB = new JButton();
    private JButton optRouteB = new JButton();
    private JButton optRouteWithStartB = new JButton();
    private JButton refreshB = new JButton();
    private JButton debugAddNodesB = new JButton();

    private JPopupMenu resPPMenu = new JPopupMenu();
    private JMenuItem ppRemoveRes = new JMenuItem();
    private JMenuItem ppNewRes = new JMenuItem();
    private JMenuItem ppRefresh = new JMenuItem();

    private DailyItemsPanel parentPanel;

    /**
     * Constructor for RouteMapPanel.
     */
    public RouteMapPanel(DailyItemsPanel parentPanel) {
        try {
            this.parentPanel = parentPanel;
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    /**
     * Initiates the JButtons on the panel.
     *
     * @throws Exception
     */
    void jbInit() throws Exception {
        toolBar.setFloatable(false);
        this.setLayout(borderLayout1);
        map.setMaximumSize(new Dimension(32767, 32767));

        /* New Route Button */
        newRouteB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        newRouteB.setText("New");
        newRouteB.setEnabled(true);
        newRouteB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        newRouteB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        newRouteB.setToolTipText(Local.getString("New route."));
        newRouteB.setRequestFocusEnabled(false);
        newRouteB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        newRouteB.setFocusable(false);
        newRouteB.setBorderPainted(true);
        newRouteB.addActionListener((e) -> {
            try {
                newRouteB_actionPerformed(e);
            } catch (DuplicateKeyException duplicateKeyException) {
                duplicateKeyException.printStackTrace();
            }
        });

        /* Remove Route Button */
        removeRouteB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        removeRouteB.setText("Remove");
        removeRouteB.setEnabled(true);
        removeRouteB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        removeRouteB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        removeRouteB.setToolTipText(Local.getString("Remove route."));
        removeRouteB.setRequestFocusEnabled(false);
        removeRouteB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        removeRouteB.setFocusable(false);
        removeRouteB.setBorderPainted(true);
        removeRouteB.addActionListener((e) -> removeRouteB_actionPerformed(e)); //todo

        /* Optimize Button */
        optRouteB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        optRouteB.setText("Optimize");
        optRouteB.setEnabled(true);
        optRouteB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteB.setToolTipText(Local.getString("Optimize route."));
        optRouteB.setRequestFocusEnabled(false);
        optRouteB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteB.setFocusable(false);
        optRouteB.setBorderPainted(true);
        optRouteB.addActionListener((e) -> optRouteB_actionPerformed(e));

        /* Optimize w/ Start Button */
        optRouteWithStartB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        optRouteWithStartB.setText("Optimize w/Start");
        optRouteWithStartB.setEnabled(true);
        optRouteWithStartB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteWithStartB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteWithStartB.setToolTipText(Local.getString("Optimize route with best start."));
        optRouteWithStartB.setRequestFocusEnabled(false);
        optRouteWithStartB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteWithStartB.setFocusable(false);
        optRouteWithStartB.setBorderPainted(true);
        optRouteWithStartB.addActionListener((e) -> optRouteWithStartB_actionPerformed(e));

        /* Refresh Button */
        refreshB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        refreshB.setText("Refresh");
        refreshB.setEnabled(true);
        refreshB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        refreshB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        refreshB.setToolTipText(Local.getString("Refresh map."));
        refreshB.setRequestFocusEnabled(false);
        refreshB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        refreshB.setFocusable(false);
        refreshB.setBorderPainted(true);
        refreshB.addActionListener((e) -> {
            routeTable.refresh();
            //map.refresh(); //todo
        });

        /* Fancy Debugger Button */
        debugAddNodesB.setText("[debug] Add Node");
        debugAddNodesB.setEnabled(true);
        debugAddNodesB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        debugAddNodesB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        debugAddNodesB.setToolTipText(
                Local.getString("Adds random nodes to the currently selected route"));
        debugAddNodesB.setRequestFocusEnabled(false);
        debugAddNodesB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        debugAddNodesB.setFocusable(false);
        debugAddNodesB.setBorderPainted(true);
        debugAddNodesB.addActionListener((e) -> debugAddNodesB_actionPerformed(e));

        /* Route Table */
        routeTable.setMaximumSize(new Dimension(32767, 32767));
        routeTable.setRowHeight(24);
        rScrollPane.getViewport().setBackground(Color.lightGray);
        rScrollPane.getViewport().add(routeTable, null);

        toolBar.add(newRouteB, null);
        toolBar.addSeparator();
        toolBar.add(removeRouteB, null);
        toolBar.addSeparator();
        toolBar.add(optRouteB, null);
        toolBar.addSeparator();
        toolBar.add(optRouteWithStartB, null);
        toolBar.addSeparator();
        toolBar.add(refreshB, null);
        toolBar.addSeparator();
        toolBar.add(debugAddNodesB, null);

        resPPMenu.addSeparator();
        resPPMenu.add(ppNewRes);
        resPPMenu.addSeparator();
        resPPMenu.add(ppRemoveRes);
        resPPMenu.addSeparator();
        resPPMenu.add(ppRefresh);

        scrollPane.getViewport().add(map, null);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(rScrollPane, BorderLayout.EAST);
        this.add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Event handler for the "Remove Route" button.
     *
     * @param e action event
     */
    private void removeRouteB_actionPerformed(ActionEvent e) {
        Route route = routeTable.getRoute();

        //do nothing if there is no route selected
        if (route != null) {
            int result = JOptionPane
                    .showConfirmDialog(null, "Delete " + route.getName() + "?", "Delete Route",
                            JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                //get list of associated tours, remove their drivers and buses, then remove them.
                //A Tour cannot exist without a route, buses and drivers can't be scheduled for
                //tours that don't exist.
                ArrayList<Tour> tours = route.getTours();

                for (int i = 0; i < tours.size(); i++) {
                    if (tours.get(i).getDriver() != null) {
                        tours.get(i).getDriver().delTour(tours.get(i));
                    }

                    if (tours.get(i).getBus() != null) {
                        tours.get(i).getBus().delTour(tours.get(i));
                    }

                    CurrentProject.getTourColl().del(tours.get(i).getID());
                }

                CurrentProject.getRouteColl().del(route.getID());
                try {
                    CurrentStorage.get()
                            .storeRouteList(CurrentProject.get(), CurrentProject.getRouteColl());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                routeTable.refresh();
                parentPanel.refresh();
            }
        }
    }

    /**
     * Event handler for the "New Route" button.
     *
     * @param e action event
     */
    public void newRouteB_actionPerformed(ActionEvent e) throws DuplicateKeyException {
        RouteDialog dialog = new RouteDialog(App.getFrame(), "New Route");
        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();

        dialog.setLocation(
                (frmSize.width - dialog.getSize().width) / 2 + loc.x,
                (frmSize.height - dialog.getSize().height) / 2 + loc.y);
        dialog.setVisible(dialog.getError() != 1);

        if (dialog.isComplete()) {
            Route route = CurrentProject.getRouteColl().newItem();
            route.setName(dialog.getName());

            NodeColl nodeColl = CurrentProject.getNodeColl();
            Node node = nodeColl.newItem();
            node.setCoords(new Coordinate(33.431245, -111.943588));
            node.setName("origin");
            node.hide();
            route.addNode(node);
            nodeColl.add(node);

            node=nodeColl.newItem();
            node.setCoords(new Coordinate(33.411095, -111.926076));
            node.setName("outlier");
            node.hide();
            route.addNode(node);
            nodeColl.add(node);

            try {
                CurrentProject.getRouteColl().add(route);
                CurrentStorage.get()
                        .storeRouteList(CurrentProject.get(), CurrentProject.getRouteColl());
                routeTable.refresh();
            } catch (DuplicateKeyException | IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    /**
     * Event handler for the "Optimize" button.
     *
     * @param e action event
     */
    public void optRouteB_actionPerformed(ActionEvent e) {
        if (routeTable.getSelectedRow() == -1) {
            return;
        }
        Route r = (Route) CurrentProject.getRouteColl().getRoutes().toArray()[routeTable
                .getSelectedRow()];
        new RouteOptimizer(r).optimize();
        map.refresh();
        routeTable.refresh();
        //map.refresh();    
    }


    /**
     * Event handler for the "Optimize w/ Start" button.
     *
     * @param e action event
     */
    public void optRouteWithStartB_actionPerformed(ActionEvent e) {
        if (routeTable.getSelectedRow() == -1) {
            return;
        }
        Route r = (Route) CurrentProject.getRouteColl().getRoutes().toArray()[routeTable
                .getSelectedRow()];
        new RouteOptimizer(r).optimizeWithStart();
        map.refresh();
        routeTable.refresh();
        //map.refresh();    
    }


    /**
     * Debugging utility while the functionality to select nodes is being developed.
     */
    public void debugAddNodesB_actionPerformed(ActionEvent e) {
        if (routeTable.getSelectedRow() == -1) {
            return;
        }
        Route r = (Route) CurrentProject.getRouteColl().getRoutes().toArray()[routeTable
                .getSelectedRow()];
        Node node = CurrentProject.getNodeColl().newItem();

        Random rand = new Random();

        node.setCoords(new Coordinate(
                rand.nextDouble() * 90,
                rand.nextDouble() * 90));
        node.setName("DEBUGNODE");
        r.addNode(node);
        try {
            CurrentProject.getNodeColl().add(node);
            CurrentStorage.get().storeNodeList(CurrentProject.get(), CurrentProject.getNodeColl());
            routeTable.refresh();
        } catch (IOException | DuplicateKeyException exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Getter for RouteTable.
     *
     * @return routeTable
     */
    public RouteTable getRouteTable() {
        return routeTable;
    }

    /**
     * Getter for RouteMap.
     *
     * @return map
     */
    public RouteMap getRouteMap() {
        return map;
    }

    //TODO: Review all of the following for usefulness

//        void newResB_actionPerformed(ActionEvent e) {
//        AddResourceDialog dlg = new AddResourceDialog(App.getFrame(), Local.getString("New
//        resource"));
//        Dimension frmSize = App.getFrame().getSize();
//        Point loc = App.getFrame().getLocation();
//        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x, (frmSize.height -
//        dlg.getSize().height) / 2 + loc.y);
//        dlg.setVisible(true);
//        if (dlg.CANCELLED)
//            return;
//        if (dlg.localFileRB.isSelected()) {
//            String fpath = dlg.pathField.getText();
//            MimeType mt = MimeTypesList.getMimeTypeForFile(fpath);
//            if (mt.getMimeTypeId().equals("__UNKNOWN")) {
//                mt = addResourceType(fpath);
//                if (mt == null)
//                    return;
//            }
//            if (!checkApp(mt))
//                return;
//            // if file if projectFile, than copy the file and change url.
//            if (dlg.projectFileCB.isSelected()) {
//                fpath = copyFileToProjectDir(fpath);
//                CurrentProject.getResourcesList().addResource(fpath, false, true);
//            }
//            else
//                CurrentProject.getResourcesList().addResource(fpath);
//
//            map.tableChanged();
//        }
//        else {
//            if (!Util.checkBrowser())
//                return;
//            CurrentProject.getResourcesList().addResource(dlg.urlField.getText(), true, false);
//            map.tableChanged();
//        }
//    }
//
//    void removeResB_actionPerformed(ActionEvent e) {
//        int[] toRemove = map.getSelectedRows();
//        String msg = "";
//        if (toRemove.length == 1)
//            msg =
//                Local.getString("Remove the shortcut to resource")
//                    + "\n'"
//                    + map.getModel().getValueAt(toRemove[0], 0)
//                    + "'";
//
//        else
//            msg = Local.getString("Remove") + " " + toRemove.length + " " + Local.getString
//            ("shortcuts");
//        msg +=
//            "\n"
//            + Local.getString("Are you sure?");
//        int n =
//            JOptionPane.showConfirmDialog(
//                App.getFrame(),
//                msg,
//                Local.getString("Remove resource"),
//                JOptionPane.YES_NO_OPTION);
//        if (n != JOptionPane.YES_OPTION)
//            return;
//        for (int i = 0; i < toRemove.length; i++) {
//                CurrentProject.getResourcesList().removeResource(
//                        ((Resource) map.getModel().getValueAt(toRemove[i], ResourcesTable
//                        ._RESOURCE)).getPath());
//        }
//        map.tableChanged();
//    }
//
//    MimeType addResourceType(String fpath) {
//        ResourceTypeDialog dlg = new ResourceTypeDialog(App.getFrame(), Local.getString
//        ("Resource type"));
//        Dimension dlgSize = new Dimension(420, 300);
//        dlg.setSize(dlgSize);
//        Dimension frmSize = App.getFrame().getSize();
//        Point loc = App.getFrame().getLocation();
//        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize
//        .height) / 2 + loc.y);
//        dlg.ext = MimeTypesList.getExtension(fpath);
//        dlg.setVisible(true);
//        if (dlg.CANCELLED)
//            return null;
//        int ix = dlg.getTypesList().getSelectedIndex();
//        MimeType mt = (MimeType) MimeTypesList.getAllMimeTypes().toArray()[ix];
//        mt.addExtension(MimeTypesList.getExtension(fpath));
//        CurrentStorage.get().storeMimeTypesList();
//        return mt;
//    }
//
//    boolean checkApp(MimeType mt) {
//        String appId = mt.getAppId();
//        AppList appList = MimeTypesList.getAppList();
//        File d;
//        if (appId == null) {
//            appId = Util.generateId();
//            d = new File("/");
//        }
//        else {
//            File exe = new File(appList.getFindPath(appId) + "/" + appList.getExec(appId));
//            if (exe.isFile())
//                return true;
//            d = new File(exe.getParent());
//            while (!d.exists())
//                d = new File(d.getParent());
//        }
//        SetAppDialog dlg =
//            new SetAppDialog(
//                App.getFrame(),
//                Local.getString(Local.getString("Select the application to open files of type")
//                +" '" + mt.getLabel() + "'"));
//        Dimension dlgSize = new Dimension(420, 300);
//        dlg.setSize(dlgSize);
//        Dimension frmSize = App.getFrame().getSize();
//        Point loc = App.getFrame().getLocation();
//        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize
//        .height) / 2 + loc.y);
//        dlg.setDirectory(d);
//        dlg.appPanel.argumentsField.setText("$1");
//        dlg.setVisible(true);
//        if (dlg.CANCELLED)
//            return false;
//        File f = new File(dlg.appPanel.applicationField.getText());
//
//        appList.addOrReplaceApp(
//            appId,
//            f.getParent().replace('\\', '/'),
//            f.getName().replace('\\', '/'),
//            dlg.appPanel.argumentsField.getText());
//        mt.setApp(appId);
//        /*appList.setFindPath(appId, chooser.getSelectedFile().getParent().replace('\\','/'));
//        appList.setExec(appId, chooser.getSelectedFile().getName().replace('\\','/'));
//        CurrentStorage.get().storeMimeTypesList();
//        return true;
//    }
//
//
//    void runApp(String fpath) {
//        MimeType mt = MimeTypesList.getMimeTypeForFile(fpath);
//        if (mt.getMimeTypeId().equals("__UNKNOWN")) {
//            mt = addResourceType(fpath);
//            if (mt == null)
//                return;
//        }
//        if (!checkApp(mt))
//            return;
//        String[] command = MimeTypesList.getAppList().getCommand(mt.getAppId(), fpath);
//        if (command == null)
//            return;
//        //DEBUG
//        System.out.println("Run: " + command[0]);
//        try {
//            Runtime.getRuntime().exec(command);
//        }
//        catch (Exception ex) {
//            new ExceptionDialog(ex, "Failed to run an external application <br><code>"
//                    +command[0]+"</code>", "Check the application path and command line
//                    parameters for this resource type " +
//                            "(File-&gt;Preferences-&gt;Resource types).");
//        }
//    }
//
//    void runBrowser(String url) {
//        Util.runBrowser(url);
//    }
//
//    class PopupListener extends MouseAdapter {
//
//        public void mouseClicked(MouseEvent e) {
//            if ((e.getClickCount() == 2) && (map.getSelectedRow() > -1)) {
//                String path = (String) map.getValueAt(map.getSelectedRow(), 3);
//                if (path.length() >0)
//                    runApp(path);
//                else
//                    runBrowser((String) map.getValueAt(map.getSelectedRow(), 0));
//            }
//            //editTaskB_actionPerformed(null);
//        }
//
//                public void mousePressed(MouseEvent e) {
//                    maybeShowPopup(e);
//                }
//
//                public void mouseReleased(MouseEvent e) {
//                    maybeShowPopup(e);
//                }
//
//                private void maybeShowPopup(MouseEvent e) {
//                    if (e.isPopupTrigger()) {
//                        resPPMenu.show(e.getComponent(), e.getX(), e.getY());
//                    }
//                }
//
//    }
//    void refreshB_actionPerformed(ActionEvent e) {
//        map.tableChanged();
//    }
//
//  /*void ppRun_actionPerformed(ActionEvent e) {
//    String path = (String) map.getValueAt(map.getSelectedRow(), 3);
//                if (path.length() >0)
//                    runApp(path);
//                else
//                    runBrowser((String) map.getValueAt(map.getSelectedRow(), 0));
//  }
//  void ppRemoveRes_actionPerformed(ActionEvent e) {
//    removeResB_actionPerformed(e);
//  }
//  void ppNewRes_actionPerformed(ActionEvent e) {
//    newResB_actionPerformed(e);
//  }
//
//  void ppRefresh_actionPerformed(ActionEvent e) {
//     map.tableChanged();
//  }*/
//
//  /**
//   * Copy a file to the directory of the current project
//   * @param srcStr The path of the source file.
//   * @param destStr The destination path.
//   * @return The new path of the file.
//   */
//  String copyFileToProjectDir(String srcStr) {
//
//      String JN_DOCPATH = Util.getEnvDir();
//
//      String baseName;
//      int i = srcStr.lastIndexOf( File.separator );
//        if ( i != -1 ) {
//            baseName = srcStr.substring(i+1);
//        } else
//            baseName = srcStr;
//
//      String destStr = JN_DOCPATH + CurrentProject.get().getID()
//                         + File.separator + "_projectFiles" + File.separator + baseName;
//
//      File f = new File(JN_DOCPATH + CurrentProject.get().getID() + File.separator +
//      "_projectFiles");
//      if (!f.exists()) {
//          f.mkdirs();
//      }
//      System.out.println("[DEBUG] Copy file from: "+srcStr+" to: "+destStr);
//
//      try {
//         FileInputStream in = new FileInputStream(srcStr);
//         FileOutputStream out = new FileOutputStream(destStr);
//         byte[] buf = new byte[4096];
//         int len;
//         while ((len = in.read(buf)) > 0) {
//           out.write(buf, 0, len);
//         }
//         out.close();
//         in.close();
//       }
//       catch (IOException e) {
//         System.err.println(e.toString());
//       }
//
//  return destStr;
//  }
}