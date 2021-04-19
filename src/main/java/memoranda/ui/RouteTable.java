package main.java.memoranda.ui;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Route;
import main.java.memoranda.RouteColl;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * JTable to display routes in the system
 *
 * @author Chris Boveda
 * @version 2021-04-11
 */
public class RouteTable extends JTable {
    private RouteColl routes;


    /**
     * Default CTor for RouteTable
     */
    public RouteTable() {
        super();
        setModel(new RouteTableModel());
        initTable();
        this.setShowGrid(false);
    }

    private void initTable() {
        routes = CurrentProject.getRouteColl();
        getColumnModel().getColumn(0).setPreferredWidth(60);
        getColumnModel().getColumn(0).setMaxWidth(60);
        updateUI();
        
        if (routes.size() > 0) {
            setRowSelectionInterval(0, 0);
        }
    }


    /**
     * Refreshes the table
     */
    public void refresh() {
        initTable();
    }


    /**
     * Setup Cells for Table
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
                Component comp;
                comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                comp.setForeground(java.awt.Color.gray);
                return comp;
            }
        };
    }


    /**
     * Route Table Builder
     *
     * @author Chris Boveda
     * @version 2021-04-11
     */
    private class RouteTableModel extends AbstractTableModel {
        private final String[] COLUMN_NAMES = {"Route ID", "Name", "Start", "Length"};


        /**
         * Returns the row count of the table.
         *
         * @return int row count
         */
        @Override
        public int getRowCount() {
            return (routes != null) ? routes.size() : 1;
        }


        /**
         * Returns the column count of the table
         *
         * @return int col count
         */
        @Override
        public int getColumnCount() {
            return 4;
        }


        /**
         * Returns object located in a cell index
         *
         * @param rowIndex    row index of the object
         * @param columnIndex col index of the object
         * @return the object located in that cell
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Route r = routes.getRoutes().toArray(new Route[routes.size()])[rowIndex];

            if (columnIndex == 0) {
                return r.getID();
            } else if (columnIndex == 1) {
                return r.getName();
            } else if (columnIndex == 2) {
                return (r.getRoute().size() == 0) ? null : r.getRoute().getFirst().getName();
            } else if (columnIndex == 3) {
                return r.length();
            } else {
                return r;
            }
        }


        /**
         * Returns the name of the specific column
         *
         * @param columnIndex the column to fetch
         * @return the name as a string
         */
        public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }
    }
    
    /**
     * Gets the currently selected Route
     * 
     * @return The selected Route
     */
    public Route getRoute() {
        if (getSelectedRow() < 0) {
            return null;
        }
        
        return routes.getRoutes().toArray(new Route[routes.size()])[getSelectedRow()];
    }

}
