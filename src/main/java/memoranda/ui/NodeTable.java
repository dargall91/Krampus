package main.java.memoranda.ui;

import java.awt.*;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import javax.swing.table.TableColumn;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.Node;
import main.java.memoranda.Route;


/**
 * JTable to display nodes from a selected route in the system.
 *
 * @author Chris Boveda
 * @version 2021-04-27
 */
public class NodeTable extends JTable {
    private Route route;
    private LinkedList<Node> trimmedRoute;
    private final RouteTable routeTable;


    /**
     * Default CTor for RouteTable.
     */
    public NodeTable(RouteTable routeTable) {
        super();
        setModel(new NodeTableModel());
        this.routeTable = routeTable;
        trimmedRoute = new LinkedList<>();
        initTable();
        this.setShowGrid(false);
    }

    private void initTable() {
        //todo NPE
        route = (Route) CurrentProject.getRouteColl().getRoutes().toArray()
            [(routeTable.getSelectedRow() != -1) ? routeTable.getSelectedRow() : 0];
        trimmedRoute = new LinkedList<>();
        for (Node n : route.getRoute()) {
            if (n.isVisible()) {
                trimmedRoute.add(n);
            }
        }



        getColumnModel().getColumn(0).setPreferredWidth(60);
        getColumnModel().getColumn(0).setMaxWidth(60);


        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        initColumnsWidth();
        updateUI();
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
        private final String[] columnNames = {"Node ID", "Name", "Lat", "Long"};

        /**
         * Returns the row count of the table.
         *
         * @return int row count
         */
        @Override
        public int getRowCount() {
            //todo NPE
            return (route != null && trimmedRoute.size() != 0) ? trimmedRoute.size() : 1;
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
            Node n;
            if (trimmedRoute.isEmpty()) {
                n = new Node(0, "Please add nodes to the route", 0.0, 0.0);
            } else {
                n = trimmedRoute.get(rowIndex);
            }

            if (columnIndex == 0) {
                return n.getID();
            } else if (columnIndex == 1) {
                return n.getName();
            } else if (columnIndex == 2) {
                return coordinateDecimalToString(n.getCoords().getLat());
            } else if (columnIndex == 3) {
                return coordinateDecimalToString(n.getCoords().getLon());
            } else {
                return n;
            }
        }


        private String coordinateDecimalToString(Double value) {
            int degrees = (int) Math.floor(value);
            double minutesDouble = (value - degrees) * 60;
            int minutes = (int) Math.floor(minutesDouble);
            double secondsDouble = (minutesDouble - minutes) * 60;
            int seconds = (int) Math.floor(secondsDouble);
            return String.format("%d, %d', %d\"", degrees, minutes, seconds);
        }


        /**
         * Returns the name of the specific column.
         *
         * @param columnIndex the column to fetch
         * @return the name as a string
         */
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }
    }

}
