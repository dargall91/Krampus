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
import main.java.memoranda.util.Local;

/*$Id: StickerDialog.java,v 1.5 2004/10/07 21:31:33 ivanrise Exp $*/
public class NewDriverDialog extends JDialog {
	private boolean cancelled;
	private JLabel errorLabel;
	private JTextField nameField;
	private JTextField phoneField;
	private final Dimension BUTTON_SIZE = new Dimension(100, 25);

	/**
	 * Creates a JDialog window that allows the user to add a new Driver to the system
	 * 
	 * @param frame The main application Frame
	 */
	public NewDriverDialog(Frame frame) {
		super(frame, Local.getString("Sticker"), true);
		try {
			init();
			pack();
		} catch (Exception ex) {
			new ExceptionDialog(ex);
		}
	}
	
	private void init() throws Exception {
		setTitle("Add Driver");
		//setResizable(false);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel errorPanel = new JPanel();
		
		errorLabel = new JLabel("All Fields Must Be Filled In");
		errorLabel.setForeground(Color.RED);
		errorLabel.setVisible(false);
		errorLabel.setHorizontalAlignment(JLabel.CENTER);
		
		errorPanel.add(errorLabel);
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(2, 2, 5, 5));
		
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		JLabel phoneLabel = new JLabel("Phone Number:");
		phoneLabel.setHorizontalAlignment(JLabel.RIGHT);
		
		nameField = new JTextField(15);
		phoneField = new JTextField(15);
		
		gridPanel.add(nameLabel);
		gridPanel.add(nameField);
		gridPanel.add(phoneLabel);
		gridPanel.add(phoneField);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		JButton okButton = new JButton("OK");
		okButton.setHorizontalAlignment(JButton.CENTER);
		okButton.setMaximumSize(BUTTON_SIZE);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okButton_actionPerformed(e);
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
		
		buttonPanel.add(okButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonPanel.add(cancelButton);
		
		panel.add(errorPanel);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(gridPanel);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(buttonPanel);
		
		getContentPane().add(panel);
	}

	private void cancelButton_actionPerformed(ActionEvent e) {
		cancelled = true;
		this.dispose();
	}

	private void okButton_actionPerformed(ActionEvent e) {
		if (getName().equals("") || getPhone().contentEquals("")) {
			errorLabel.setVisible(true);
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
}