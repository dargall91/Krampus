package main.java.memoranda.ui;

import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.ui.table.TableSorter;
import main.java.memoranda.util.Local;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;

/**
 * DriverTable is a JTable that contains the data related to a Driver (name, ID, and phone number)
 *
 */
public class DriverTable extends JTable {
    private DriverColl drivers;
    private TableSorter sorter;

    /**
     * Constructor for a DriverTable
     * 
     * @param drivers The DriverColl to use in this table
     */
    public DriverTable(DriverColl drivers) {
        super();
        this.drivers = drivers;
        initTable();   
    }

    private void initTable() {
        //TODO: No longer needed? Could just be moved to constructor
    	//drivers = new DriverColl();
    	sorter = new TableSorter(new DriverTableModel());
        sorter.addMouseListenerToHeaderInTable(this);
        setModel(sorter);
        this.setShowGrid(false);
        initColumnsWidth();
    }

    private void initColumnsWidth() {
        for (int i = 0; i < 3; i++) {
            TableColumn column = getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(100);
                column.setPreferredWidth(300);
            }

            else {
                column.setMinWidth(100);
                column.setPreferredWidth(100);
            }
        }
    }

    /**
     * Repaints the table to reflect any changes to the data
     */
    public void tableChanged() {
        initTable();
        sorter.tableChanged(null);
        initColumnsWidth();
        updateUI();
    }

    /**
     * TODO: Figure out what this did in the original code
     */
    public TableCellRenderer getCellRenderer(int row, int column) {
        return new javax.swing.table.DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                JLabel comp;

                return (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
    }

    /**
     * Defines the table model for the Driver Table
     */
    private class DriverTableModel extends AbstractTableModel {
        private String[] columnNames = {
                Local.getString("Name"),
                Local.getString("ID"),
                Local.getString("Phone Number")};

        public String getColumnName(int i) {
            return columnNames[i];
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return drivers.size();
        }

        public Object getValueAt(int row, int col) {
            //get driver at row
            Driver driver = drivers.getDrivers().toArray(new Driver[drivers.size()])[row];

            switch (col) {
                case 0:
                    return driver.getName();//driver.getName();
                case 1:
                    return driver.getId();//driver.getID();
                case 2:
                    return driver.getPhoneNumber();//driver.getPhone();
            }

            return null;
        }

        /*public Class getColumnClass(int col) {
            try {
                switch (col) {
                    case 1:
                        return Class.forName("java.lang.Integer");
                    case 0:
                    case 2:
                        return Class.forName("java.lang.String");
                }
            }

            catch (Exception ex) {
                new ExceptionDialog(ex);
            }

            return null;
        }*/
    }
}
