package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.History;
import main.java.memoranda.HistoryItem;
import main.java.memoranda.HistoryListener;
import main.java.memoranda.Project;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.date.DateListener;
import main.java.memoranda.util.Local;

/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

/*$Id: DailyItemsPanel.java,v 1.22 2005/02/13 03:06:10 rawsushi Exp $*/
public class DailyItemsPanel extends JPanel {
    private JSplitPane splitPane = new JSplitPane();
    private JPanel controlPanel = new JPanel(); /* Contains the calendar */
    private JPanel mainPanel = new JPanel();
    private JPanel statusPanel = new JPanel();
    private JPanel editorsPanel = new JPanel();
    private CardLayout cardLayout1 = new CardLayout();
    private JLabel currentDateLabel = new JLabel();
    private DriverPanel driverPanel = new DriverPanel(this);
    private BusPanel busPanel = new BusPanel(this);
    private TourPanel tourPanel = new TourPanel(this);
    private RouteMapPanel routeMapPanel = new RouteMapPanel(this);
    private ImageIcon expIcon = new ImageIcon(main.java.memoranda.ui.AppFrame.class
            .getResource("/ui/icons/exp_right.png"));
    private ImageIcon collIcon = new ImageIcon(main.java.memoranda.ui.AppFrame.class
            .getResource("/ui/icons/exp_left.png"));
    boolean expanded = true;
    
    private CalendarDate currentDate;

    private boolean calendarIgnoreChange = false;
    private boolean dateChangedByCalendar = false;
    private boolean changedByHistory = false;
    private JPanel cmainPanel = new JPanel();
    private JNCalendarPanel calendar = new JNCalendarPanel();
    private JToolBar toggleToolBar = new JToolBar();
    private BorderLayout borderLayout5 = new BorderLayout();
    private JButton toggleButton = new JButton();
    private WorkPanel parentPanel = null;

    private JPanel indicatorsPanel = new JPanel();
    private FlowLayout flowLayout1 = new FlowLayout();
    private JPanel mainTabsPanel = new JPanel();
    private CardLayout cardLayout2 = new CardLayout();

    private JTabbedPane tasksTabbedPane = new JTabbedPane();
    private JTabbedPane toursTabbedPane = new JTabbedPane();
    private JTabbedPane driverTabbedPane = new JTabbedPane();

    private String currentPanel;

