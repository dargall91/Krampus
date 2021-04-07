package main.java.memoranda.ui;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Bus;
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
 * BusTourDialogTable is a JTable that displays unscheduled tours to be scheduled to a bus.
 * This table is intended to be used in the BusTourDialog component
 * 
 * @author Derek Argall
 * @version 04/05/2020
 */
public class BusTourDialogTable extends JTable {
    private Bus bus;
    private TableRowSorter<TableModel> sorter;
    private ArrayList<Tour> tourArray;
    private final int HEIGHT = 24;

    /**
     * Constructor to the BusTourDialogTable
     */
    public BusTourDialogTable() {
		super();
        init();
	}

	private void init() {
        TourColl tours = CurrentProject.getTourColl();
        
        tourArray = new ArrayList<Tour>();
        
        for (int i = 0; i < tours.size(); i++) {
        	Tour tour = tours.getTours().toArray(new Tour[tours.size()])[i];
        	
        	//only display tours without a bus
    		if (tour.getDriver() == null) {
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
                Local.getString("Name"),
                Local.getString("Tour ID"),
                //Local.getString("Date"),
                Local.getString("Time")};

        public String getColumnName(int i) {
            return columnNames[i];
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return tourArray.size();
        }

        public Object getValueAt(int row, int col) {
        	if (col == 0) {
            	return tourArray.get(row).getName();
            }
            
            if (col == 1) {
            	return tourArray.get(row).getID();
            }
            
            if (col == 2) {
            	return tourArray.get(row).getTime();
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
