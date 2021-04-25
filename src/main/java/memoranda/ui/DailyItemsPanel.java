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
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.History;
import main.java.memoranda.HistoryItem;
import main.java.memoranda.HistoryListener;
import main.java.memoranda.Project;
import main.java.memoranda.ProjectListener;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.date.DateListener;
import main.java.memoranda.util.Local;

/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

/*$Id: DailyItemsPanel.java,v 1.22 2005/02/13 03:06:10 rawsushi Exp $*/
public class DailyItemsPanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JSplitPane splitPane = new JSplitPane();
    JPanel controlPanel = new JPanel(); /* Contains the calendar */
    JPanel mainPanel = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    JPanel statusPanel = new JPanel();
    BorderLayout borderLayout3 = new BorderLayout();
    JPanel editorsPanel = new JPanel();
    CardLayout cardLayout1 = new CardLayout();
    JLabel currentDateLabel = new JLabel();
    BorderLayout borderLayout4 = new BorderLayout();
    private DriverPanel driverPanel = new DriverPanel(this);
    private BusPanel busPanel = new BusPanel(this);
    private TourPanel tourPanel = new TourPanel(this);
    private RouteMapPanel routeMapPanel = new RouteMapPanel(this);
    ImageIcon expIcon = new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/exp_right.png"));
    ImageIcon collIcon = new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/exp_left.png"));
    ImageIcon bookmarkIcon = new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/star8.png"));
    boolean expanded = true;

    CalendarDate currentDate;

    boolean calendarIgnoreChange = false;
    boolean dateChangedByCalendar = false;
    boolean changedByHistory = false;
    JPanel cmainPanel = new JPanel();
    JNCalendarPanel calendar = new JNCalendarPanel();
    JToolBar toggleToolBar = new JToolBar();
    BorderLayout borderLayout5 = new BorderLayout();
    Border border1;
    JButton toggleButton = new JButton();
    WorkPanel parentPanel = null;

    boolean addedToHistory = false;
    JPanel indicatorsPanel = new JPanel();
    JButton alarmB = new JButton();
    FlowLayout flowLayout1 = new FlowLayout();
    JButton taskB = new JButton();
    JPanel mainTabsPanel = new JPanel();
    CardLayout cardLayout2 = new CardLayout();

    JTabbedPane tasksTabbedPane = new JTabbedPane();
    JTabbedPane toursTabbedPane = new JTabbedPane();
    JTabbedPane driverTabbedPane = new JTabbedPane();
    Border border2;

    String CurrentPanel;

    Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);

    public DailyItemsPanel(WorkPanel _parentPanel) {
        try {
            parentPanel = _parentPanel;
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    void jbInit() throws Exception {
        border1 = BorderFactory.createEtchedBorder(Color.white, Color.gray);
        border2 = BorderFactory.createEtchedBorder(Color.white, new Color(161, 161, 161));
        this.setLayout(borderLayout1);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBorder(null);
        splitPane.setDividerSize(2);
        controlPanel.setLayout(borderLayout2);
        //calendar.setMinimumSize(new Dimension(200, 170));
        mainPanel.setLayout(borderLayout3);
        editorsPanel.setLayout(cardLayout1);
        statusPanel.setBackground(Color.black);
        statusPanel.setForeground(Color.white);
        statusPanel.setMinimumSize(new Dimension(14, 24));
        statusPanel.setPreferredSize(new Dimension(14, 24));
        statusPanel.setLayout(borderLayout4);
        currentDateLabel.setFont(new java.awt.Font("Dialog", 0, 16));
        currentDateLabel.setForeground(Color.white);
        currentDateLabel.setText(CurrentDate.get().getFullDateString());
        borderLayout4.setHgap(4);
        controlPanel.setBackground(new Color(230, 230, 230));
        controlPanel.setBorder(border2);
        controlPanel.setMinimumSize(new Dimension(20, 170));
        controlPanel.setPreferredSize(new Dimension(205, 170));
        //controlPanel.setMaximumSize(new Dimension(206, 170));
        //controlPanel.setSize(controlPanel.getMaximumSize());
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
        alarmB.setMaximumSize(new Dimension(24, 24));
        alarmB.setOpaque(false);
        alarmB.setPreferredSize(new Dimension(24, 24));
        alarmB.setToolTipText(Local.getString("Active events"));
        alarmB.setBorderPainted(false);
        alarmB.setMargin(new Insets(0, 0, 0, 0));
        alarmB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alarmB_actionPerformed(e);
            }
        });
        alarmB.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/alarm.png")));
        flowLayout1.setAlignment(FlowLayout.RIGHT);
        flowLayout1.setVgap(0);
        taskB.setMargin(new Insets(0, 0, 0, 0));
        taskB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskB_actionPerformed(e);
            }
        });
        taskB.setPreferredSize(new Dimension(24, 24));
        taskB.setToolTipText(Local.getString("Active to-do tasks"));
        taskB.setBorderPainted(false);
        taskB.setMaximumSize(new Dimension(24, 24));
        taskB.setOpaque(false);
        taskB.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/task.png")));

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

    void currentDateChanged(CalendarDate newdate) {
        Cursor cur = App.getFrame().getCursor();
        App.getFrame().setCursor(waitCursor);
        
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

        App.getFrame().setCursor(cur);
    }

    void currentProjectChanged(Project newprj) {
//        Util.debug("currentProjectChanged");

        Cursor cur = App.getFrame().getCursor();
        App.getFrame().setCursor(waitCursor);
        if (!changedByHistory) {
            History.add(new HistoryItem(CurrentDate.get(), newprj));
        }

        CurrentProject.save();        

        App.getFrame().setCursor(cur);
    }

    void historyChanged(HistoryItem hi) {
        changedByHistory = true;
        CurrentProject.set(hi.getProject());
        CurrentDate.set(hi.getDate());
        changedByHistory = false;
    }

    void toggleButton_actionPerformed(ActionEvent e) {
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

    public void selectPanel(String pan) {
        cardLayout1.show(editorsPanel, pan);
        cardLayout2.show(mainTabsPanel, pan + "TAB");
        calendar.jnCalendar.updateUI();
        CurrentPanel = pan;
    }

    public String getCurrentPanel() {
        return CurrentPanel;
    }

    /**
     * Gets the DriverScheduleTable used to display a Driver's schedule
     *
     * @return The DriverScheduleTable
     */
    public DriverScheduleTable getDriverScheduleTable() {
        return driverPanel.getDriverScheduleTable();
    }
    
    /**
     * Gets the BusScheduleTable used to display a Bus's schedule
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
     * Refreshes the Panels when the project is changed
     */
    public void refresh() {
        driverPanel.refresh();
        busPanel.refresh();
        tourPanel.refresh();
    }
}