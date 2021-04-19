package main.java.memoranda.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Util;

/**
 * A JPanel that provides the interface for a user to add, edit, and delete drivers from the system, as well as schedule tours for a driver
 *
 * @author Derek Argall
 * @version 04/05/2020
 */
public class DriverPanel extends JSplitPane {
    private DriverTable driverTable;
    private DriverScheduleTable scheduleTable;
    private DailyItemsPanel parentPanel;
    private DriverColl drivers;
    private String gotoTask;
    private boolean isActive;
    private final Dimension VERTICAL_GAP = new Dimension(0, 5);
    private static final int LABEL_SIZE = 25;

    /**
     * Constructor for the DriverPanel
     * <p>
     * Creates a JPanel which houses the the information about the Driver Schedule
     *
     * @param parentPanel The DailyItemsPanel which will house this panel
     */
    public DriverPanel(DailyItemsPanel parentPanel) {
        super(JSplitPane.HORIZONTAL_SPLIT);

        try {
            this.parentPanel = parentPanel;
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        drivers = CurrentProject.getDriverColl();

        setActive(true);

        setDividerSize(5);
        setResizeWeight(0.4);
        setOneTouchExpandable(false);
        setEnabled(false);

        setLeftComponent(getDriverPanel());
        setRightComponent(getSchedulePanel());
    }

    private JPanel getDriverPanel() {
        driverTable = new DriverTable();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(JPanel.TOP_ALIGNMENT);

        panel.setMaximumSize(new Dimension());

        JLabel label = new JLabel("Drivers");
        label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, LABEL_SIZE));
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        JButton add = new JButton("Add Driver");
        add.setAlignmentX(JButton.LEFT_ALIGNMENT);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DriverDialog dlg = new DriverDialog(App.getFrame(), "Add Driver", "Add");
                Dimension frmSize = App.getFrame().getSize();
                Point loc = App.getFrame().getLocation();

                dlg.setLocation(
                        (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                        (frmSize.height - dlg.getSize().height) / 2
                                + loc.y);
                dlg.setVisible(true);

                if (!dlg.isCancelled()) {
                    Driver driver = CurrentProject.getDriverColl().newItem();
                    driver.setName(dlg.getName());
                    driver.setPhoneNumber(dlg.getPhone());

                    try {
                        CurrentProject.getDriverColl().add(driver);
                        CurrentStorage.get().storeDriverList(CurrentProject.get(), CurrentProject.getDriverColl());
                        driverTable.tableChanged();
                        scheduleTable.setDriver(driverTable.getDriver());
                        scheduleTable.tableChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(driverTable);
        scroll.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(VERTICAL_GAP));
        panel.add(add);
        panel.add(Box.createRigidArea(VERTICAL_GAP));
        panel.add(scroll);

        return panel;
    }

    private JPanel getSchedulePanel() {
        if (drivers.size() >= 1) {
            scheduleTable = new DriverScheduleTable(driverTable.getDriver());
        } else {
            scheduleTable = new DriverScheduleTable();
        }

        driverTable.setScheduleTable(scheduleTable);

        JButton schedule = new JButton("Schedule Tour");
        schedule.setAlignmentX(JButton.LEFT_ALIGNMENT);
        schedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (scheduleTable.getDriver() == null) {
                    JOptionPane.showMessageDialog(null, "Cannot Schedule Tour: No Driver Selected", "Error", JOptionPane.OK_OPTION, new ImageIcon(main.java.memoranda.ui.ExceptionDialog.class.getResource(
                            "/ui/icons/error.png")));
                } else {
                    DriverTourDialog dlg = new DriverTourDialog(App.getFrame(), scheduleTable.getDriver().getName());
                    Dimension frmSize = App.getFrame().getSize();
                    Point loc = App.getFrame().getLocation();

                    dlg.setLocation(
                            (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                            (frmSize.height - dlg.getSize().height) / 2
                                    + loc.y);
                    dlg.setVisible(true);

                    if (!dlg.isCancelled() && dlg.getTour() != null) {
                        try {
                            scheduleTable.getDriver().addTour(dlg.getTour());
                            scheduleTable.addTour(dlg.getTour());
                            CurrentStorage.get().storeDriverList(CurrentProject.get(), CurrentProject.getDriverColl());
                            CurrentStorage.get().storeTourList(CurrentProject.get(), CurrentProject.getTourColl());
                            scheduleTable.tableChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(JPanel.TOP_ALIGNMENT);

        JLabel label = new JLabel("Schedule");
        label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, LABEL_SIZE));
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(scheduleTable);
        scroll.setAlignmentX(JButton.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(VERTICAL_GAP));
        panel.add(schedule);
        panel.add(Box.createRigidArea(VERTICAL_GAP));
        panel.add(scroll);

        return panel;
    }

    /**
     * Refreshes this panel
     */
    public void refresh() {
        driverTable.tableChanged();
        scheduleTable.tableChanged();
    }

    /**
     * Flags this panel as the active panel
     *
     * @param isa
     */
    public void setActive(boolean isa) {
        isActive = isa;
    }

    /**
     * Gets the DriverScheduleTable used to display a Driver's schedule
     *
     * @return The DriverScheduleTable
     */
    public DriverScheduleTable getDriverScheduleTable() {
        return scheduleTable;
    }
}
