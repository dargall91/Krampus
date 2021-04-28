package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import javax.swing.table.TableColumn;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.Route;
import main.java.memoranda.RouteColl;

/**
 * JTable to display routes in the system.
 *
 * @author Chris Boveda, John Thurstonson, Brian Pape
 * @version 2021-04-25
 */
public class RouteTable extends JTable implements MouseListener {
    private RouteColl routes;
    private RouteMapPanel parentPanel;
    private NodeTable nodeTable;

    /**
     * Default CTor for RouteTable.
     */
    public RouteTable(RouteMapPanel parentPanel) {
        super();
        setModel(new RouteTableModel());
        initTable();
        this.setShowGrid(false);
        this.parentPanel = parentPanel;
    }

    private void initTable() {
        routes = CurrentProject.getRouteColl();
        getColumnModel().getColumn(0).setPreferredWidth(60);
        getColumnModel().getColumn(0).setMaxWidth(60);
        updateUI();

        //if (routes.size() > 0) {
        if (getSelectedRow() >= 0 && getSelectedRow() < routes.size()) {
            setRowSelectionInterval(getSelectedRow(), getSelectedRow());
        } else {
            setRowSelectionInterval(0, 0);
        }

        addMouseListener(this);
        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        initColumnsWidth();
    }

    public void setNodeTable(NodeTable nodeTable) {
        this.nodeTable = nodeTable;
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
     *
     * @param row    row
     * @param column column
     * @return new renderer
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
                comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);

                if (((row % 2) > 0) && (!isSelected)) {
                    comp.setBackground(new Color(230, 240, 255));
                }

                return comp;
            }
        };
    }

    /**
     * Gets the currently selected Route.
     *
     * @return The selected Route
     */
    public Route getRoute() {
        if (getSelectedRow() < 0) {
            return null;
        }

        return routes.getRoutes().toArray(new Route[routes.size()])[getSelectedRow()];
    }

    /**
     * Action for mouse clicked. Refreshes RouteMap.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        parentPanel.getRouteMap().refresh();
    }

    /**
     * Action for mouse pressed. Currently no action.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        nodeTable.refresh();
    }

    /**
     * Action for mouse released. Currently no action.
     */
    @Override
    public void mouseReleased(MouseEvent e) {


    }

    /**
     * Action for mouse entered. Currently no action.
     */
    @Override
    public void mouseEntered(MouseEvent e) {


    }

    /**
     * Action for mouse exited. Currently no action.
     */
    @Override
    public void mouseExited(MouseEvent e) {


    }

    /**
     * Route Table Builder.
     *
     * @author Chris Boveda
     * @version 2021-04-11
     */
    private class RouteTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Route ID", "Name", "Start", "Length"};


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
            Route r = routes.getRoutes().toArray(new Route[routes.size()])[rowIndex];

            if (columnIndex == 0) {
                return r.getID();
            } else if (columnIndex == 1) {
                return r.getName();
            } else if (columnIndex == 2) {
                return (r.getRoute().size() == 0) ? null : r.getRoute().getFirst().getName();
            } else if (columnIndex == 3) {
                return Double.parseDouble(String.format("%.2f", r.length()));
            } else {
                return r;
            }
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
