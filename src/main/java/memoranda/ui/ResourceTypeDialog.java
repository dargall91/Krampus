package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import main.java.memoranda.util.Local;

/*$Id: ResourceTypeDialog.java,v 1.11 2004/07/01 14:44:10 pbielen Exp $*/
public class ResourceTypeDialog extends JDialog {
    //Border border2;
    //TitledBorder titledBorder2;
    public String ext = "";
    JButton cancelB = new JButton();
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JLabel header = new JLabel();
    JPanel dialogTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton okB = new JButton();
    //JPanel mPanel = new JPanel(new BorderLayout());
    ResourceTypePanel areaPanel = new ResourceTypePanel();
    boolean CANCELLED = true;

    public ResourceTypeDialog(JFrame frame, String title) {
        super(frame, title, true);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    void jbInit() throws Exception {
        this.setResizable(false);
        dialogTitlePanel.setBackground(Color.WHITE);
        dialogTitlePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        header.setFont(new java.awt.Font("Dialog", 0, 20));
        header.setForeground(new Color(0, 0, 124));
        header.setText(Local.getString("Resource type"));
        header.setIcon(new ImageIcon(main.java.memoranda.ui.ResourceTypeDialog.class.getResource(
                "/ui/icons/resource48.png")));
        dialogTitlePanel.add(header);
        this.getContentPane().add(dialogTitlePanel, BorderLayout.NORTH);

        //mPanel.add(areaPanel, BorderLayout.CENTER);
        this.getContentPane().add(areaPanel, BorderLayout.CENTER);

        cancelB.addActionListener(e -> cancelB_actionPerformed(e));
        cancelB.setText(Local.getString("Cancel"));
        cancelB.setPreferredSize(new Dimension(100, 26));
        cancelB.setMinimumSize(new Dimension(100, 26));
        cancelB.setMaximumSize(new Dimension(100, 26));


        okB.setMaximumSize(new Dimension(100, 26));
        okB.setMinimumSize(new Dimension(100, 26));
        okB.setPreferredSize(new Dimension(100, 26));
        okB.setText(Local.getString("Ok"));
        okB.addActionListener(e -> okB_actionPerformed(e));
        this.getRootPane().setDefaultButton(okB);
        buttonsPanel.add(okB, null);
        buttonsPanel.add(cancelB, null);

        this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }


    void cancelB_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    void okB_actionPerformed(ActionEvent e) {
        CANCELLED = false;
        this.dispose();
    }

    public JList getTypesList() {
        return areaPanel.typesList;
    }


}