    private Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);

    public DailyItemsPanel(WorkPanel parentPanel) {
        try {
            this.parentPanel = parentPanel;
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    public void jbInit() throws Exception {
        this.setLayout(new BorderLayout());
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBorder(null);
        splitPane.setDividerSize(2);
        controlPanel.setLayout(new BorderLayout());
        mainPanel.setLayout(new BorderLayout());
        editorsPanel.setLayout(cardLayout1);
        statusPanel.setBackground(Color.black);
        statusPanel.setForeground(Color.white);
        statusPanel.setMinimumSize(new Dimension(14, 24));
        statusPanel.setPreferredSize(new Dimension(14, 24));
        statusPanel.setLayout(new BorderLayout(4, 0));
        currentDateLabel.setFont(new java.awt.Font("Dialog", 0, 16));
        currentDateLabel.setForeground(Color.white);
        currentDateLabel.setText(CurrentDate.get().getFullDateString());
        controlPanel.setBackground(new Color(230, 230, 230));
        controlPanel.setBorder(BorderFactory.createEtchedBorder(Color.white, new Color(161, 161, 161)));
        controlPanel.setMinimumSize(new Dimension(20, 170));
        controlPanel.setPreferredSize(new Dimension(205, 170));

        calendar.setFont(new java.awt.Font("Dialog", 0, 11));
        calendar.setMinimumSize(new Dimension(0, 168));
        toggleToolBar.setBackground(new Color(215, 225, 250));
        toggleToolBar.setRequestFocusEnabled(false);
        toggleToolBar.setFloatable(false);
        cmainPanel.setLayout(borderLayout5);
        cmainPanel.setBackground(SystemColor.desktop);
        cmainPanel.setMinimumSize(new Dimension(0, 168));
        cmainPanel.setOpaque(false);
        toggleButton.setMaximumSize(new Dimension(32767, 32767));
        toggleButton.setMinimumSize(new Dimension(16, 16));
        toggleButton.setOpaque(false);
        toggleButton.setPreferredSize(new Dimension(16, 16));
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setMargin(new Insets(0, 0, 0, 0));
        toggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleButton_actionPerformed(e);
            }
        });
        toggleButton.setIcon(collIcon);
        indicatorsPanel.setOpaque(false);
        indicatorsPanel.setLayout(flowLayout1);

        flowLayout1.setAlignment(FlowLayout.RIGHT);
        flowLayout1.setVgap(0);


        mainTabsPanel.setLayout(cardLayout2);
        this.add(splitPane, BorderLayout.CENTER);

        controlPanel.add(cmainPanel, BorderLayout.CENTER);
        cmainPanel.add(calendar, BorderLayout.NORTH);

        mainPanel.add(statusPanel, BorderLayout.NORTH);
        statusPanel.add(currentDateLabel, BorderLayout.CENTER);
        statusPanel.add(indicatorsPanel, BorderLayout.EAST);

        mainPanel.add(editorsPanel, BorderLayout.CENTER);

        editorsPanel.add(driverPanel, "DRIVERS");
        editorsPanel.add(tourPanel, "TOURS");
        editorsPanel.add(busPanel, "BUSES");
        editorsPanel.add(routeMapPanel, "MAP");

        splitPane.add(mainPanel, JSplitPane.RIGHT);
        splitPane.add(controlPanel, JSplitPane.LEFT);
        controlPanel.add(toggleToolBar, BorderLayout.SOUTH);
        toggleToolBar.add(toggleButton, null);

        splitPane.setDividerLocation((int) controlPanel.getPreferredSize().getWidth());

        CurrentDate.addDateListener(new DateListener() {
            public void dateChange(CalendarDate d) {
                currentDateChanged(d);
            }
        });

        calendar.addSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (calendarIgnoreChange) {
                    return;
                }
                dateChangedByCalendar = true;
                CurrentDate.set(calendar.get());
                dateChangedByCalendar = false;
            }
        });

        History.addHistoryListener(new HistoryListener() {
            public void historyWasRolledTo(HistoryItem hi) {
                historyChanged(hi);
            }
        });

        currentDate = CurrentDate.get();

        History.add(new HistoryItem(CurrentDate.get(), CurrentProject.get()));
        cmainPanel.add(mainTabsPanel, BorderLayout.CENTER);
        mainTabsPanel.add(toursTabbedPane, "TOURSTAB");
        mainTabsPanel.add(tasksTabbedPane, "TASKSTAB");
        mainTabsPanel.add(driverTabbedPane, "DRIVERSTAB");
        mainPanel.setBorder(null);
    }

    private void currentDateChanged(CalendarDate newdate) {        
        if (!changedByHistory) {
            History.add(new HistoryItem(newdate, CurrentProject.get()));
        }
        
        if (!dateChangedByCalendar) {
            calendarIgnoreChange = true;
            calendar.set(newdate);
            calendarIgnoreChange = false;
        }

        currentDate = CurrentDate.get();


        currentDateLabel.setText(newdate.getFullDateString());
        currentDateLabel.setIcon(null);
        
        Cursor cur = App.getFrame().getCursor();
        App.getFrame().setCursor(waitCursor);

        App.getFrame().setCursor(cur);
    }

    private void currentProjectChanged(Project newprj) {
        if (!changedByHistory) {
            History.add(new HistoryItem(CurrentDate.get(), newprj));
        }

        Cursor cur = App.getFrame().getCursor();
        App.getFrame().setCursor(waitCursor);
        CurrentProject.save(); 
        
        App.getFrame().setCursor(cur);
    }

    private void historyChanged(HistoryItem hi) {
        changedByHistory = true;
        CurrentProject.set(hi.getProject());
        CurrentDate.set(hi.getDate());
        changedByHistory = false;
    }

    private void toggleButton_actionPerformed(ActionEvent e) {
        if (expanded) {
            expanded = false;
            toggleButton.setIcon(expIcon);
            controlPanel.remove(toggleToolBar);
            controlPanel.add(toggleToolBar, BorderLayout.EAST);
            splitPane.setDividerLocation((int) controlPanel.getMinimumSize().getWidth());

        } else {
            expanded = true;
            toggleButton.setIcon(collIcon);
            controlPanel.remove(toggleToolBar);
            controlPanel.add(toggleToolBar, BorderLayout.SOUTH);
            splitPane.setDividerLocation((int) controlPanel.getPreferredSize().getWidth());
        }
    }

    /**
     * Selects a panel.
     * 
     * @param pan The name fo the panel to select
     */
    public void selectPanel(String pan) {
        cardLayout1.show(editorsPanel, pan);
        cardLayout2.show(mainTabsPanel, pan + "TAB");
        calendar.jnCalendar.updateUI();
        currentPanel = pan;
    }

    /**
     * Gets the name of the currently selected panel.
     * 
     * @return Selected panel's name
     */
    public String getCurrentPanel() {
        return currentPanel;
    }
    
    /**
     * Gets the calendar.
     * 
     * @return THe calendar
     */
    public JNCalendarPanel getCalendar() {
        return calendar;
    }

    /**
     * Gets the DriverScheduleTable used to display a Driver's schedule.
     *
     * @return The DriverScheduleTable
     */
    public DriverScheduleTable getDriverScheduleTable() {
        return driverPanel.getDriverScheduleTable();
    }
    
    /**
     * Gets the BusScheduleTable used to display a Bus's schedule.
     *
     * @return The BusScheduleTable
     */
    public BusScheduleTable getBusScheduleTable() {
        return busPanel.getBusScheduleTable();
    }

    void taskB_actionPerformed(ActionEvent e) {
        parentPanel.busB_actionPerformed(null);
    }

    void alarmB_actionPerformed(ActionEvent e) {
        parentPanel.toursB_actionPerformed(null);
    }

    /**
     * Refreshes the Panels when the project is changed.
     */
    public void refresh() {
        driverPanel.refresh();
        busPanel.refresh();
        tourPanel.refresh();
    }
}