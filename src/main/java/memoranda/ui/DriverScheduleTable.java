package main.java.memoranda.ui;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.Tour;
import main.java.memoranda.TourColl;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * DriverScheduleTable is a JTable that contains the data related to a Driver's Schedule (Tour Name, bus ID, tour ID, date, time)
 * 
 * @author Derek Argall
 * @version 04/05/2020
 */
public class DriverScheduleTable extends JTable {
    private ArrayList<Tour> tours;
    private Driver driver;
    private TourColl tourColl;
    private DriverColl driverColl;
    private TableRowSorter<TableModel> sorter;
    private final int HEIGHT = 24;

    /**
     * Constructor for DriverScheduleTable. Sets a default driver
     * 
     * @param driver The default driver's who's schedule will be displayed
     */
    public DriverScheduleTable(Driver driver) {
        super();
        setDriver(driver);
        init();
    }

    /**
     * Constructor for DriverScheduleTable to be used when DriverColl is empty
     */
    public DriverScheduleTable() {
		super();
		setDriver(null);
        init();
	}

	private void init() {
		setToolTipText("Click to select a tour. Right-click for options.");
		
		driverColl = CurrentProject.getDriverColl();
		tourColl = CurrentProject.getTourColl();
    	
    	JPopupMenu optionsMenu = new JPopupMenu();
    	optionsMenu.setFont(new Font("Dialog", 1, 10));
    	
    	JMenuItem removeTour = new JMenuItem("Remove Tour");
    	removeTour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeActionEvent(e);
			}
    	});
    	
    	optionsMenu.add(removeTour);
    	
    	addMouseListener(new MouseAdapter() {
    		public void mousePressed(MouseEvent e) {
    			maybeShowPopup(e);
    		}

    		public void mouseReleased(MouseEvent e) {
    			maybeShowPopup(e);
    		}

    		private void maybeShowPopup(MouseEvent e) {
    			if (e.isPopupTrigger()) {
    				optionsMenu.show(e.getComponent(), e.getX(), e.getY());
    			}
    		}
    	});
    	
    	setRowHeight(HEIGHT);
        setShowGrid(false);
        
        ScheduleTableModel model = new ScheduleTableModel();
        setModel(model);
        
        sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);
        
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        if (tours.size() > 0) {
        	setRowSelectionInterval(0, 0);
        }
        
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
    	//Reload driver's tours in case this method was called from BusTable or BusScheduleTable
    	setDriver(driver);
        init();
        updateUI();
    }

    /**
     * Defines how to render a cell
     */

	/**
	 *
	 * @param row
	 * @param column
	 * @return
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
                Local.getString("Bus ID"),
                //Local.getString("Date"),
                Local.getString("Time")};

        public String getColumnName(int i) {
            return columnNames[i];
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
        	if (tours == null) {
        		return 0;
        	}
        	
            return tours.size();
        }

        public Object getValueAt(int row, int col) {
            //String tour = "Tour";
        	Tour tour = driver.getTours().toArray(new Tour[tours.size()])[row];

            if (col == 0) {
            	return tour.getName();
            }
            
            if (col == 1) {
            	return tour.getID();
            }
            
            if (col == 2) {
            	return tour.getBusID();
            }
            
            if (col == 3) {
            	return tour.getTime();
            }
            
            /*if (col == 4) {
            	return tour.getDate();
            }*/

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
    
    private void removeActionEvent(ActionEvent e) {
    	Tour tour = getTour();
    	int result = JOptionPane.showConfirmDialog(null,  "Remove Tour from " + driver.getName() + "'s Schedule?", "Delete Tour", JOptionPane.OK_CANCEL_OPTION);
    	
    	if (result == JOptionPane.OK_OPTION) {
    		driver.delTour(tour);
    		
    		try {
				CurrentStorage.get().storeDriverList(CurrentProject.get(), driverColl);
				CurrentStorage.get().storeTourList(CurrentProject.get(), tourColl);
				setDriver(driver);
				tableChanged();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
    	}
    }
    
    /**
     * Sets the driver who's schedule is to be displayed
     * 
     * @param driver The driver to display the schedule for
     */
    public void setDriver(Driver driver) {
    	this.driver = driver;
    	
    	if (driver == null) {
    		tours = new ArrayList<Tour>();
    	}
    	
    	else {
    		tours = new ArrayList<Tour>(driver.getTours());    		
    	}
    }
    
    /**
     * Gets the driver who's schedule is being displayed
     * 
     * @param driver The driver to display the schedule for
     */
    public Driver getDriver() {
    	return driver;
    }
    
    /**
     * Gets the currently selected tour
     * 
     * @return the Tour
     */
    private Tour getTour() {
    	return tours.get(getSelectedRow());
    }
    
    /**
     * Add a tour to this table
     * 
     * @param tour The tour  to be added
     */
    public void addTour(Tour tour) {
    	tours.add(tour);
    }
}
