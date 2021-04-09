package main.java.memoranda.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.java.memoranda.Tour;

/**
 * BusTourDialog is a JDialog that allows the user to schedule a Bus for a Tour
 * 
 * @author Derek Argall
 * @version 04/09/2020
 */
public class BusTourDialog extends JDialog {
	private boolean cancelled;
	private JPanel errorPanel;
	private BusTourDialogTable tourTable;
	private final Dimension BUTTON_SIZE = new Dimension(100, 25);
	private final Dimension HORIZONTAL_GAP = new Dimension(5, 0);
	private final Dimension VERTICAL_GAP = new Dimension(0, 5);
	private int bus;

	/**
	 * Creates a JDialog window that allows the user to add a new Bus to the system
	 * or edit an existing one
	 * 
	 * @param frame The main application Frame
	 * @param title The title for this JDialog
	 * @param posButtonName The name for the positive button
	 */
	public BusTourDialog(Frame frame, int bus) {
		super(frame);
		try {
			this.bus = bus;
			init();
			pack();
		} catch (Exception ex) {
			new ExceptionDialog(ex);
		}
	}
	
	private void init() throws Exception {
		setModal(true);
		setTitle("Schedule Tour");
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel namePanel = new JPanel();
		JLabel busNo = new JLabel("Schedule a Tour for Bus No. " + bus);
		busNo.setHorizontalAlignment(JLabel.CENTER);
		
		namePanel.add(busNo);
		
		errorPanel = new JPanel();
		errorPanel.setVisible(false);
		
		JLabel errorLabel = new JLabel("No Tour Selected");
		errorLabel.setForeground(Color.RED);
		errorLabel.setHorizontalAlignment(JLabel.CENTER);
		
		errorPanel.add(errorLabel);

		JScrollPane scroll = new JScrollPane();
		tourTable = new BusTourDialogTable();
		scroll.setViewportView(tourTable);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		//TODO: OK and Cancel Buttons maximize window
		JButton scheduleButton = new JButton("Schedule");
		scheduleButton.setHorizontalAlignment(JButton.CENTER);
		scheduleButton.setMaximumSize(BUTTON_SIZE);
		scheduleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				posButton_actionPerformed(e);
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setHorizontalAlignment(JButton.CENTER);
		cancelButton.setMaximumSize(BUTTON_SIZE);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});
		
		buttonPanel.add(scheduleButton);
		buttonPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		buttonPanel.add(cancelButton);
		
		panel.add(namePanel);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(errorPanel);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(scroll);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(buttonPanel);
		
		getContentPane().add(panel);
	}

	private void cancelButton_actionPerformed(ActionEvent e) {
		cancelled = true;
		this.dispose();
	}

	private void posButton_actionPerformed(ActionEvent e) {
		if (tourTable.getSelectionModel().isSelectionEmpty()) {
			errorPanel.setVisible(true);
			pack();
			validate();
		}
		
		else {
			cancelled = false;
			this.dispose();
		}
	}
	
	/**
	 * Checks if this JDialog was exited via the cancel button or not
	 * 
	 * @return True if exited via Cancel button, false if not
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Gets the tour selected in the BusTourDialogTable
	 */
	public Tour getTour() {
		return tourTable.getTour();
	}
}