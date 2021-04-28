package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

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
 * @author alexeya, Kevin Dolan, Chris Boveda, John Thurstonson
 * @version 2021-04-27
 */
public class RouteMapPanel extends JPanel {
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 150;

    private final BorderLayout borderLayout1 = new BorderLayout();
    private final JToolBar toolBar = new JToolBar();
    private final JScrollPane scrollPane = new JScrollPane();
    private final RouteTable routeTable = new RouteTable(this);
    private final JScrollPane routeScrollPane = new JScrollPane();
    private final NodeTable nodeTable = new NodeTable(routeTable);
    private final JScrollPane nodeScrollPane = new JScrollPane();
    private final RouteMap map = new RouteMap(this);

    private final JButton newRouteB = new JButton();
    private final JButton removeRouteB = new JButton();
    private final JButton optRouteB = new JButton();
    private final JButton optRouteWithStartB = new JButton();
    private final JButton refreshB = new JButton();
    //private JButton debugAddNodesB = new JButton();

    private final JPopupMenu resPopMenu = new JPopupMenu();
    private final JMenuItem ppRemoveRes = new JMenuItem();
    private final JMenuItem ppNewRes = new JMenuItem();
    private final JMenuItem ppRefresh = new JMenuItem();

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
     * @throws Exception    exception
     */
    void jbInit() throws Exception {
        toolBar.setFloatable(false);
        this.setLayout(borderLayout1);
        map.setMaximumSize(new Dimension(32767, 32767));
        routeTable.setNodeTable(nodeTable);

        /* New Route Button */
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
        removeRouteB.setText("Remove");
        removeRouteB.setEnabled(true);
        removeRouteB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        removeRouteB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        removeRouteB.setToolTipText(Local.getString("Remove route."));
        removeRouteB.setRequestFocusEnabled(false);
        removeRouteB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        removeRouteB.setFocusable(false);
        removeRouteB.setBorderPainted(true);
        removeRouteB.addActionListener((e) -> removeRouteB_actionPerformed(e));

        /* Optimize Button */
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
        refreshB.setText("Refresh");
        refreshB.setEnabled(true);
        refreshB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        refreshB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        refreshB.setToolTipText(Local.getString("Refresh map."));
        refreshB.setRequestFocusEnabled(false);
        refreshB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        refreshB.setFocusable(false);
        refreshB.setBorderPainted(true);
        refreshB.addActionListener((e) -> refresh());

        /* Fancy Debugger Button */
        /* Removed for release
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
         */

        /* Route Table */
        routeTable.setMaximumSize(new Dimension(32767, 32767));
        routeTable.setRowHeight(24);
        routeScrollPane.getViewport().setBackground(Color.lightGray);
        routeScrollPane.getViewport().add(routeTable, null);
        routeScrollPane.setPreferredSize(new Dimension(400, 32767));

        /* Node Table */
        nodeTable.setMaximumSize(new Dimension(32767, 32767));
        nodeTable.setRowHeight(24);
        nodeScrollPane.getViewport().setBackground(Color.gray);
        nodeScrollPane.getViewport().add(nodeTable, null);
        nodeScrollPane.setPreferredSize(new Dimension(400, 32767));

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
        //toolBar.add(debugAddNodesB, null);

        resPopMenu.addSeparator();
        resPopMenu.add(ppNewRes);
        resPopMenu.addSeparator();
        resPopMenu.add(ppRemoveRes);
        resPopMenu.addSeparator();
        resPopMenu.add(ppRefresh);

        scrollPane.getViewport().add(map, null);
        scrollPane.setPreferredSize(new Dimension(1120, 32767));

        this.add(scrollPane, BorderLayout.WEST);
        this.add(nodeScrollPane, BorderLayout.CENTER);
        this.add(routeScrollPane, BorderLayout.EAST);
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
                refresh();
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

            node = nodeColl.newItem();
            node.setCoords(new Coordinate(33.411095, -111.926076));
            node.setName("outlier");
            node.hide();
            route.addNode(node);
            nodeColl.add(node);

            try {
                CurrentProject.getRouteColl().add(route);
                CurrentStorage.get()
                    .storeRouteList(CurrentProject.get(), CurrentProject.getRouteColl());
                refresh();
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
        refresh();
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
        refresh();
    }


    /*
     * Debugging utility while the functionality to select nodes is being developed.
     * Removed for release.
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
            refresh();
        } catch (IOException | DuplicateKeyException exception) {
            exception.printStackTrace();
        }

    }
     */

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


    /**
     * Refreshes the components of this panel.
     */
    public void refresh() {
        routeTable.refresh();
        nodeTable.refresh();
        map.refresh();
    }
}