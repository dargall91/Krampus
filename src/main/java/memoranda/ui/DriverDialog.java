package main.java.memoranda.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * DriverDialog is a JDialog that has fields to enter and edit a driver's name and phone number
 * 
 * @author Derek Argall
 * @version 04/05/2020
 */
public class DriverDialog extends JDialog {
	private boolean cancelled;
	private JPanel errorPanel;
	private JTextField nameField;
	private JTextField phoneField;
	private final Dimension BUTTON_SIZE = new Dimension(100, 25);
	private final Dimension HORIZONTAL_GAP = new Dimension(5, 0);
	private final Dimension VERTICAL_GAP = new Dimension(0, 5);
	private final int FIELD_WIDTH = 15;

	/**
	 * Creates a JDialog window that allows the user to add a new Driver to the system
	 * or edit an existing one
	 * 
	 * @param frame The main application Frame
	 * @param title The title for this JDialog
	 * @param posButtonName The name for the positive button
	 */
	public DriverDialog(Frame frame, String title, String posButtonName) {
		super(frame, title);
		try {
			init(posButtonName);
			pack();
		} catch (Exception ex) {
			new ExceptionDialog(ex);
		}
	}
	
	private void init(String posButtonName) throws Exception {
		setModal(true);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		errorPanel = new JPanel();
		errorPanel.setVisible(false);
		
		JLabel errorLabel = new JLabel("All Fields Must Be Filled In");
		errorLabel.setForeground(Color.RED);
		errorLabel.setHorizontalAlignment(JLabel.CENTER);
		
		errorPanel.add(errorLabel);
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(2, 2, 5, 5));
		
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		JLabel phoneLabel = new JLabel("Phone Number:");
		phoneLabel.setHorizontalAlignment(JLabel.RIGHT);
		
		nameField = new JTextField(FIELD_WIDTH);
		phoneField = new JTextField(FIELD_WIDTH);
		
		gridPanel.add(nameLabel);
		gridPanel.add(nameField);
		gridPanel.add(phoneLabel);
		gridPanel.add(phoneField);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		//TODO: OK and Cancel Buttons maximize window
		JButton posButton = new JButton(posButtonName);
		posButton.setHorizontalAlignment(JButton.CENTER);
		posButton.setMaximumSize(BUTTON_SIZE);
		posButton.addActionListener(new ActionListener() {
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
		
		buttonPanel.add(posButton);
		buttonPanel.add(Box.createRigidArea(HORIZONTAL_GAP));
		buttonPanel.add(cancelButton);
		
		panel.add(errorPanel);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(gridPanel);
		panel.add(Box.createRigidArea(VERTICAL_GAP));
		panel.add(buttonPanel);
		
		getContentPane().add(panel);
	}

	private void cancelButton_actionPerformed(ActionEvent e) {
		cancelled = true;
		this.dispose();
	}

	private void posButton_actionPerformed(ActionEvent e) {
		if (getName().equals("") || getPhone().contentEquals("")) {
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
	 * New Driver name getter
	 */
	public String getName() {
		return nameField.getText();
	}
	
	/**
	 * New Driver phone number getter
	 */
	public String getPhone() {
		return phoneField.getText();
	}
	
	/**
	 * Sets the driver name field when editing a Driver
	 * 
	 * @param name The driver's name
	 */
	public void setName(String name) {
		nameField.setText(name);
	}
	
	/**
	 * Sets the driver phone number field when editing a Driver
	 * 
	 * @param phone The driver's phone number
	 */
	public void setPhone(String phone) {
		phoneField.setText(phone);
	}
}