package main.java.memoranda.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.FileStorage;
import main.java.memoranda.util.Util;

/*$Id: AgendaPanel.java,v 1.11 2005/02/15 16:58:02 rawsushi Exp $*/
public class DriverPanel extends JPanel {
	private DriverTable driverTable;
	private ScheduleTable scheduleTable;
	private DailyItemsPanel parentPanel;
	private DriverColl drivers;
	private FileStorage storage;
	String gotoTask = null;

	private boolean isActive = true;

	/**
	 * Constructor for the DriverPanel
	 * 
	 * Creates a JPanel which houses the the information about the Driver Schedule
	 * 
	 * @param parentPanel The DailyItemsPanel which will house this panel
	 */
	public DriverPanel(DailyItemsPanel parentPanel) {
		try {
			this.parentPanel = parentPanel;
			jbInit();
		} catch (Exception ex) {
			new ExceptionDialog(ex);
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		CurrentStorage.get().createProjectStorage(CurrentProject.get());
		drivers = CurrentStorage.get().openDriverList(CurrentProject.get());
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		add(getDriverPanel());
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(getSchedulePanel());
	}

	private JPanel getDriverPanel() {
		driverTable = new DriverTable(drivers);
		driverTable.setRowHeight(24);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(JPanel.TOP_ALIGNMENT);
		
		JLabel label = new JLabel("Drivers");
		label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, 25));
		label.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JButton add = new JButton("Add Driver");
		add.setAlignmentX(JButton.LEFT_ALIGNMENT);
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				DriverDialog dlg = new DriverDialog(App.getFrame());
				Dimension frmSize = App.getFrame().getSize();
				Point loc = App.getFrame().getLocation();
				
				dlg.setLocation(
						(frmSize.width - dlg.getSize().width) / 2 + loc.x,
						(frmSize.height - dlg.getSize().height) / 2
								+ loc.y);
				dlg.setVisible(true);
				
				if (!dlg.isCancelled()) {
					Driver driver = new Driver(9001, dlg.getName(), dlg.getPhone());
					
					try {
						drivers.createUnique(driver);
						CurrentStorage.get().storeDriverList(drivers, CurrentProject.get());
						driverTable.tableChanged();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(driverTable);
		scroll.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);

		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(add);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(scroll);

		return panel;
	}

	private JPanel getSchedulePanel() {
		scheduleTable = new ScheduleTable();
		scheduleTable.setRowHeight(24);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(JPanel.TOP_ALIGNMENT);
		
		JLabel label = new JLabel("Schedule");
		label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, 25));
		label.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(scheduleTable);
		scroll.setAlignmentX(JButton.LEFT_ALIGNMENT);

		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(scroll);

		return panel;
	}

	/**
	 * Refreshes this panel
	 * 
	 * @param date
	 */
	public void refresh(CalendarDate date) {
		//viewer.setText(AgendaGenerator.getAgenda(date,expandedTasks));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(gotoTask != null) {
					//viewer.scrollToReference(gotoTask);
					//scrollPane.setViewportView(viewer);
					Util.debug("Set view port to " + gotoTask);
				}
			}
		});

		Util.debug("Summary updated.");
	}

	/**
	 * Flags this panel as the active panel
	 * 
	 * @param isa
	 */
	public void setActive(boolean isa) {
		isActive = isa;
	}
}
