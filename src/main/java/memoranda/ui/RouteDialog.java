package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.RouteColl;
import main.java.memoranda.util.Local;


/**
 * Dialog box for the creation of a new Route.
 *
 * @author Chris Boveda
 * @version 2021-04-11
 */
public class RouteDialog extends JDialog {
    private JLabel header = new JLabel();
    private JTextField nameField = new JTextField();
    private JLabel lblText = new JLabel();
    private JButton okB = new JButton();
    private JButton cancelB = new JButton();
    private GridBagConstraints gbc;
    private JPanel topPanel = new JPanel(new BorderLayout());
    private JPanel bottomPanel = new JPanel(new BorderLayout());
    private JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel routePanel = new JPanel(new GridBagLayout());
    private JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    private boolean complete = false;
    private int error;
    private RouteColl routes;


    /**
     * Ctor for RouteDialog.
     *
     * @param frame The parent frame
     * @param title Title of the dialog box
     */
    public RouteDialog(Frame frame, String title) {
        super(frame, title, true);
        try {
            error = jbInit();
            pack();
        } catch (Exception e) {
            new ExceptionDialog(e);
        }
    }


    /**
     * Initializes the dialog box.
     *
     * @return 0 if successful
     */
    private int jbInit() {
        routes = CurrentProject.getRouteColl();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setResizable(false);
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        header.setForeground(new Color(0, 0, 124));
        header.setText("Route");
        headerPanel.add(header);

        lblText.setText("Name");
        lblText.setMinimumSize(new Dimension(120, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        routePanel.add(lblText, gbc);

        nameField.setMinimumSize(new Dimension(375, 24));
        nameField.setPreferredSize(new Dimension(375, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        routePanel.add(nameField, gbc);

        okB.setMaximumSize(new Dimension(100, 26));
        okB.setMinimumSize(new Dimension(100, 26));
        okB.setPreferredSize(new Dimension(100, 26));
        okB.setText(Local.getString("Ok"));
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        this.getRootPane().setDefaultButton(okB);
        cancelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        cancelB.setText(Local.getString("Cancel"));
        cancelB.setPreferredSize(new Dimension(100, 26));
        cancelB.setMinimumSize(new Dimension(100, 26));
        cancelB.setMaximumSize(new Dimension(100, 26));
        buttonsPanel.add(okB);
        buttonsPanel.add(cancelB);

        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(routePanel, BorderLayout.SOUTH);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        return 0;
    }


    /**
     * OK button pressed.
     *
     * @param e ok button ActionEvent
     */
    private void okButton_actionPerformed(ActionEvent e) {
        if (getName().equals("")) {
            pack();
            validate();
        } else {
            complete = true;
            this.dispose();
        }
    }


    /**
     * Cancel button pressed.
     *
     * @param e cancel button ActionEvent
     */
    private void cancelButton_actionPerformed(ActionEvent e) {
        complete = false;
        this.dispose();
    }


    /**
     * Return if complete or not.
     *
     * @return true if complete
     */
    public boolean isComplete() {
        return complete;
    }


    /**
     * Returns the error code.
     *
     * @return error int
     */
    public int getError() {
        return error;
    }


    /**
     * Returns the nameField text.
     *
     * @return string name
     */
    public String getName() {
        return nameField.getText();
    }

}
