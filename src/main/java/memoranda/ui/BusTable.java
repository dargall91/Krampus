package main.java.memoranda.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
 * BusTable is a JTable that contains the data related to a Bus (Bus Number and ID).
 *
 * @author Derek Argall
 * @version 04/09/2020
 */
public class BusTable extends JTable {
    private BusColl buses;
    private TableRowSorter<TableModel> sorter;
    private static final int HEIGHT = 24;
    private static final int ID_COLUMN = 1;
    private BusScheduleTable busSchedule;
    private DriverScheduleTable driverTable;

    /**
     * Constructor for a BusTable.
     */
    public BusTable(BusScheduleTable busSchedule, DriverScheduleTable driverTable) {
        super();
        this.busSchedule = busSchedule;
        this.driverTable = driverTable;
        init();
    }

    private void init() {
        setToolTipText("Click a bus to display their schedule. Right-click for options.");

        buses = CurrentProject.getBusColl();

        JPopupMenu optionsMenu = new JPopupMenu();
        optionsMenu.setFont(new Font("Dialog", 1, 10));

        JMenuItem editBus = new JMenuItem("Edit Bus");
        editBus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editActionEvent(e);
            }
        });

        JMenuItem deleteBus = new JMenuItem("Delete Bus");
        deleteBus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteActionEvent(e);
            }
        });

        optionsMenu.add(editBus);
        optionsMenu.add(deleteBus);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                busSchedule.setBus(getBus());
                busSchedule.tableChanged();
            }

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

        BusTableModel model = new BusTableModel();
        setModel(model);

        sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (buses.size() >= 1) {
            setRowSelectionInterval(0, 0);
        }

        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        initColumnsWidth();
    }

    private void initColumnsWidth() {
        //dynamically set column width
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

    /**
     * Defines the table model for the Bus Table.
     */
    private class BusTableModel extends AbstractTableModel {
        private String[] columnNames = {
                Local.getString("Number"),
                Local.getString("ID")};

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
            return buses.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            //set the selected bus for use by other methods in addition to displaying information
            Bus bus = buses.getBuses().toArray(new Bus[buses.size()])[row];

            if (col == 0) {
                return bus.getNumber();
            }

            if (col == 1) {
                return bus.getID();
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

    private void editActionEvent(ActionEvent e) {
        BusDialog dlg = new BusDialog(App.getFrame(), "Edit Bus", "Edit");
        Bus bus = getBus();
        dlg.setNumber(bus.getNumber());

        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();

        dlg.setLocation(
                (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                (frmSize.height - dlg.getSize().height) / 2
                        + loc.y);
        dlg.setVisible(true);

        if (!dlg.isCancelled()) {
            bus.setNumber(dlg.getNumber());

            try {
                CurrentStorage.get().storeBusList(CurrentProject.get(), buses);
                tableChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteActionEvent(ActionEvent e) {
        Bus bus = getBus();
        int result = JOptionPane.showConfirmDialog(null, "Delete Bus No. " + bus.getNumber() + "?",
                "Delete Bus", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            TourColl tours = CurrentProject.getTourColl();

            //remove bus from all scheduled tours
            for (Tour t : bus.getTours()) {
                //A driver cannot be scheduled for a tour without a bus. Check if the tour has a driver, then unschedule
                //the tour from the driver if there is
                if (t.getDriver() != null) {
                    t.getDriver().delTour(t);
                    driverTable.tableChanged();
                }

                bus.delTour(t);
            }

            buses.del(bus.getID());

            try {
                CurrentStorage.get().storeBusList(CurrentProject.get(), buses);
                CurrentStorage.get().storeTourList(CurrentProject.get(), tours);
                tableChanged();

                if (buses.size() == 0) {
                    busSchedule.setBus(null);
                } else {
                    busSchedule.setBus(getBus());
                }

                busSchedule.tableChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            tableChanged();
        }
    }

    /**
     * Gets the currently selected Bus.
     *
     * @return the Bus
     */
    public Bus getBus() {
        return (Bus) buses.get((int) getValueAt(getSelectedRow(), ID_COLUMN));
    }

    /**
     * Sets the BusScheduleTable to be updated when a user selects a bus in this table
     * The BusScheduleTable MUST be set for else errors will occur.
     *
     * @param busSchedule The BusScheduleTable
     */
    public void setBusScheduleTable(BusScheduleTable busSchedule) {
        this.busSchedule = busSchedule;
    }
}
