package main.java.memoranda.ui;

import main.java.memoranda.ui.table.TableSorter;
import main.java.memoranda.util.Local;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.util.ArrayList;

public class ScheduleTable extends JTable {
    //TODO: ArrayList->TourColl
    private ArrayList<String> tours;
    private TableSorter sorter;

    public ScheduleTable() {
        super();
        //TODO: ArrayList->TourColl
        tours = new ArrayList<String>();
        initTable();
        sorter = new TableSorter(new ScheduleTableModel());
        sorter.addMouseListenerToHeaderInTable(this);
        setModel(sorter);
        this.setShowGrid(false);
        initColumnsWidth();
    }

    private void initTable() {
        //TODO: read data from TourColl
        tours.add("Tour1");
        tours.add("Tour2");
        tours.add("Tour3");
    }

    void initColumnsWidth() {
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

    public void tableChanged() {
        initTable();
        sorter.tableChanged(null);
        initColumnsWidth();
        updateUI();
    }

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

    class ScheduleTableModel extends AbstractTableModel {
        private String[] columnNames = {
                Local.getString("Driver"),
                Local.getString("Bus ID"),
                Local.getString("Tour"),
                Local.getString("Date"),
                Local.getString("Time")};

        public String getColumnName(int i) {
            return columnNames[i];
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return tours.size();
        }

        public Object getValueAt(int row, int col) {
            //TODO: String -> TourColl
            String tour = tours.get(row);

            switch (col) {
                case 0:
                    return tour;//driver.getDriver();
                case 1:
                    return tour;//driver.getBusID();
                case 2:
                    return tour;//driver.getName();
                case 3:
                    return tour;//driver.getDate();
                case 4:
                    return tour;//driver.getTime();
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
                    case 3:
                        return Class.forName("java.util.Date");
                    case 4:
                        return Class.forName("java.sql.Time");
                }
            }

            catch (Exception ex) {
                new ExceptionDialog(ex);
            }

            return null;
        }
    }
}
