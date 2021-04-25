package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.Tour;
import main.java.memoranda.TourColl;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;

/**
 * DriverTable is a JTable that contains the data related to a Driver (name, ID, and phone number).
 *
 * @author Derek Argall
 * @version 04/05/2020
 */
public class DriverTable extends JTable {
    private static final int HEIGHT = 24;
    private static final int ID_COLUMN = 1;
    private DriverColl drivers;
    private TableRowSorter<TableModel> sorter;
    private DriverScheduleTable scheduleTable;

    /**
     * Constructor for a DriverTable.
     */
    public DriverTable() {
        super();
        init();
    }

    private void init() {
        setToolTipText("Click a driver to display their schedule. Right-click for options.");

        drivers = CurrentProject.getDriverColl();

        JPopupMenu optionsMenu = new JPopupMenu();
        optionsMenu.setFont(new Font("Dialog", 1, 10));

        JMenuItem editDriver = new JMenuItem("Edit Driver");
        editDriver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editActionEvent(e);
            }
        });

        JMenuItem deleteDriver = new JMenuItem("Delete Driver");
        deleteDriver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteActionEvent(e);
            }
        });

        optionsMenu.add(editDriver);
        optionsMenu.add(deleteDriver);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                scheduleTable.setDriver(getDriver());
                scheduleTable.tableChanged();
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

        DriverTableModel model = new DriverTableModel();
        setModel(model);

        sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (drivers.size() >= 1) {
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

    private void editActionEvent(ActionEvent e) {
        DriverDialog dlg = new DriverDialog(App.getFrame(), "Edit Driver", "Edit");
        Driver driver = getDriver();
        dlg.setName(driver.getName());
        dlg.setPhone(driver.getPhoneNumber());

        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();

        dlg.setLocation(
                (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                (frmSize.height - dlg.getSize().height) / 2
                        + loc.y);
        dlg.setVisible(true);

        if (!dlg.isCancelled()) {
            driver.setName(dlg.getName());
            driver.setPhoneNumber(dlg.getPhone());

            try {
                CurrentStorage.get().storeDriverList(CurrentProject.get(), drivers);
                tableChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteActionEvent(ActionEvent e) {
        Driver driver = getDriver();
        int result = JOptionPane.showConfirmDialog(null, "Delete " + driver.getName()
                + "?", "Delete Driver", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            TourColl tours = CurrentProject.getTourColl();

            //remove driver from all scheduled tours
            for (Tour t : driver.getTours()) {
                driver.delTour(t);
            }

            drivers.del(driver.getID());

            try {
                CurrentStorage.get().storeDriverList(CurrentProject.get(), drivers);
                CurrentStorage.get().storeTourList(CurrentProject.get(), tours);
                tableChanged();

                if (drivers.size() == 0) {
                    scheduleTable.setDriver(null);
                } else {
                    scheduleTable.setDriver(getDriver());
                }

                scheduleTable.tableChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            tableChanged();
        }
    }

    /**
     * Gets the currently selected Driver.
     *
     * @return the Driver
     */
    public Driver getDriver() {
        return (Driver) drivers.get((int) getValueAt(getSelectedRow(), ID_COLUMN));
    }

    /**
     * Sets the DriverScheduleTable to be updated when a user selects a driver in this table.
     *
     * @param scheduleTable The DriverScheduleTable
     */
    public void setScheduleTable(DriverScheduleTable scheduleTable) {
        this.scheduleTable = scheduleTable;
    }

    private class DriverTableModel extends AbstractTableModel {
        private String[] columnNames = {
                Local.getString("Name"),
                Local.getString("ID"),
                Local.getString("Phone Number")
        };

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
            return drivers.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            Driver driver = drivers.getDrivers().toArray(new Driver[drivers.size()])[row];

            if (col == 0) {
                return driver.getName();
            }

            if (col == 1) {
                return driver.getID();
            }

            if (col == 2) {
                return driver.getPhoneNumber();
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
}
