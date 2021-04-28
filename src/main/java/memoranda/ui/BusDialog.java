package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * BusDialog is a JDialog that has fields to enter and edit a bus's number. This dialog will not
 * allow for the user to enter non numeric characters for the Bus's Number
 *
 * @author Derek Argall
 * @version 04/09/2020
 */
public class BusDialog extends JDialog {
    private static final Dimension BUTTON_SIZE = new Dimension(100, 25);
    private static final Dimension HORIZONTAL_GAP = new Dimension(5, 0);
    private static final Dimension VERTICAL_GAP = new Dimension(0, 5);
    private static final int FIELD_WIDTH = 5;
    private static final int NO_NUMBER = -1;
    private boolean cancelled;
    private JPanel errorPanel;
    private JTextField numberField;

    /**
     * Creates a JDialog window that allows the user to add a new Bus to the system or edit an
     * existing one.
     *
     * @param frame         The main application Frame
     * @param title         The title for this JDialog
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

        JLabel errorLabel = new JLabel("Number Field Cannot Be Empty");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);

        errorPanel.add(errorLabel);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(1, 2, 5, 5));

        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setHorizontalAlignment(JLabel.RIGHT);

        numberField = new JTextField(FIELD_WIDTH);
        numberField.addKeyListener(new KeyAdapter() {
            //Do not allow non-numeric characters to be entered into this field
            @Override
            public void keyPressed(KeyEvent e) {
                numberField.setEditable(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || (
                        e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                                || e.getKeyCode() == KeyEvent.VK_DELETE));
            }

            //If the bus number is multiple digits and the first digit is 0, drop the the 0
            @Override
            public void keyReleased(KeyEvent e) {
                if (numberField.getText().length() > 1 && numberField.getText().charAt(0) == '0') {
                    numberField.setText(numberField.getText().substring(1));
                }
            }
        });

        gridPanel.add(numberLabel);
        gridPanel.add(numberField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        //TODO: OK and Cancel Buttons maximize window
        JButton posButton = new JButton(posButtonName);
        posButton.setHorizontalAlignment(JButton.CENTER);
        posButton.setMaximumSize(BUTTON_SIZE);
        posButton.addActionListener(e -> posButton_actionPerformed(e));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setHorizontalAlignment(JButton.CENTER);
        cancelButton.setMaximumSize(BUTTON_SIZE);
        cancelButton.addActionListener(e -> cancelButton_actionPerformed(e));

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
        if (getNumber() <= NO_NUMBER) {
            errorPanel.setVisible(true);
            numberField.setEditable(true);
            pack();
            validate();
        } else {
            cancelled = false;
            this.dispose();
        }
    }

    /**
     * Checks if this JDialog was exited via the cancel button or not.
     *
     * @return True if exited via Cancel button, false if not
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * New Bus number getter.
     *
     * @return The bus's number, or -1 if no number is entered
     */
    public int getNumber() {
        if (numberField.getText().equals("")) {
            return NO_NUMBER;
        }

        return Integer.parseInt(numberField.getText());
    }

    /**
     * Sets the bus number field when editing a Bus.
     *
     * @param number The bus's number
     */
    public void setNumber(int number) {
        numberField.setText(Integer.toString(number));
    }
}