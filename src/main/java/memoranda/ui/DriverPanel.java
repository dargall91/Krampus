package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.ui.table.TableMap;
import main.java.memoranda.util.Util;

/*$Id: AgendaPanel.java,v 1.11 2005/02/15 16:58:02 rawsushi Exp $*/
public class DriverPanel extends JPanel {
	private DriverTable driverTable;
	private DailyItemsPanel parentPanel;
	String gotoTask = null;

	private boolean isActive = true;

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
		GridLayout layout = new GridLayout(1,2);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setLayout(layout);

		driverTable = new DriverTable();
		driverTable.setMaximumSize(new Dimension(800, 800));
		driverTable.setRowHeight(24);

		add(getDriverPanel());
		add(getSchedulePanel());
	}

	private JPanel getDriverPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel label = new JLabel("Drivers");
		label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, 25));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton add = new JButton("Add Driver");
		add.setAlignmentX(Component.LEFT_ALIGNMENT);
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				NewDriverDialog dlg = new NewDriverDialog(App.getFrame());
				Dimension frmSize = App.getFrame().getSize();
				Point loc = App.getFrame().getLocation();
				dlg.setLocation(
						(frmSize.width - dlg.getSize().width) / 2 + loc.x,
						(frmSize.height - dlg.getSize().height) / 2
								+ loc.y);
				dlg.setVisible(true);
				if (!dlg.CANCELLED) {
					System.out.println("Not Cancelled");
				}
			}
		});

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(driverTable);//.add(driverTable);
		scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(add);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(scroll);

		return panel;
	}

	private JPanel getSchedulePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel label = new JLabel("Schedule");
		label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, 25));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(label);

		return panel;
	}

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

	public void setActive(boolean isa) {
		isActive = isa;
	}

	//	void toggleShowActiveOnly_actionPerformed(ActionEvent e) {
	//		Context.put(
	//			"SHOW_ACTIVE_TASKS_ONLY",
	//			new Boolean(ppShowActiveOnlyChB.isSelected()));
	//		/*if (taskTable.isShowActiveOnly()) {
	//			// is true, toggle to false
	//			taskTable.setShowActiveOnly(false);
	//			//showActiveOnly.setToolTipText(Local.getString("Show Active Only"));			
	//		}
	//		else {
	//			// is false, toggle to true
	//			taskTable.setShowActiveOnly(true);
	//			showActiveOnly.setToolTipText(Local.getString("Show All"));			
	//		}*/	    
	//		refresh(CurrentDate.get());
	////		parentPanel.updateIndicators();
	//		//taskTable.updateUI();
	//	}

	//    class PopupListener extends MouseAdapter {
	//
	//        public void mouseClicked(MouseEvent e) {
	//        	System.out.println("mouse clicked!");
	////			if ((e.getClickCount() == 2) && (taskTable.getSelectedRow() > -1))
	////				editTaskB_actionPerformed(null);
	//		}
	//
	//		public void mousePressed(MouseEvent e) {
	//        	System.out.println("mouse pressed!");
	//			maybeShowPopup(e);
	//		}
	//
	//		public void mouseReleased(MouseEvent e) {
	//        	System.out.println("mouse released!");
	//			maybeShowPopup(e);
	//		}
	//
	//		private void maybeShowPopup(MouseEvent e) {
	//			if (e.isPopupTrigger()) {
	//				agendaPPMenu.show(e.getComponent(), e.getX(), e.getY());
	//			}
	//		}
	//
	//    }
}
