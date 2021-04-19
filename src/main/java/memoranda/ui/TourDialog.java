package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import main.java.memoranda.Bus;
import main.java.memoranda.BusColl;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.Route;
import main.java.memoranda.RouteColl;
import main.java.memoranda.util.Local;


/**
 * JDialog for user to add, edit or remove a tour
 *
 * @author John Thurstonson
 * @version 04/10/2021
 * <p>
 * References:
 * Used EventDialog.java as base, v 1.28 2005/02/19 10:06:25 rawsushi Exp
 * Referenced DriverTourDialog.java, Author: Derek Argall, Version: 04/05/2021
 */
public class TourDialog extends JDialog {
    private boolean cancelled = true;;
    private JPanel topPanel = new JPanel(new BorderLayout());
    private JPanel bottomPanel = new JPanel(new BorderLayout());
    private JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel header = new JLabel();
    private JPanel tourPanel = new JPanel(new GridBagLayout());
    private GridBagConstraints gbc;
    private JLabel lblTime = new JLabel();
    private JSpinner timeSpin = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
    private JLabel lblText = new JLabel();
    private JTextField nameField = new JTextField();
    private JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    private JButton okB = new JButton();
    private JButton cancelB = new JButton();
    private Date tourDate;
    private RouteColl routes;
    private BusColl buses;
    private JComboBox<Route> routeCB = new JComboBox<Route>();
    private JComboBox<String > busCB = new JComboBox<String>();
    private int error = 0;
    private final int ERROR_VALUE = 1;

    /**
     * TourDialog constructor
     *
     * @param frame frame to display with
     * @param title dialog title
     */
    public TourDialog(Frame frame, String title) {
        super(frame, title, true);
        try {
            error = jbInit();
            pack();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }

    }

    private int jbInit() throws Exception {
        routes = CurrentProject.getRouteColl();
        if (routes.size() < 1) {
            int result = JOptionPane.showConfirmDialog(null, "No Routes In System", "Need Route", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                cancelled = true;
                return ERROR_VALUE;
            } else {
                cancelled = true;
                return ERROR_VALUE;
            }
        }
        
        Iterator<Route> routeIter = routes.iterator();
        
        while (routeIter.hasNext()) {
            routeCB.addItem(routeIter.next());
        }
        
        routeCB.setRenderer(new RouteListCellRenderer());
        buses = CurrentProject.getBusColl();
        
        if (buses.size() < 1) {
            int result = JOptionPane.showConfirmDialog(null, "No Buses In System", "Need Bus", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                cancelled = true;
                return ERROR_VALUE;
            } else {
                cancelled = true;
                return ERROR_VALUE;
            }
        }
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        Iterator<Bus> busIter = buses.iterator();
        
        while (busIter.hasNext()) {
            busCB.addItem("No. " + busIter.next().getNumber());
        }


        this.setResizable(false);
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        header.setForeground(new Color(0, 0, 124));
        header.setText("Tour");
        headerPanel.add(header);

        lblTime.setText(Local.getString("Time"));
        lblTime.setMinimumSize(new Dimension(60, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        tourPanel.add(lblTime, gbc);

        timeSpin.setPreferredSize(new Dimension(60, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        tourPanel.add(timeSpin, gbc);

        lblText.setText(Local.getString("Tour Name"));
        lblText.setMinimumSize(new Dimension(120, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        tourPanel.add(lblText, gbc);

        nameField.setMinimumSize(new Dimension(375, 24));
        nameField.setPreferredSize(new Dimension(375, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tourPanel.add(nameField, gbc);

        routeCB.setPreferredSize(new Dimension(100, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tourPanel.add(routeCB, gbc);

        busCB.setPreferredSize(new Dimension(100, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tourPanel.add(busCB, gbc);

        okB.setMaximumSize(new Dimension(100, 26));
        okB.setMinimumSize(new Dimension(100, 26));
        okB.setPreferredSize(new Dimension(100, 26));
        okB.setText(Local.getString("Ok"));
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        
        this.getRootPane().setDefaultButton(okB);
        cancelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        
        cancelB.setText(Local.getString("Cancel"));
        cancelB.setPreferredSize(new Dimension(100, 26));
        cancelB.setMinimumSize(new Dimension(100, 26));
        cancelB.setMaximumSize(new Dimension(100, 26));
        buttonsPanel.add(okB);
        buttonsPanel.add(cancelB);

        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(tourPanel, BorderLayout.SOUTH);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        ((JSpinner.DateEditor) timeSpin.getEditor()).getFormat().applyPattern("HH:mm");

        return 0;
    }


    /**
     * OK button pressed
     *
     * @param e ok button ActionEvent
     */
    private void okButton_actionPerformed(ActionEvent e) {
        if (getName().equals("")) {
            pack();
            validate();
        } else {
            cancelled = false;
            this.dispose();
        }
    }

    /**
     * Cancel button pressed
     *
     * @param e cancel button ActionEvent
     */
    private void cancelButton_actionPerformed(ActionEvent e) {
        cancelled = true;
        this.dispose();
    }

    /**
     * Return if cancelled or not
     *
     * @return true if cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }


    /**
     * Returns name
     *
     * @return name
     */
    public String getName() {
        return nameField.getText();
    }

    /**
     * Sets current name
     *
     * @param name tour name
     */
    public void setName(String name) {
        nameField.setText(name);
    }

    /**
     * Returns route
     *
     * @return route
     */
    public Route getRoute() {
        return (Route) routeCB.getSelectedItem();
    }

    /**
     * Sets current route
     *
     * @param route tour route
     */
    public void setRoute(Route route) {
        routeCB.setSelectedItem(route);
    }

    /**
     * Returns bus
     *
     * @return bus
     */
    public Bus getBus() {
        return buses.getBuses().toArray(new Bus[buses.size()])[busCB.getSelectedIndex()];
    }

    /**
     * Sets current bus
     *
     * @param bus tour bus
     */
    public void setBus(Bus bus) {
        busCB.setSelectedItem(bus);
    }

    /**
     * Returns time
     *
     * @return set time in HH:MM
     */
    public LocalTime getTime() {
        //https://stackoverflow.com/questions/63502597/how-to-set-the-value-of-a-jspinner-using-a-localtime-object
        //Time formatting
        Object obj = timeSpin.getValue();
        if (obj instanceof java.util.Date) {
            tourDate = (Date) obj;
            java.time.Instant inst = tourDate.toInstant();
            java.time.ZoneId zone = java.time.ZoneId.systemDefault();
            java.time.LocalTime time = java.time.LocalTime.ofInstant(inst, zone);
            int hour = time.getHour();
            int minute = time.getMinute();
            String timeString;

            if (hour < 10) {
                timeString = "0" + String.valueOf(hour) + ":";
            } else {
                timeString = String.valueOf(hour) + ":";
            }

            if (minute < 10) {
                timeString = timeString + "0" + String.valueOf(minute);
            } else {
                timeString = timeString + String.valueOf(minute);
            }

            DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("HH:mm");
            time = LocalTime.parse(timeString, timeParser);
            return time;
        }
        return null;
    }


    /**
     * Sets to current tour time
     *
     * @param time tour time
     */
    public void setTime(LocalTime time) {
        Instant inst = time.atDate(LocalDate.now()).
                atZone(ZoneId.systemDefault()).toInstant();
        Date dateTime = Date.from(inst);
        timeSpin.setValue(dateTime);
    }

    /**
     * Returns 1 for error and 0 for no error
     *
     * @return error
     */
    public int getError() {
        return error;
    }

}