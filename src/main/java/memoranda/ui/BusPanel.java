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

import main.java.memoranda.Bus;
import main.java.memoranda.BusColl;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.util.CurrentStorage;

/**
 * A JPanel that provides the interface for a user to add, edit, and delete buses from the system,
 * as well as schedule tours for a bus.
 *
 * @author Derek Argall
 * @version 04/09/2020
 */
public class BusPanel extends JSplitPane {
    private BusTable busTable;
    private BusScheduleTable scheduleTable;
    private DailyItemsPanel parentPanel;
    private BusColl buses;
    private String gotoTask;
    private static final Dimension VERTICAL_GAP = new Dimension(0, 5);
    private static final int LABEL_SIZE = 25;

    /**
     * Constructor for the BusPanel.
     *
     * @param parentPanel The DailyItemsPanel which will house this panel
     */
    public BusPanel(DailyItemsPanel parentPanel) {
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
        buses = CurrentProject.getBusColl();

        setDividerSize(5);
        setResizeWeight(0.4);
        setOneTouchExpandable(false);
        setEnabled(false);

        setLeftComponent(getBusPanel());
        setRightComponent(getSchedulePanel());
    }

    private JPanel getBusPanel() {
        busTable = new BusTable(scheduleTable, parentPanel.getDriverScheduleTable());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(JPanel.TOP_ALIGNMENT);

        panel.setMaximumSize(new Dimension());

        JLabel label = new JLabel("Buses");
        label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, LABEL_SIZE));
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        JButton add = new JButton("Add Bus");
        add.setAlignmentX(JButton.LEFT_ALIGNMENT);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                BusDialog dlg = new BusDialog(App.getFrame(), "Add Bus", "Add");
                Dimension frmSize = App.getFrame().getSize();
                Point loc = App.getFrame().getLocation();

                dlg.setLocation(
                        (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                        (frmSize.height - dlg.getSize().height) / 2
                                + loc.y);
                dlg.setVisible(true);

                if (!dlg.isCancelled()) {
                    Bus bus = CurrentProject.getBusColl().newItem();
                    bus.setNumber(dlg.getNumber());

                    try {
                        CurrentProject.getBusColl().add(bus);
                        CurrentStorage.get().storeBusList(CurrentProject.get(), CurrentProject.getBusColl());
                        busTable.tableChanged();
                        scheduleTable.setBus(busTable.getBus());
                        scheduleTable.tableChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(busTable);
        scroll.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(VERTICAL_GAP));
        panel.add(add);
        panel.add(Box.createRigidArea(VERTICAL_GAP));
        panel.add(scroll);

        return panel;
    }

    private JPanel getSchedulePanel() {
        if (buses.size() >= 1) {
            scheduleTable = new BusScheduleTable(busTable.getBus(), parentPanel.getDriverScheduleTable());
        } else {
            scheduleTable = new BusScheduleTable(parentPanel.getDriverScheduleTable());
        }

        busTable.setBusScheduleTable(scheduleTable);

        JButton schedule = new JButton("Schedule Tour");
        schedule.setAlignmentX(JButton.LEFT_ALIGNMENT);
        schedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (scheduleTable.getBus() == null) {
                    JOptionPane.showMessageDialog(null, "Cannot Schedule Tour: No Bus Selected",
                            "Error", JOptionPane.OK_OPTION,
                            new ImageIcon(main.java.memoranda.ui.ExceptionDialog.class.getResource(
                            "/ui/icons/error.png")));
                } else {
                    BusTourDialog dlg = new BusTourDialog(App.getFrame(), scheduleTable.getBus().getNumber());
                    Dimension frmSize = App.getFrame().getSize();
                    Point loc = App.getFrame().getLocation();

                    dlg.setLocation(
                            (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                            (frmSize.height - dlg.getSize().height) / 2
                                    + loc.y);
                    dlg.setVisible(true);

                    if (!dlg.isCancelled() && dlg.getTour() != null) {
                        try {
                            scheduleTable.getBus().addTour(dlg.getTour());
                            scheduleTable.addTour(dlg.getTour());
                            CurrentStorage.get().storeBusList(CurrentProject.get(), CurrentProject.getBusColl());
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
     * Refreshes the Panel.
     */
    public void refresh() {
        busTable.tableChanged();
        scheduleTable.tableChanged();
    }

    /**
     * Gets the BusScheduleTable used to display a Bus's schedule.
     *
     * @return The BusScheduleTable
     */
    public BusScheduleTable getBusScheduleTable() {
        return scheduleTable;
    }
}
