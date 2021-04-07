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
 * BusDialog is a JDialog that has fields to enter and edit a bus's number
 * 
 * @author Derek Argall
 * @version 04/05/2020
 */
public class BusDialog extends JDialog {
	private boolean cancelled;
	private JPanel errorPanel;
	private JTextField numberField;
	private final Dimension BUTTON_SIZE = new Dimension(100, 25);
	private final Dimension HORIZONTAL_GAP = new Dimension(5, 0);
	private final Dimension VERTICAL_GAP = new Dimension(0, 5);
	private final int FIELD_WIDTH = 15;

	/**
	 * Creates a JDialog window that allows the user to add a new Bus to the system
	 * or edit an existing one
	 * 
	 * @param frame The main application Frame
	 * @param title The title for this JDialog
	 * @param posButtonName The name for the positive button
	 */
	public BusDialog(Frame frame, String title, String posButtonName) {
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
		gridPanel.setLayout(new GridLayout(1, 2, 5, 5));
		
		JLabel numberLabel = new JLabel("Number:");
		numberLabel.setHorizontalAlignment(JLabel.RIGHT);
		
		numberField = new JTextField(FIELD_WIDTH);
		
		gridPanel.add(numberLabel);
		gridPanel.add(numberField);
		
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
		if (getNumber() == 0) {
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
	 * New Bus number getter
	 */
	public int getNumber() {
		return Integer.parseInt(numberField.getText());
	}
	
	/**
	 * Sets the bus number field when editing a Bus
	 * 
	 * @param number The bus's number
	 */
	public void setNumber(int number) {
		numberField.setText(Integer.toString(number));
	}
}