package main.java.memoranda.ui;

import main.java.memoranda.Driver;
import main.java.memoranda.Tour;
import main.java.memoranda.ui.DriverTable.DriverTableModel;
import main.java.memoranda.ui.table.TableSorter;
import main.java.memoranda.util.Local;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

/**
 * ScheduleTable is a JTable that contains the data related to a Driver's Schedule (driver, bus ID, tour, date, time)
 *
 */
public class DriverScheduleTable extends JTable {
    //TODO: ArrayList->TourColl
    private ArrayList<String> tours;
    private static Driver driver;
    private TableRowSorter<TableModel> sorter;
    private final int HEIGHT = 24;

    /**
     * Constructor for DriverScheduleTable
     * 
     * @param driver The default driver's who's schedule will be displayed
     */
    public DriverScheduleTable(Driver driver) {
        super();
        setDriver(driver);
        //TODO: ArrayList->TourColl
        tours = new ArrayList<String>();
        init();
    }

    private void init() {
    	//TODO: read data from TourColl
        tours.add("Tour1");
        tours.add("Tour2");
        tours.add("Tour3");
        
    	setToolTipText("Click to select a tour. Right-click to edit a tour or the schedule.");
    	
    	setRowHeight(HEIGHT);
        setShowGrid(false);
        
        ScheduleTableModel model = new ScheduleTableModel();
        setModel(model);
        
        sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);
        
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowSelectionInterval(0, 0);
        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        
        initColumnsWidth();
    }

    private void initColumnsWidth() {
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

    public void tableChanged() {
        init();
        initColumnsWidth();
        updateUI();
    }

    /**
     * Defines how to render a cell
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
            	JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (((row % 2) > 0) && (!isSelected)) {
                    comp.setBackground(new Color(230, 240, 255));
                }
                
                return comp;
            }
        };
    }

    private class ScheduleTableModel extends AbstractTableModel {
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

            if (col == 0) {
            	return tour;
            }
            
            if (col == 1) {
            	return tour;
            }
            
            if (col == 2) {
            	return tour;
            }
            
            if (col == 3) {
            	return tour;
            }
            
            if (col == 4) {
            	return tour;
            }

            return null;
        }

        public Class getColumnClass(int col) {
        	for (int i = 0; i < getRowCount(); i++) {
        		Object obj = getValueAt(i, col); {
        			if (obj != null) {
        				return obj.getClass();
        			}
        		}
        	}

        	//default to String
            return String.class;
        }
    }
    
    /**
     * Sets the driver who's schedule is to be displayed
     * 
     * @param driver The driver to display the schedule for
     */
    public static void setDriver(Driver d) {
    	driver = d;
    }
    
    /**
     * Gets the currently selected tour
     * 
     * @return the Tour
     */
    private Tour getTour() {
    	return null;
    }
}
