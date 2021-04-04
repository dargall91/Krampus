package main.java.memoranda.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Driver;
import main.java.memoranda.DriverColl;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Util;

/**
 * A JPanel that provides the interface for a user to add, edit, and delete drivers from the system, as well as schedule tours for a driver
 */
public class DriverPanel extends JSplitPane {
	private DriverTable driverTable;
	private DriverScheduleTable scheduleTable;
	private DailyItemsPanel parentPanel;
	private DriverColl drivers;
	private final int OVER_9000 = 9001;
	private String gotoTask;
	private boolean isActive;
	private final Dimension VERTICAL_GAP = new Dimension(0, 5);
	private final int LABEL_SIZE = 25;

	/**
	 * Constructor for the DriverPanel
	 * 
	 * Creates a JPanel which houses the the information about the Driver Schedule
	 * 
	 * @param parentPanel The DailyItemsPanel which will house this panel
	 */
	public DriverPanel(DailyItemsPanel parentPanel) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		
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
		drivers = CurrentStorage.get().openDriverList(CurrentProject.get(), CurrentProject.getTourColl());
		
		setActive(true);

		setDividerSize(5);
		setResizeWeight(0.4);
		setOneTouchExpandable(false);
		setEnabled(false);

		setLeftComponent(getDriverPanel());
		setRightComponent(getSchedulePanel());
	}

	private JPanel getDriverPanel() {
		driverTable = new DriverTable();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(JPanel.TOP_ALIGNMENT);
		
		panel.setMaximumSize(new Dimension());
		
		JLabel label = new JLabel("Drivers");
		label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, LABEL_SIZE));
		label.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JButton add = new JButton("Add Driver");
		add.setAlignmentX(JButton.LEFT_ALIGNMENT);
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				DriverDialog dlg = new DriverDialog(App.getFrame(), "Add Driver", "Add");
				Dimension frmSize = App.getFrame().getSize();
				Point loc = App.getFrame().getLocation();
				
				dlg.setLocation(
						(frmSize.width - dlg.getSize().width) / 2 + loc.x,
						(frmSize.height - dlg.getSize().height) / 2
								+ loc.y);
				dlg.setVisible(true);
				
				if (!dlg.isCancelled()) {
					Driver driver = new Driver(OVER_9000, dlg.getName(), dlg.getPhone());
					
					try {
						CurrentProject.getDriverColl().createUnique(driver);
						CurrentStorage.get().storeDriverList(CurrentProject.get(), CurrentProject.getDriverColl());
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
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(add);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(scroll);

		return panel;
	}

	private JPanel getSchedulePanel() {
		if (drivers.size() >= 1) {
			scheduleTable = new DriverScheduleTable(driverTable.getDriver());
		}
		
		else {
			scheduleTable = new DriverScheduleTable();
		}
		
		JButton schedule = new JButton("Schedule Tour");
		schedule.setAlignmentX(JButton.LEFT_ALIGNMENT);
		schedule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (scheduleTable.getDriver() == null) {
					JOptionPane.showMessageDialog(null,  "Cannot Schedule Tour: No Driver Selected", "Error", JOptionPane.OK_OPTION, new ImageIcon(main.java.memoranda.ui.ExceptionDialog.class.getResource(
				            "/ui/icons/error.png")));
				}
				
				else {
					DriverTourDialog dlg = new DriverTourDialog(App.getFrame());
					Dimension frmSize = App.getFrame().getSize();
					Point loc = App.getFrame().getLocation();
					
					dlg.setLocation(
							(frmSize.width - dlg.getSize().width) / 2 + loc.x,
							(frmSize.height - dlg.getSize().height) / 2
									+ loc.y);
					dlg.setVisible(true);
					
					if (!dlg.isCancelled() && dlg.getTour() != null) {
						scheduleTable.getDriver().addTour(dlg.getTour());
						scheduleTable.addTour(dlg.getTour());
						dlg.getTour().setDriverID(scheduleTable.getDriver().getID());
						
						try {
							CurrentStorage.get().storeDriverList(CurrentProject.get(), CurrentProject.getDriverColl());
							CurrentStorage.get().storeTourList(CurrentProject.get(), CurrentProject.getTourColl());
							scheduleTable.tableChanged();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(JPanel.TOP_ALIGNMENT);
		
		JLabel label = new JLabel("Schedule");
		label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, LABEL_SIZE));
		label.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(scheduleTable);
		scroll.setAlignmentX(JButton.LEFT_ALIGNMENT);

		panel.add(label);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(schedule);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(scroll);

		return panel;
	}

	/**
	 * Refreshes this panel
	 * 
	 * TODO: Is CalendarDate needed? What did it do before? Only usage commented out in original code.
	 * Does this method even do anything, or did it only refresh something related to the Calendar system?
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
