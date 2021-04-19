package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Driver;
import main.java.memoranda.Tour;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.Local;


/**
 * JPanel for tours. Allows tours to be added, edited and removed
 *
 * @author John Thurstonson
 * @version 04/10/2021
 * <p>
 *      References:
 *      Used EventsPanel.java as base, v 1.25 2005/02/19 10:06:25 rawsushi Exp
 *      Referenced DriverPanel.java, Author: Derek Argall, Version: 04/05/2021
 * </p>
 */
public class TourPanel extends JPanel {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JToolBar toursToolBar = new JToolBar();
    private JScrollPane scrollPane = new JScrollPane();
    private TourTable tourTable = new TourTable();
    private JPopupMenu tourMenu = new JPopupMenu();
    private JMenuItem ppEditTour = new JMenuItem();
    private JMenuItem ppRemoveTour = new JMenuItem();
    private JMenuItem ppNewTour = new JMenuItem();
    private DailyItemsPanel parentPanel = null;

    /**
     * TourPanel Constructor. Creates TourPanel with current tour information.
     *
     * @param parentPanel DailyItemsPanel
     */
    public TourPanel(DailyItemsPanel parentPanel) {
        try {
            this.parentPanel = parentPanel;
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        toursToolBar.setFloatable(false);

        JButton newTour = new JButton("New Tour");
        newTour.setEnabled(true);
        newTour.setMaximumSize(new Dimension(100, 24));
        newTour.setMinimumSize(new Dimension(100, 24));
        newTour.setBorderPainted(true);
        newTour.setToolTipText(Local.getString("New Tour"));
        newTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTourB_actionPerformed(e);
            }
        });

        JButton editTour = new JButton("Edit Tour");
        editTour.setEnabled(false);
        editTour.setMaximumSize(new Dimension(100, 24));
        editTour.setMinimumSize(new Dimension(100, 24));
        editTour.setBorderPainted(true);
        editTour.setToolTipText(Local.getString("Edit Tour"));
        editTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editTourB_actionPerformed(e);
            }
        });

        JButton removeTour = new JButton("Remove Tour");
        removeTour.setEnabled(false);
        removeTour.setMaximumSize(new Dimension(100, 24));
        removeTour.setMinimumSize(new Dimension(100, 24));
        removeTour.setBorderPainted(true);
        removeTour.setToolTipText(Local.getString("Remove Tour"));
        removeTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeTourB_actionPerformed(e);
            }
        });


        scrollPane.getViewport().setBackground(Color.white);
        tourTable.setMaximumSize(new Dimension(32767, 32767));
        tourTable.setRowHeight(24);

        ppEditTour.setText("Edit Tour");
        ppEditTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppEditTour_actionPerformed(e);
            }
        });


        ppRemoveTour.setText("Remove Tour");
        ppRemoveTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppRemoveTour_actionPerformed(e);
            }
        });


        ppNewTour.setText("New Tour");
        ppNewTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppNewTour_actionPerformed(e);
            }
        });

        scrollPane.getViewport().add(tourTable, null);
        this.add(scrollPane, BorderLayout.CENTER);


        toursToolBar.addSeparator(new Dimension(8, 24));
        toursToolBar.add(newTour, null);
        toursToolBar.addSeparator(new Dimension(8, 24));
        toursToolBar.add(removeTour, null);
        toursToolBar.addSeparator(new Dimension(8, 24));
        toursToolBar.add(editTour, null);

        this.add(toursToolBar, BorderLayout.NORTH);

        PopupListener ppListener = new PopupListener();
        scrollPane.addMouseListener(ppListener);
        tourTable.addMouseListener(ppListener);


        tourTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean enbl = tourTable.getSelectedRow() > -1;
                editTour.setEnabled(enbl);
                ppEditTour.setEnabled(enbl);
                removeTour.setEnabled(enbl);
                ppRemoveTour.setEnabled(enbl);
            }
        });

        ppEditTour.setEnabled(false);
        ppRemoveTour.setEnabled(false);
        tourMenu.add(ppNewTour);
        tourMenu.addSeparator();
        tourMenu.add(ppEditTour);
        tourMenu.addSeparator();
        tourMenu.add(ppRemoveTour);


    }


    /**
     * Attempts to create new tour when new tour action is performed.
     *
     * @param e new tour ActionEvent
     */
    public void newTourB_actionPerformed(ActionEvent e) {
        TourDialog dlg = new TourDialog(App.getFrame());
        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();

        dlg.setLocation(
                (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                (frmSize.height - dlg.getSize().height) / 2
                        + loc.y);
        if (dlg.getError() == 1) {
            dlg.setVisible(false);
        } else {
            dlg.setVisible(true);
        }

        if (!dlg.isCancelled()) {
            Tour tour = CurrentProject.getTourColl().newItem();
            tour.setName(dlg.getName());
            tour.setRoute(dlg.getRoute());
            tour.setTime(dlg.getTime());

            try {
                dlg.getBus().addTour(tour);
                CurrentProject.getTourColl().add(tour);
                CurrentStorage.get().storeTourList(CurrentProject.get(), CurrentProject.getTourColl());
                tourTable.refresh();
                parentPanel.getBusScheduleTable().tableChanged();
            } catch (Exception f) {
                f.printStackTrace();
            }
        }
    }

    /**
     * Allows user to edit tour.
     *
     * @param e edit tour ActionEvent
     */
    public void editTourB_actionPerformed(ActionEvent e) {
        Tour tour = (Tour) tourTable.getModel().getValueAt(tourTable.getSelectedRow(), TourTable.TOUR);
        TourDialog dlg = new TourDialog(App.getFrame(), tour);
        dlg.setName(tour.getName());
        dlg.setRoute(tour.getRoute());
        dlg.setTime(tour.getTime());
        dlg.setBus(tour.getBus());

        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();

        dlg.setLocation(
                (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                (frmSize.height - dlg.getSize().height) / 2
                        + loc.y);
        dlg.setVisible(true);

        if (!dlg.isCancelled()) {

            tour.setName(dlg.getName());
            tour.setRoute(dlg.getRoute());
            tour.setTime(dlg.getTime());
            
            try {
                tour.getBus().delTour(tour);
                dlg.getBus().addTour(tour);
            } catch (DuplicateKeyException e2) {
                e2.printStackTrace();
            }
            
            if (dlg.getDriver() != null && tour.getDriver() == null) {
                try {
                    dlg.getDriver().addTour(tour);
                } catch (DuplicateKeyException e1) {
                    e1.printStackTrace();
                }
            } else if (dlg.getDriver() != null && tour.getDriver() != null) {
                try {
                    tour.getDriver().delTour(tour);
                    dlg.getDriver().addTour(tour);
                } catch (DuplicateKeyException e1) {
                    e1.printStackTrace();
                }
            }

            try {

                CurrentStorage.get().storeTourList(CurrentProject.get(), CurrentProject.getTourColl());
                CurrentStorage.get().storeDriverList(CurrentProject.get(), CurrentProject.getDriverColl());
                CurrentStorage.get().storeBusList(CurrentProject.get(), CurrentProject.getBusColl());
                tourTable.refresh();
                parentPanel.getDriverScheduleTable().tableChanged();
                parentPanel.getBusScheduleTable().tableChanged();
                
            } catch (Exception f) {
                f.printStackTrace();
            }
        }
    }

    /**
     * Allows user to delete tour.
     *
     * @param e remove tour ActionEvent
     */
    public void removeTourB_actionPerformed(ActionEvent e) {
        Driver driver;
        Tour tour = (Tour) tourTable.getModel().getValueAt(tourTable.getSelectedRow(), TourTable.TOUR);
        int result = JOptionPane.showConfirmDialog(null, "Delete " + tour.getName()
                + "?", "Delete Tour", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {


            if (tour.getDriver() != null) {
                tour.getDriver().delTour(tour);
                parentPanel.getDriverScheduleTable().tableChanged();
            }
            
            if (tour.getBus() != null) {
                tour.getBus().delTour(tour);
                parentPanel.getBusScheduleTable().tableChanged();
            }

            CurrentProject.getTourColl().del(tour.getID());

            try {

                CurrentStorage.get().storeTourList(CurrentProject.get(), CurrentProject.getTourColl());
                CurrentStorage.get().storeDriverList(CurrentProject.get(), CurrentProject.getDriverColl());
                tourTable.refresh();
            } catch (Exception f) {
                f.printStackTrace();
            }


        }


    }


    /**
     * PopupListener for mouse on specific tour.
     *
     * @author John Thurstonson
     * @version 04/10/2021
     */
    private class PopupListener extends MouseAdapter {

        /**
         * Double click for edit.
         */
        public void mouseClicked(MouseEvent e) {
            if ((e.getClickCount() == 2) && (tourTable.getSelectedRow() > -1)) {
                editTourB_actionPerformed(null);
            }
        }

        /**
         * Mouse clicked.
         */
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        /**
         * Mouse released.
         */
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        /**
         * Shows pop up for add, edit, remove.
         *
         * @param e mouse click
         */
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                tourMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }

    /**
     * Edit tour selected.
     *
     * @param e edit even
     */
    public void ppEditTour_actionPerformed(ActionEvent e) {
        editTourB_actionPerformed(e);
    }

    /**
     * Remove tour selected.
     *
     * @param e remove tour
     */
    public void ppRemoveTour_actionPerformed(ActionEvent e) {
        removeTourB_actionPerformed(e);
    }

    /**
     * New tour selected.
     *
     * @param e new tour
     */
    public void ppNewTour_actionPerformed(ActionEvent e) {
        newTourB_actionPerformed(e);
    }
    
    /**
     * Refreshes the table. Should be called when changes are made in another tab.
     */
    public void refresh() {
        tourTable.refresh();
    }
}