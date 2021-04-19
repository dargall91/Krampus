package main.java.memoranda.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.java.memoranda.Bus;
import main.java.memoranda.BusColl;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.Tour;
import main.java.memoranda.TourColl;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;

/**
 * BusScheduleTable is a JTable that contains the data related to a Bus's Schedule (Tour Name, bus
 * ID, tour ID, date, time).
 *
 * @author Derek Argall
 * @version 04/09/2020
 */
public class BusScheduleTable extends JTable {
    private ArrayList<Tour> tours;
    private Bus bus;
    private TourColl tourColl;
    private BusColl busColl;
    private TableRowSorter<TableModel> sorter;
    private static final int HEIGHT = 24;
    private DriverScheduleTable driverTable;

    /**
     * Constructor for BusScheduleTable. Sets a default bus.
     *
     * @param bus The default bus who's schedule will be displayed
     */
    public BusScheduleTable(Bus bus, DriverScheduleTable driverTable) {
        super();
        this.driverTable = driverTable;
        setBus(bus);
        init();
    }

    /**
     * Constructor for BusScheduleTable to be used when BusColl is empty.
     */
    public BusScheduleTable(DriverScheduleTable driverTable) {
        super();
        this.driverTable = driverTable;
        setBus(null);
        init();
    }

    private void init() {
        setToolTipText("Click to select a tour. Right-click for options.");

        busColl = CurrentProject.getBusColl();
        tourColl = CurrentProject.getTourColl();

        JPopupMenu optionsMenu = new JPopupMenu();
        optionsMenu.setFont(new Font("Dialog", 1, 10));

        JMenuItem removeTour = new JMenuItem("Remove Tour");
        removeTour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeActionEvent(e);
            }
        });

        optionsMenu.add(removeTour);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    optionsMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        setRowHeight(HEIGHT);
        setShowGrid(false);

        ScheduleTableModel model = new ScheduleTableModel();
        setModel(model);

        sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (tours.size() > 0) {
            setRowSelectionInterval(0, 0);
        }

        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        initColumnsWidth();
    }

    private void initColumnsWidth() {
        for (int i = 0; i < getColumnCount(); i++) {
            TableColumn tableColumn = getColumnModel().getColumn(i);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int j = 0; j < getRowCount(); j++) {
                TableCellRenderer cellRenderer = getCellRenderer(j, i);
                Component comp = prepareRenderer(cellRenderer, j, i);
                int width = comp.getPreferredSize().width + getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setMinWidth(preferredWidth);
        }
    }

    /**
     * Repaints the table to reflect any changes to the data.
     */
    public void tableChanged() {
        setBus(getBus());
        init();
        updateUI();
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return new javax.swing.table.DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (((row % 2) > 0) && (!isSelected)) {
                    comp.setBackground(new Color(230, 240, 255));
                }

                return comp;
            }
        };
    }

    private class ScheduleTableModel extends AbstractTableModel {
        private String[] columnNames = {
                Local.getString("Name"),
                Local.getString("Tour ID"),
                //Local.getString("Date"),
                Local.getString("Time")};

        @Override
        public String getColumnName(int i) {
            return columnNames[i];
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            if (tours == null) {
                return 0;
            }

            return tours.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            Tour tour = bus.getTours().toArray(new Tour[tours.size()])[row];

            if (col == 0) {
                return tour.getName();
            }

            if (col == 1) {
                return tour.getID();
            }

            if (col == 2) {
                return tour.getTime();
            }

            return null;
        }

        @Override
        public Class getColumnClass(int col) {
            for (int i = 0; i < getRowCount(); i++) {
                Object obj = getValueAt(i, col);
                {
                    if (obj != null) {
                        return obj.getClass();
                    }
                }
            }

            //default to String
            return String.class;
        }
    }

    private void removeActionEvent(ActionEvent e) {
        Tour tour = getTour();
        int result = JOptionPane.showConfirmDialog(null, "Remove Tour from " + bus.getNumber()
                + "'s Schedule?", "Delete Tour", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            //A driver cannot be scheduled for a tour without a bus. Check if the tour has a driver, then unschedule
            //the tour from the driver if there is
            if (tour.getDriver() != null) {
                tour.getDriver().delTour(tour);
                driverTable.tableChanged();
            }

            bus.delTour(tour);

            try {
                CurrentStorage.get().storeBusList(CurrentProject.get(), busColl);
                CurrentStorage.get().storeTourList(CurrentProject.get(), tourColl);
                setBus(bus);
                tableChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Sets the bus who's schedule is to be displayed.
     *
     * @param bus The bus to display the schedule for
     */
    public void setBus(Bus bus) {
        this.bus = bus;

        if (bus == null) {
            tours = new ArrayList<Tour>();
        } else {
            tours = new ArrayList<Tour>(bus.getTours());
        }
    }

    /**
     * Gets the bus who's schedule is being displayed.
     *
     * @return bus The bus to display the schedule for
     */
    public Bus getBus() {
        return bus;
    }

    /**
     * Gets the currently selected tour.
     *
     * @return the Tour
     */
    private Tour getTour() {
        return tours.get(getSelectedRow());
    }

    /**
     * Add a tour to this table.
     *
     * @param tour The tour  to be added
     */
    public void addTour(Tour tour) {
        tours.add(tour);
    }
}
