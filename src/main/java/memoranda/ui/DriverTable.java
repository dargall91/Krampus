package main.java.memoranda.ui;

import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.IndexedObject;
import main.java.memoranda.ui.table.TableSorter;
import main.java.memoranda.util.DuplicateKeyException;
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
 * @author derek
 *
 */
public class DriverTable extends JTable {
    private DriverColl drivers;
    private TableSorter sorter;

    /**
     * Constructor for a DriverTable
     */
    public DriverTable() {
        super();
        //TODO: ArrayList->DriverColl
        //drivers = new ArrayList<String>();
        drivers = new DriverColl();
        //drivers2.add(new Driver(1, "Test Name", "111-111-1111"));
        initTable();
        
    }

    private void initTable() {
        //TODO: No longer needed? Could just be moved to constructor
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
     * TODO: Figure out what this did in the original code
     * Presumably call when the table is changed
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
     * Defines the column names for the JTable and determines what data to place in each column
     * 
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
            //convert to array
            Driver[] driverArray = drivers.getDrivers().toArray(new Driver[drivers.size()]);

            switch (col) {
                case 0:
                    return driverArray[row].getName();//driver.getName();
                case 1:
                    return driverArray[row].getId();//driver.getID();
                case 2:
                    return driverArray[row].getPhoneNumber();//driver.getPhone();
            }

            return null;
        }

        public Class getColumnClass(int col) {
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
        }
    }
}
