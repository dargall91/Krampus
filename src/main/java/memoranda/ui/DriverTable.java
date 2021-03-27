package main.java.memoranda.ui;

import main.java.memoranda.ui.table.TableSorter;
import main.java.memoranda.util.Local;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class DriverTable extends JTable {
    //TODO: ArrayList->DriverColl
    private ArrayList<String> drivers;
    //static final int DRIVER_ID = 100;
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
                column.setPreferredWidth(400);//32767);
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

                comp = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 0) {
                    //Resource r = (Resource)getModel().getValueAt(row, DRIVER_ID);
                    //if (!r.isInetShortcut())
                    //    comp.setIcon(MimeTypesList.getMimeTypeForFile((String)value).getIcon());
                    //else {}
                        //comp.setIcon(inetIcon);
                }

                return comp;
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
    }
}
