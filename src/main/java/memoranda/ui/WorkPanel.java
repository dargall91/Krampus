package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import main.java.memoranda.util.Context;
import main.java.memoranda.util.Local;

/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

/*$Id: WorkPanel.java,v 1.9 2004/04/05 10:05:44 alexeya Exp $*/
/**
 * A WorkPanel is the panel that contains the buttons for the different tabs of the Bus Schedule
 * System (Drivers, Buses, the Map, and Tours).
 *
 */
public class WorkPanel extends JPanel {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JToolBar toolBar = new JToolBar();
    private JPanel panel = new JPanel();
    private CardLayout cardLayout1 = new CardLayout();

    private DailyItemsPanel dailyItemsPanel = new DailyItemsPanel(this);
    private JButton driverB = new JButton();
    private JButton busesB = new JButton();
    private JButton toursB = new JButton();
    private JButton mapB = new JButton();
    private JButton currentB = null;
    private Border border1;

    /**
     * Creates a new WorkPanel.
     */
    public WorkPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    private void jbInit() throws Exception {
        border1 =
                BorderFactory.createCompoundBorder(
                        BorderFactory.createBevelBorder(
                                BevelBorder.LOWERED,
                                Color.white,
                                Color.white,
                                new Color(124, 124, 124),
                                new Color(178, 178, 178)),
                        BorderFactory.createEmptyBorder(0, 2, 0, 0));

        this.setLayout(borderLayout1);
        toolBar.setOrientation(JToolBar.VERTICAL);
        toolBar.setBackground(Color.white);

        toolBar.setBorderPainted(false);
        toolBar.setFloatable(false);
        panel.setLayout(cardLayout1);

        driverB.setBackground(Color.white);
        driverB.setMaximumSize(new Dimension(60, 80));
        driverB.setMinimumSize(new Dimension(30, 30));

        driverB.setFont(new java.awt.Font("Dialog", 1, 10));
        driverB.setPreferredSize(new Dimension(50, 50));
        driverB.setBorderPainted(false);
        driverB.setContentAreaFilled(false);
        driverB.setFocusPainted(false);
        driverB.setHorizontalTextPosition(SwingConstants.CENTER);
        driverB.setText(Local.getString("Drivers"));
        driverB.setVerticalAlignment(SwingConstants.TOP);
        driverB.setVerticalTextPosition(SwingConstants.BOTTOM);
        driverB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                driverB_actionPerformed(e);
            }
        });
        driverB.setIcon(
                new ImageIcon(
                        main.java.memoranda.ui.AppFrame.class.getResource(
                                "/ui/icons/driver_icon.png")));
        driverB.setOpaque(false);
        driverB.setMargin(new Insets(0, 0, 0, 0));
        driverB.setSelected(true);

        toursB.setBackground(Color.white);
        toursB.setMaximumSize(new Dimension(60, 80));
        toursB.setMinimumSize(new Dimension(30, 30));

        toursB.setFont(new java.awt.Font("Dialog", 1, 10));
        toursB.setPreferredSize(new Dimension(50, 50));
        toursB.setBorderPainted(false);
        toursB.setContentAreaFilled(false);
        toursB.setFocusPainted(false);
        toursB.setHorizontalTextPosition(SwingConstants.CENTER);
        toursB.setText(Local.getString("Tours"));
        toursB.setVerticalAlignment(SwingConstants.TOP);
        toursB.setVerticalTextPosition(SwingConstants.BOTTOM);
        toursB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toursB_actionPerformed(e);
            }
        });
        
        toursB.setIcon(
                new ImageIcon(
                        main.java.memoranda.ui.AppFrame.class.getResource(
                                "/ui/icons/tour_icon.png")));
        toursB.setOpaque(false);
        toursB.setMargin(new Insets(0, 0, 0, 0));

        busesB.setSelected(true);
        busesB.setFont(new java.awt.Font("Dialog", 1, 10));
        busesB.setMargin(new Insets(0, 0, 0, 0));
        busesB.setIcon(
                new ImageIcon(
                        main.java.memoranda.ui.AppFrame.class.getResource(
                                "/ui/icons/bus_icon.png")));
        busesB.setVerticalTextPosition(SwingConstants.BOTTOM);
        busesB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busB_actionPerformed(e);
            }
        });
        
        busesB.setVerticalAlignment(SwingConstants.TOP);
        busesB.setText(Local.getString("Buses"));
        busesB.setHorizontalTextPosition(SwingConstants.CENTER);
        busesB.setFocusPainted(false);
        busesB.setBorderPainted(false);
        busesB.setContentAreaFilled(false);
        busesB.setPreferredSize(new Dimension(50, 50));
        busesB.setMinimumSize(new Dimension(30, 30));
        busesB.setOpaque(false);
        busesB.setMaximumSize(new Dimension(60, 80));
        busesB.setBackground(Color.white);
        this.setPreferredSize(new Dimension(1073, 300));

        mapB.setSelected(true);
        mapB.setMargin(new Insets(0, 0, 0, 0));
        mapB.setIcon(
                new ImageIcon(
                        main.java.memoranda.ui.AppFrame.class.getResource(
                                "/ui/icons/map_icon.png")));
        mapB.setVerticalTextPosition(SwingConstants.BOTTOM);
        mapB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mapB_actionPerformed(e);
            }
        });
        
        mapB.setFont(new java.awt.Font("Dialog", 1, 10));
        mapB.setVerticalAlignment(SwingConstants.TOP);
        mapB.setText(Local.getString("Map"));
        mapB.setHorizontalTextPosition(SwingConstants.CENTER);
        mapB.setFocusPainted(false);
        mapB.setBorderPainted(false);
        mapB.setContentAreaFilled(false);
        mapB.setPreferredSize(new Dimension(50, 50));
        mapB.setMinimumSize(new Dimension(30, 30));
        mapB.setOpaque(false);
        mapB.setMaximumSize(new Dimension(60, 80));
        mapB.setBackground(Color.white);
        this.add(toolBar, BorderLayout.WEST);
        this.add(panel, BorderLayout.CENTER);
        panel.add(dailyItemsPanel, "DAILYITEMS");
        toolBar.add(driverB, null);
        toolBar.add(toursB, null);
        toolBar.add(busesB, null);
        toolBar.add(mapB, null);
        currentB = driverB;
        // Default blue color
        currentB.setBackground(new Color(215, 225, 250));
        currentB.setOpaque(true);

        toolBar.setBorder(null);
        panel.setBorder(null);
        dailyItemsPanel.setBorder(null);

    }

    public void selectPanel(String pan) {
        if (pan != null) {
            if (pan.equals("BUSES")) {
                busB_actionPerformed(null);
            } else if (pan.equals("EVENTS")) {
                toursB_actionPerformed(null);
            } else if (pan.equals("MAP")) {
                mapB_actionPerformed(null);
            }
        }
    }

    public void driverB_actionPerformed(ActionEvent e) {
        cardLayout1.show(panel, "DAILYITEMS");
        dailyItemsPanel.selectPanel("DRIVERS");
        setCurrentButton(driverB);
        Context.put("CURRENT_PANEL", "DRIVERS");
    }

    public void busB_actionPerformed(ActionEvent e) {
        cardLayout1.show(panel, "DAILYITEMS");
        dailyItemsPanel.selectPanel("BUSES");
        setCurrentButton(busesB);
        Context.put("CURRENT_PANEL", "BUSES");
    }

    public void toursB_actionPerformed(ActionEvent e) {
        cardLayout1.show(panel, "DAILYITEMS");
        dailyItemsPanel.selectPanel("TOURS");
        setCurrentButton(toursB);
        Context.put("CURRENT_PANEL", "TOURS");
    }

    public void mapB_actionPerformed(ActionEvent e) {
        cardLayout1.show(panel, "MAP");
        dailyItemsPanel.selectPanel("MAP");
        setCurrentButton(mapB);
        Context.put("CURRENT_PANEL", "MAP");
    }

    void setCurrentButton(JButton cb) {
        currentB.setBackground(Color.white);
        currentB.setOpaque(false);
        currentB = cb;
        // Default color blue
        currentB.setBackground(new Color(215, 225, 250));
        currentB.setOpaque(true);
    }

    /**
     * Gets the DailyItemsPanel.
     * 
     * @return the DailyItemsPanel
     */
    public DailyItemsPanel getDailyItemsPanel() {
        return dailyItemsPanel;
    }
    
    /**
     * Gets this panel's Map Button.
     * 
     * @return The Map button
     */
    public JButton getMapButton() {
        return mapB;
    }
    
    /**
     * Gets this panel's Driver Button.
     * 
     * @return The Driver button
     */
    public JButton getDriverButton() {
        return driverB;
    }
    
    /**
     * Gets this panel's Bus Button.
     * 
     * @return The Bus button
     */
    public JButton getBusButton() {
        return busesB;
    }
    
    /**
     * Gets this panel's Tour Button.
     * 
     * @return The Tour button
     */
    public JButton getTourButton() {
        return toursB;
    }
}