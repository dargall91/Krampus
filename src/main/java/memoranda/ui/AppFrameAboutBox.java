package main.java.memoranda.ui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import main.java.memoranda.util.Local;

/**
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

/*$Id: AppFrame_AboutBox.java,v 1.13 2005/11/09 22:38:07 alexeya Exp $*/
public class AppFrameAboutBox extends JDialog implements ActionListener {

    JButton button1 = new JButton();
    //JLabel imageLabel = new JLabel();
    JLabel lblText = new JLabel();

    String product = "Version: " + App.getVersionInfo() + " (Build: " + App.getBuildInfo() + ")";
    String copyright = "Copyright (c) 2003, 2004 Memoranda team";
    String url = App.getWebsiteUrl();
    String developersHead = Local.getString("Developers") + ":";
    String[] developers = {
        "Alex Alishevskikh (alexeya@users.sourceforge.net)",
        "Patrick Bielen (bielen@stafa.nl)", "Ryan Ho (rawsushi@users.sourceforge.net)",
        "Ivan Ribas (ivanrise@users.sourceforge.net)",
        "Jyrki Velhonoja (velhonoja@kapsi.fi>)",
        "Jeremy Whitlock (jwhitlock@starprecision.com)",
        "Derek Argall",
        "Brian Pape",
        "Chris Bodeva",
        "Kevin Dolan",
        "John Thurston"
    };
    String othersHead = Local.getString("Other contributors") + ":";
    String[] others = {
        "Thomas Chuffart (informatique@pierrelouiscarlier.fr)",
        "Willy Dobe (wdobe@gmx.de)",
        "Yunjie Liu (liu-610@163.com)",
        "Kenneth J. Pouncey (kjpou@pt.lu)",
        "Michael Radtke (mradtke@abigale.de)",
        "Carel-J Rischmuller (carel-j.rischmuller@epiuse.com)",
        "Milena Vitali-Charewicz (milo22370@yahoo.com)",
        "Toru Watanabe (t-wata@cablenet.ne.jp)"
    };

    JLayeredPane layeredPane;
    ImageIcon image;
    JLabel imgLabel;

    public AppFrameAboutBox(Frame parent) {
        super(parent);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSize(400, 500);
    }

    //Component initialization
    private void jbInit() throws Exception {
        String text = "<html>";
        text += "<b>" + product + "</b><br><br>";
        text += copyright + "<br>" + url + "<br><br>";
        text += "<b>" + developersHead + "</b><br>";
        for (String developer : developers) {
            text += developer + "<br>";
        }
        text += "<br><b>" + othersHead + "</b><br>";
        for (String other : others) {
            text += other + "<br>";
        }

        text += "</html>";

        image = new ImageIcon(Objects.requireNonNull(AppFrameAboutBox.class.getResource("/ui/memoranda.png")));
        this.setTitle(Local.getString("About Memoranda"));
        setResizable(false);
        // Initialize Objects
        lblText.setFont(new java.awt.Font("Dialog", 0, 11));
        lblText.setText(text);
        lblText.setBounds(10, 55, 300, 400);


        button1.setText(Local.getString("Ok"));
        button1.setBounds(150, 415, 95, 30);
        button1.addActionListener(this);
        button1.setPreferredSize(new Dimension(95, 30));
        button1.setBackground(new Color(69, 125, 186));
        button1.setForeground(Color.white);
        layeredPane = getLayeredPane();
        //layeredPane.setPreferredSize(new Dimension(300, 300));
        imgLabel = new JLabel(image);
        imgLabel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
        layeredPane.add(imgLabel, Integer.valueOf(1));
        layeredPane.add(lblText, Integer.valueOf(2));
        layeredPane.add(button1, Integer.valueOf(2));
        this.getContentPane().setBackground(new Color(251, 197, 63));
    }

    //Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cancel();
        }
        super.processWindowEvent(e);
    }

    //Close the dialog
    void cancel() {
        dispose();
    }

    //Close the dialog on a button event
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            cancel();
        }
    }
}
