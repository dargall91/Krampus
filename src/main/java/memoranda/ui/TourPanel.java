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
import main.java.memoranda.util.Local;


/**
 * JPanel for tours. Allows tours to be added, edited and removed
 * 
 * @author John Thurstonson
 * @version 04/10/2021
 * 
 * References:
 * Used EventsPanel.java as base, v 1.25 2005/02/19 10:06:25 rawsushi Exp
 * Referenced DriverPanel.java, Author: Derek Argall, Version: 04/05/2021
 *
 */
public class TourPanel extends JPanel {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JToolBar toursToolBar = new JToolBar();
    private JScrollPane scrollPane = new JScrollPane();
    private TourTable tourTable = new TourTable();
    private JPopupMenu tourPPMenu = new JPopupMenu();
    private JMenuItem ppEditTour = new JMenuItem();
    private JMenuItem ppRemoveTour = new JMenuItem();
    private JMenuItem ppNewTour = new JMenuItem();
    private DailyItemsPanel parentPanel = null;
    //private DriverScheduleTable scheduleTable;

    /**
     * TourPanel Constructor
     * Creates TourPanel with current tour information
     * 
     * @param _parentPanel DailyItemsPanel
     */
    public TourPanel(DailyItemsPanel _parentPanel) {
        try {
            parentPanel = _parentPanel;
            jbInit();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }
    
    void jbInit() throws Exception {
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
                ppEditEvent_actionPerformed(e);
            }
        });
        
      
        ppRemoveTour.setText("Remove Tour");
        ppRemoveTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppRemoveEvent_actionPerformed(e);
            }
        });
        
        
        ppNewTour.setText("New Tour");
        ppNewTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppNewEvent_actionPerformed(e);
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
        tourPPMenu.add(ppNewTour);
        tourPPMenu.addSeparator();
        tourPPMenu.add(ppEditTour);
        tourPPMenu.addSeparator();
        tourPPMenu.add(ppRemoveTour);
		
		
    }

    

    /**
     * Attempts to create new tour when new tour action is performed
     * 
     * @param e new tour ActionEvent
     */
    void newTourB_actionPerformed(ActionEvent e) {
        TourDialog dlg = new TourDialog(App.getFrame(), "Add Tour");
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
            tour.setBus(dlg.getBus());
            try {
                CurrentProject.getTourColl().add(tour);
                CurrentStorage.get().storeTourList(CurrentProject.get(), CurrentProject.getTourColl());
                tourTable.refresh();
            } catch (Exception f) {
                f.printStackTrace();
            }
        }
    }
    
    /**
     * Allows user to edit tour
     * 
     * @param e edit tour ActionEvent
     */
    void editTourB_actionPerformed(ActionEvent e) {
        Tour tour = (Tour) tourTable.getModel().getValueAt(tourTable.getSelectedRow(), TourTable.TOUR);
        TourDialog dlg = new TourDialog(App.getFrame(), "Edit Tour");
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
            tour.setBus(dlg.getBus());
            try {
                
                CurrentStorage.get().storeTourList(CurrentProject.get(), CurrentProject.getTourColl());
                tourTable.refresh();
                
            } catch (Exception f) {
                f.printStackTrace();
            }
        }
    }
    
    /**
     * Allows user to delete tour
     * 
     * @param e remove tour ActionEvent
     */
    void removeTourB_actionPerformed(ActionEvent e) {
        Driver driver;
        Tour tour = (Tour) tourTable.getModel().getValueAt(tourTable.getSelectedRow(), TourTable.TOUR);
        int result = JOptionPane.showConfirmDialog(null,  "Delete " + tour.getName() + 
                "?", "Delete Tour", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            
            
            
            //TODO Fix to remove tour without having to remove from driver schedule first
            if (Objects.nonNull(tour.getDriver())) {
                driver = tour.getDriver();
                int result2 = JOptionPane.showConfirmDialog(null,  "Must Remove " + tour.getName() + 
                        " From " + driver.getName() +" Schedule First " 
                        , "Delete Tour", JOptionPane.OK_CANCEL_OPTION);
                if (result2 == JOptionPane.OK_OPTION) {
                    parentPanel.selectPanel("DriverPanel");
                    return;
                } else {
                    return;
                }
                
                /*if (driver.getTours().size() > 1) {
                    System.out.println(driver.getTours().size());
                }
                
                
                driver.delTour(tour);
                if (Objects.isNull(driver.getTours())) {
                    //CurrentProject.getTourColl().del(tour.getID());
                    scheduleTable = new DriverScheduleTable();
                } else {
                    int result2 = JOptionPane.showConfirmDialog(null,  "Must Remove " + tour.getName() + 
                            "From " + driver.getName() +"Schedule First " 
                            , "Delete Tour", JOptionPane.OK_CANCEL_OPTION);
                }
                */  
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
    


    class PopupListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if ((e.getClickCount() == 2) && (tourTable.getSelectedRow() > -1))
                editTourB_actionPerformed(null);
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                tourPPMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }
    void ppEditEvent_actionPerformed(ActionEvent e) {
        editTourB_actionPerformed(e);
    }
    void ppRemoveEvent_actionPerformed(ActionEvent e) {
        removeTourB_actionPerformed(e);
    }
    void ppNewEvent_actionPerformed(ActionEvent e) {
        newTourB_actionPerformed(e);
    }
}