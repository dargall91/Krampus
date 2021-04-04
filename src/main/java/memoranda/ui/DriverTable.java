package main.java.memoranda.ui;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.ui.table.TableSorter;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * DriverTable is a JTable that contains the data related to a Driver (name, ID, and phone number)
 *
 */
public class DriverTable extends JTable {
    private DriverColl drivers;
    private JPopupMenu optionsMenu;
    private TableRowSorter<TableModel> sorter;
    private final int HEIGHT = 24;
    private final int ID_COLUMN = 1;

    /**
     * Constructor for a DriverTable
     * 
     * @param drivers The DriverColl to use in this table
     */
    public DriverTable(DriverColl drivers) {
        super();
        this.drivers = drivers;
        init();   
    }

    private void init() {
    	setToolTipText("Click a driver to display their schedule. Right-click to edit the selected driver.");
    	
    	optionsMenu = new JPopupMenu();
    	optionsMenu.setFont(new Font("Dialog", 1, 10));
    	
    	JMenuItem editDriver = new JMenuItem("Edit Driver");
    	editDriver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editActionEvent(e);
			}
    		
    	});
    	
    	JMenuItem deleteDriver = new JMenuItem("Delete Driver");
    	deleteDriver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteActionEvent(e);
			}
    		
    	});
    	
    	optionsMenu.add(editDriver);
    	optionsMenu.add(deleteDriver);
    	
    	addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent e) {
    			//TODO: display this driver's tours
    			//TODO: This class needs access to DriverPanel class, which then needs some kind of displayTours(driver) method
    		}
    		
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
        
        DriverTableModel model = new DriverTableModel();
        setModel(model);
        
        sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);
        
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowSelectionInterval(0, 0);
        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        
        initColumnsWidth();
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
     * Repaints the table to reflect any changes to the data
     */
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

    /**
     * Defines the table model for the Driver Table
     */
    private class DriverTableModel extends AbstractTableModel {
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
            //set the selected driver for use by other methods in addition to displaying information
            Driver driver = drivers.getDrivers().toArray(new Driver[drivers.size()])[row];

            if (col == 0) {
            	return driver.getName();
            }
            
            if (col == 1) {
            	return driver.getId();
            }
            
            if (col == 2) {
            	return driver.getPhoneNumber();
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
    
    private void editActionEvent(ActionEvent e) {
    	DriverDialog dlg = new DriverDialog(App.getFrame(), "Edit Driver");
    	Driver driver = getDriver();
    	dlg.setName(driver.getName());
    	dlg.setPhone(driver.getPhoneNumber());
    	
    	Dimension frmSize = App.getFrame().getSize();
		Point loc = App.getFrame().getLocation();
		
		dlg.setLocation(
				(frmSize.width - dlg.getSize().width) / 2 + loc.x,
				(frmSize.height - dlg.getSize().height) / 2
						+ loc.y);
		dlg.setVisible(true);
		
		if (!dlg.isCancelled()) {
			driver.setName(dlg.getName());
			driver.setPhoneNumber(dlg.getPhone());
			
			try {
				CurrentStorage.get().storeDriverList(drivers, CurrentProject.get());
				tableChanged();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }
    
    private void deleteActionEvent(ActionEvent e) {
    	Driver driver = getDriver();
    	int result = JOptionPane.showConfirmDialog(null,  "Delete " + driver.getName() + "?", "test", JOptionPane.OK_CANCEL_OPTION);
    	
    	if (result == JOptionPane.OK_OPTION) {
    		drivers.del(driver.getId());
    		
    		try {
				CurrentStorage.get().storeDriverList(drivers, CurrentProject.get());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
    		
			tableChanged();
    	}
    }
    
    /**
     * Gets the currently selected Driver
     * 
     * @return the Driver
     */
    public Driver getDriver() {
    	return (Driver) drivers.get((int) getValueAt(getSelectedRow(), ID_COLUMN));
    }
}
