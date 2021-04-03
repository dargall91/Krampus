package main.java.memoranda.ui;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.ui.table.TableSorter;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * DriverTable is a JTable that contains the data related to a Driver (name, ID, and phone number)
 *
 */
public class DriverTable extends JTable {
    private DriverColl drivers;
    private TableSorter sorter;
    private JPopupMenu menu;
    private Driver driver;

    /**
     * Constructor for a DriverTable
     * 
     * @param drivers The DriverColl to use in this table
     */
    public DriverTable(DriverColl drivers) {
        super();
        this.drivers = drivers;
        setToolTipText("Click to display tour schedule. Right-click to edit selected driver.");
        initTable();   
    }

    private void initTable() {
        //TODO: No longer needed? Could just be moved to constructor
    	menu = new JPopupMenu();
    	menu.setFont(new Font("Dialog", 1, 10));
    	
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
    	
    	menu.add(editDriver);
    	menu.add(deleteDriver);
    	
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
    				menu.show(e.getComponent(), e.getX(), e.getY());
    			}
    		}
    	});
    	
    	sorter = new TableSorter(new DriverTableModel());
        sorter.addMouseListenerToHeaderInTable(this);
        setModel(sorter);
        this.setShowGrid(false);
        initColumnsWidth();
    }

    private void initColumnsWidth() {
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

    /**
     * Repaints the table to reflect any changes to the data
     */
    public void tableChanged() {
        initTable();
        sorter.tableChanged(null);
        initColumnsWidth();
        updateUI();
    }

    /**
     * TODO: Figure out what this did in the original code
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
                JLabel comp;

                return (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
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
            driver = drivers.getDrivers().toArray(new Driver[drivers.size()])[row];

            switch (col) {
                case 0:
                    return driver.getName();//driver.getName();
                case 1:
                    return driver.getId();//driver.getID();
                case 2:
                    return driver.getPhoneNumber();//driver.getPhone();
            }

            return null;
        }

        //TODO: Possible not needed, seems to cause errors when sorting by ID, but removing doesn't seem to change any other functionality
        /*public Class getColumnClass(int col) {
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
        }*/
    }
    
    private void editActionEvent(ActionEvent e) {
    	DriverDialog dlg = new DriverDialog(App.getFrame());
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
    	int result = JOptionPane.showConfirmDialog(null,  "Delete Driver?", "test", JOptionPane.OK_CANCEL_OPTION);
    	
    	if (result == JOptionPane.OK_OPTION) {
    		drivers.del(driver.getId());
    		try {
				CurrentStorage.get().storeDriverList(drivers, CurrentProject.get());
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			tableChanged();
    	}
    }
}
