package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.text.Document;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Project;
import main.java.memoranda.ProjectListener;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;

/*$Id: SearchPanel.java,v 1.5 2004/04/05 10:05:44 alexeya Exp $*/
public class SearchPanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JScrollPane scrollPane = new JScrollPane();
    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    JTextField searchField = new JTextField();
    JPanel jPanel2 = new JPanel();
    BorderLayout borderLayout3 = new BorderLayout();
    Border border1;
    TitledBorder titledBorder1;
    JPanel jPanel3 = new JPanel();
    JPanel jPanel4 = new JPanel();
    JCheckBox caseSensCB = new JCheckBox();
    JCheckBox regexpCB = new JCheckBox();
    JCheckBox wholeWCB = new JCheckBox();
    JButton searchB = new JButton();
    BorderLayout borderLayout4 = new BorderLayout();
    BorderLayout borderLayout5 = new BorderLayout();
    //JProgressBar progressBar = new JProgressBar();

    public SearchPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    void jbInit() throws Exception {
        border1 = BorderFactory.createEmptyBorder(2, 2, 2, 2);

        titledBorder1 = new TitledBorder(BorderFactory.createEmptyBorder(), Local.getString("Search") + ":");

        this.setLayout(borderLayout1);

        jPanel1.setLayout(borderLayout2);
        jPanel2.setLayout(borderLayout3);
        jPanel2.setBorder(titledBorder1);
        titledBorder1.setTitleFont(new java.awt.Font("Dialog", Font.BOLD, 11));
        searchField.setFont(new java.awt.Font("Dialog", Font.BOLD, 10));
        searchField.addCaretListener(e -> searchField_caretUpdate(e));
        jPanel3.setLayout(borderLayout5);
        caseSensCB.setText(Local.getString("Case sensitive"));
        caseSensCB.setFont(new java.awt.Font("Dialog", Font.BOLD, 10));
        caseSensCB.setMargin(new Insets(0, 0, 0, 0));

        regexpCB.setFont(new java.awt.Font("Dialog", Font.BOLD, 10));
        regexpCB.setMargin(new Insets(0, 0, 0, 0));
        regexpCB.setText(Local.getString("Regular expressions"));
        wholeWCB.setText(Local.getString("Whole words only"));
        wholeWCB.setMargin(new Insets(0, 0, 0, 0));
        wholeWCB.setFont(new java.awt.Font("Dialog", Font.BOLD, 10));
        searchB.setEnabled(false);
        searchB.setFont(new java.awt.Font("Dialog", Font.BOLD, 11));
        searchB.setMaximumSize(new Dimension(72, 25));
        searchB.setMinimumSize(new Dimension(2, 25));
        searchB.setPreferredSize(new Dimension(70, 25));
        searchB.setMargin(new Insets(0, 0, 0, 0));
        searchB.setText(Local.getString("Search"));
        searchB.addActionListener(e -> searchB_actionPerformed(e));
        jPanel4.setLayout(borderLayout4);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(jPanel1, BorderLayout.NORTH);
        jPanel1.add(jPanel2, BorderLayout.NORTH);
        jPanel2.add(searchField, BorderLayout.CENTER);
        jPanel1.add(jPanel3, BorderLayout.CENTER);
        jPanel3.add(jPanel4, BorderLayout.NORTH);
        jPanel4.add(caseSensCB, BorderLayout.SOUTH);
        jPanel4.add(wholeWCB, BorderLayout.NORTH);
        jPanel4.add(regexpCB, BorderLayout.CENTER);
        jPanel3.add(searchB, BorderLayout.SOUTH);

    }

    Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);

    void searchB_actionPerformed(ActionEvent e) {
        Cursor cur = App.getFrame().getCursor();
        App.getFrame().setCursor(waitCursor);
        doSearch();
        App.getFrame().setCursor(cur);
    }

    void searchField_caretUpdate(CaretEvent e) {
        searchB.setEnabled(searchField.getText().length() > 0);
    }


    void doSearch() {
        Pattern pattern;
        //this.add(progressBar, BorderLayout.SOUTH);
        int flags = Pattern.DOTALL;
        if (!caseSensCB.isSelected()) {
            flags = flags + Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE;
        }
        String _find = searchField.getText();
        if (!regexpCB.isSelected()) {
            _find = "\\Q" + _find + "\\E";
        }
        if (wholeWCB.isSelected()) {
            _find = "[\\s\\p{Punct}]" + _find + "[\\s\\p{Punct}]";
        }
        try {
            pattern = Pattern.compile(_find, flags);
        } catch (Exception ex) {
            new ExceptionDialog(ex, "Error in regular expression", "Check the regular expression syntax");
            return;
        }
    }

}