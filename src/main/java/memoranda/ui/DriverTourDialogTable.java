package main.java.memoranda.ui;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Driver;
import main.java.memoranda.Tour;
import main.java.memoranda.TourColl;
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
 * DriverTourDialogTable is a JTable that displays unscheduled tours to be scheduled to a driver.
 * This table is intended to be used in the DriverTourDialog component
 * 
 * @author Derek Argall
 * @version 04/05/2020
 */
public class DriverTourDialogTable extends JTable {
    private Driver driver;
    private TableRowSorter<TableModel> sorter;
    private ArrayList<Tour> tourArray;
    private static final int HEIGHT = 24;

    /**
     * Constructor to the DriverTourDialogTable
     */
    public DriverTourDialogTable() {
		super();
        init();
	}

	private void init() {
        TourColl tours = CurrentProject.getTourColl();
        
        tourArray = new ArrayList<Tour>();
        
        for (int i = 0; i < tours.size(); i++) {
        	Tour tour = tours.getTours().toArray(new Tour[tours.size()])[i];
        	
        	//only display tours with a bus scheduled that do not have a driver
    		if (tour.getDriver() == null && tour.getBus() != null) {
    			tourArray.add(tours.getTours().toArray(new Tour[tours.size()])[i]);
    		}
    	}
    	
    	setRowHeight(HEIGHT);
        setShowGrid(false);
        
        ScheduleTableModel model = new ScheduleTableModel();
        setModel(model);
        
        sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);
        
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
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

    /**
     * Repaints the table to reflect any changes to the data
     */
    public void tableChanged() {
        init();
        initColumnsWidth();
        updateUI();
    }

    /**
     * @see https://docs.oracle.com/javase/7/docs/api/javax/swing/table/TableCellRenderer.html
     */
    @Override
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
                Local.getString("Name"),
                Local.getString("Tour ID"),
                Local.getString("Bus ID"),
                //Local.getString("Date"),
                Local.getString("Time")};

        @Override
        public String getColumnName(int i) {
            return columnNames[i];
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return tourArray.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
        	if (col == 0) {
            	return tourArray.get(row).getName();
            }
            
            if (col == 1) {
            	return tourArray.get(row).getID();
            }
            
            if (col == 2) {
            	return tourArray.get(row).getBusID();
            }
            
            if (col == 3) {
            	return tourArray.get(row).getTime();
            }
            
            /*if (col == 4) {
            	return tour.get(row).getDate();
            }*/

            return null;
        }

        @Override
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
     * Gets the currently selected tour
     * 
     * @return the Tour
     */
    public Tour getTour() {
    	if (getSelectionModel().isSelectionEmpty() ) {
    		return null;
    	}
    	return tourArray.get(getSelectedRow());
    }
}
