package main.java.memoranda.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Tour;
import main.java.memoranda.TourColl;


/**
 * JTable to display current Tours in system
 *
 * @author John Thurstonson
 * @version 04/10/2021
 * <p>
 * References:
 * Used EventsTable.java as base, v 1.6 2004/10/11 08:48:20 alexeya Exp
 */
public class TourTable extends JTable {

    public static final int TOUR = 100;
    private TourColl tours;

    /**
     * TourTable constructor
     */
    public TourTable() {
        super();
        setModel(new TourTableModel());
        initTable();
        this.setShowGrid(false);
    }

    private void initTable() {

        tours = CurrentProject.getTourColl();
        getColumnModel().getColumn(0).setPreferredWidth(60);
        getColumnModel().getColumn(0).setMaxWidth(60);
        clearSelection();
        updateUI();
    }

    /**
     * Refresh table
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
     * Creates tour table
     *
     * @author John Thurstonson
     * @version 04/10/2021
     */
    private class TourTableModel extends AbstractTableModel {

        private String[] columnNames = {"Time", "Tour", "Route", "Bus"};


        /**
         * Tour table model constructor
         */
        public TourTableModel() {
            super();
        }

        /**
         * Return number of columns in table
         *
         * @return int
         */
        public int getColumnCount() {
            return 4;
        }

        /**
         * Return number of rows in table
         *
         * @return int
         */
        public int getRowCount() {
            int i;
            try {
                i = tours.size();
            } catch (NullPointerException e) {
                i = 1;
            }
            return i;
        }

        /**
         * Return object in cell
         *
         * @return Object
         */
        public Object getValueAt(int row, int col) {
            Tour tour = tours.getTours().toArray(new Tour[tours.size()])[row];

            if (col == 0) {
                return tour.getTimeString();
            } else if (col == 1) {
                return tour.getName();
            } else if (col == 2) {
                return tour.getRoute().getName();
            } else if (col == 3) {
                return tour.getBus();
            } else {
                return tour;
            }
        }

        /**
         * Return name of column
         *
         * @return String
         */
        public String getColumnName(int col) {
            return columnNames[col];
        }
    }
}
