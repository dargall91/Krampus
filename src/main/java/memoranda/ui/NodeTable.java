package main.java.memoranda.ui;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Route;
import main.java.memoranda.Node;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * JTable to display nodes from a selected route in the system.
 *
 * @author Chris Boveda
 * @version 2021-04-27
 */
public class NodeTable extends JTable {
    private Route route;
    private final RouteTable routeTable;


    /**
     * Default CTor for RouteTable.
     */
    public NodeTable(RouteTable routeTable) {
        super();
        setModel(new NodeTableModel());
        this.routeTable = routeTable;
        initTable();
        this.setShowGrid(false);
    }

    private void initTable() {
        //todo NPE
        route = (Route) CurrentProject.getRouteColl().getRoutes().toArray()
                [(routeTable.getSelectedRow() != -1) ? routeTable.getSelectedRow() : 0];
        getColumnModel().getColumn(0).setPreferredWidth(60);
        getColumnModel().getColumn(0).setMaxWidth(60);
        updateUI();
    }


    /**
     * Refreshes the table.
     */
    public void refresh() {
        initTable();
    }


    /**
     * Setup Cells for Table.
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
     * Node Table Builder.
     *
     * @author Chris Boveda
     * @version 2021-04-16
     */
    private class NodeTableModel extends AbstractTableModel {
        private final String[] COLUMN_NAMES = {"Node ID", "Name", "Lat", "Long"};


        /**
         * Returns the row count of the table.
         *
         * @return int row count
         */
        @Override
        public int getRowCount() {
            //todo NPE
            return (route != null && route.getRoute().size() != 0) ? route.getRoute().size() : 1;
        }


        /**
         * Returns the column count of the table.
         *
         * @return int col count
         */
        @Override
        public int getColumnCount() {
            return 4;
        }


        /**
         * Returns object located in a cell index.
         *
         * @param rowIndex    row index of the object
         * @param columnIndex col index of the object
         * @return the object located in that cell
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            //todo NPE

            Node n;
            if (route.getRoute().isEmpty()) {
                n = new Node(0, "Please add nodes to the route", 0.0, 0.0);
            } else {
                n = route.getRoute().get(rowIndex);
            }

            if (columnIndex == 0) {
                return n.getID();
            } else if (columnIndex == 1) {
                return n.getName();
            } else if (columnIndex == 2) {
                return Double.parseDouble(String.format("%.2f", n.getCoords().getLat()));
            } else if (columnIndex == 3) {
                return Double.parseDouble(String.format("%.2f", n.getCoords().getLon()));
            } else {
                return n;
            }
        }


        /**
         * Returns the name of the specific column.
         *
         * @param columnIndex the column to fetch
         * @return the name as a string
         */
        public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }
    }

}
