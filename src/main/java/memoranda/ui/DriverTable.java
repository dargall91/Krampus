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

public class DriverTable extends JTable {
    //TODO: ArrayList->DriverColl
    private ArrayList<String> drivers;
    private TableSorter sorter;

    public DriverTable() {
        super();
        //TODO: ArrayList->DriverColl
        drivers = new ArrayList<String>();
        initTable();
        sorter = new TableSorter(new DriverTableModel());
        sorter.addMouseListenerToHeaderInTable(this);
        setModel(sorter);
        this.setShowGrid(false);
        initColumnsWidth();
    }

    private void initTable() {
        //TODO: read data from DriverColl
        drivers.add("Count Dooku");
        drivers.add("Lucifer Morningstar");
        drivers.add("Bowser");
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

    class DriverTableModel extends AbstractTableModel {
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
            //TODO: String -> DriverColl
            String driver = drivers.get(row);

            switch (col) {
                case 0:
                    return driver;//driver.getName();
                case 1:
                    return driver;//driver.getID();
                case 2:
                    return driver;//driver.getPhone();
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
