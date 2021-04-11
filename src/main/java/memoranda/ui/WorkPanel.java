package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import main.java.memoranda.util.Context;
import main.java.memoranda.util.Local;

/**
 * 
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

/*$Id: WorkPanel.java,v 1.9 2004/04/05 10:05:44 alexeya Exp $*/
public class WorkPanel extends JPanel {
	BorderLayout borderLayout1 = new BorderLayout();
	JToolBar toolBar = new JToolBar();
	JPanel panel = new JPanel();
	CardLayout cardLayout1 = new CardLayout();

	public JButton notesB = new JButton();
	public DailyItemsPanel dailyItemsPanel = new DailyItemsPanel(this);
	public RouteMapPanel filesPanel = new RouteMapPanel();
	public JButton driverB = new JButton();
	//public JButton tasksB = new JButton();
	public JButton busesB = new JButton();
	public JButton toursB = new JButton();
	public JButton filesB = new JButton();
	JButton currentB = null;
	Border border1;

	public WorkPanel() {
		try {
			jbInit();
		} catch (Exception ex) {
			new ExceptionDialog(ex);
		}
	}

	void jbInit() throws Exception {
		border1 =
			BorderFactory.createCompoundBorder(
				BorderFactory.createBevelBorder(
					BevelBorder.LOWERED,
					Color.white,
					Color.white,
					new Color(124, 124, 124),
					new Color(178, 178, 178)),
				BorderFactory.createEmptyBorder(0, 2, 0, 0));

		this.setLayout(borderLayout1);
		toolBar.setOrientation(JToolBar.VERTICAL);
		toolBar.setBackground(Color.white);

		toolBar.setBorderPainted(false);
		toolBar.setFloatable(false);
		panel.setLayout(cardLayout1);

		driverB.setBackground(Color.white);
		driverB.setMaximumSize(new Dimension(60, 80));
		driverB.setMinimumSize(new Dimension(30, 30));

		driverB.setFont(new java.awt.Font("Dialog", 1, 10));
		driverB.setPreferredSize(new Dimension(50, 50));
		driverB.setBorderPainted(false);
		driverB.setContentAreaFilled(false);
		driverB.setFocusPainted(false);
		driverB.setHorizontalTextPosition(SwingConstants.CENTER);
		driverB.setText(Local.getString("Drivers"));
		driverB.setVerticalAlignment(SwingConstants.TOP);
		driverB.setVerticalTextPosition(SwingConstants.BOTTOM);
		driverB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				driverB_actionPerformed(e);
			}
		});
		driverB.setIcon(
			new ImageIcon(
				main.java.memoranda.ui.AppFrame.class.getResource(
					"/ui/icons/driver.png")));
		driverB.setOpaque(false);
		driverB.setMargin(new Insets(0, 0, 0, 0));
		driverB.setSelected(true);

		toursB.setBackground(Color.white);
		toursB.setMaximumSize(new Dimension(60, 80));
		toursB.setMinimumSize(new Dimension(30, 30));

		toursB.setFont(new java.awt.Font("Dialog", 1, 10));
		toursB.setPreferredSize(new Dimension(50, 50));
		toursB.setBorderPainted(false);
		toursB.setContentAreaFilled(false);
		toursB.setFocusPainted(false);
		toursB.setHorizontalTextPosition(SwingConstants.CENTER);
		toursB.setText(Local.getString("Tours"));
		toursB.setVerticalAlignment(SwingConstants.TOP);
		toursB.setVerticalTextPosition(SwingConstants.BOTTOM);
		toursB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toursB_actionPerformed(e);
			}
		});
		toursB.setIcon(
			new ImageIcon(
				main.java.memoranda.ui.AppFrame.class.getResource(
					"/ui/icons/events.png")));
		toursB.setOpaque(false);
		toursB.setMargin(new Insets(0, 0, 0, 0));
		//eventsB.setSelected(true);

		busesB.setSelected(true);
		busesB.setFont(new java.awt.Font("Dialog", 1, 10));
		busesB.setMargin(new Insets(0, 0, 0, 0));
		busesB.setIcon(
			new ImageIcon(
				main.java.memoranda.ui.AppFrame.class.getResource(
					"/ui/icons/tasks.png")));
		busesB.setVerticalTextPosition(SwingConstants.BOTTOM);
		busesB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				busB_actionPerformed(e);
			}
		});
		busesB.setVerticalAlignment(SwingConstants.TOP);
		busesB.setText(Local.getString("Buses"));
		busesB.setHorizontalTextPosition(SwingConstants.CENTER);
		busesB.setFocusPainted(false);
		busesB.setBorderPainted(false);
		busesB.setContentAreaFilled(false);
		busesB.setPreferredSize(new Dimension(50, 50));
		busesB.setMinimumSize(new Dimension(30, 30));
		busesB.setOpaque(false);
		busesB.setMaximumSize(new Dimension(60, 80));
		busesB.setBackground(Color.white);

		notesB.setFont(new java.awt.Font("Dialog", 1, 10));
		notesB.setBackground(Color.white);
		notesB.setBorder(null);
		notesB.setMaximumSize(new Dimension(60, 80));
		notesB.setMinimumSize(new Dimension(30, 30));
		notesB.setOpaque(false);
		notesB.setPreferredSize(new Dimension(60, 50));
		notesB.setBorderPainted(false);
		notesB.setContentAreaFilled(false);
		notesB.setFocusPainted(false);
		notesB.setHorizontalTextPosition(SwingConstants.CENTER);
		notesB.setText(Local.getString("Notes"));
		notesB.setVerticalAlignment(SwingConstants.TOP);
		notesB.setVerticalTextPosition(SwingConstants.BOTTOM);
		notesB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notesB_actionPerformed(e);
			}
		});
		notesB.setIcon(
			new ImageIcon(
				main.java.memoranda.ui.AppFrame.class.getResource(
					"/ui/icons/notes.png")));
		notesB.setMargin(new Insets(0, 0, 0, 0));
		notesB.setSelected(true);
		this.setPreferredSize(new Dimension(1073, 300));

		filesB.setSelected(true);
		filesB.setMargin(new Insets(0, 0, 0, 0));
		filesB.setIcon(
			new ImageIcon(
				main.java.memoranda.ui.AppFrame.class.getResource(
					"/ui/icons/map_icon.png")));
		filesB.setVerticalTextPosition(SwingConstants.BOTTOM);
		filesB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filesB_actionPerformed(e);
			}
		});
		filesB.setFont(new java.awt.Font("Dialog", 1, 10));
		filesB.setVerticalAlignment(SwingConstants.TOP);
		filesB.setText(Local.getString("Map"));
		filesB.setHorizontalTextPosition(SwingConstants.CENTER);
		filesB.setFocusPainted(false);
		filesB.setBorderPainted(false);
		filesB.setContentAreaFilled(false);
		filesB.setPreferredSize(new Dimension(50, 50));
		filesB.setMinimumSize(new Dimension(30, 30));
		filesB.setOpaque(false);
		filesB.setMaximumSize(new Dimension(60, 80));
		filesB.setBackground(Color.white);
		this.add(toolBar, BorderLayout.WEST);
		this.add(panel, BorderLayout.CENTER);
		panel.add(dailyItemsPanel, "DAILYITEMS");
		panel.add(filesPanel, "FILES");
		toolBar.add(driverB, null);
		toolBar.add(toursB, null);
		toolBar.add(busesB, null);
		toolBar.add(notesB, null);
		toolBar.add(filesB, null);
		currentB = driverB;
		// Default blue color
		currentB.setBackground(new Color(215, 225, 250));
		currentB.setOpaque(true);

		toolBar.setBorder(null);
		panel.setBorder(null);
		dailyItemsPanel.setBorder(null);
		filesPanel.setBorder(null);

	}

	public void selectPanel(String pan) {
		if (pan != null) {
			if (pan.equals("NOTES"))
				notesB_actionPerformed(null);
			else if (pan.equals("BUSES"))
				busB_actionPerformed(null);
			else if (pan.equals("EVENTS"))
				toursB_actionPerformed(null);
			else if (pan.equals("FILES"))
				filesB_actionPerformed(null);
		}
	}

	public void driverB_actionPerformed(ActionEvent e) {
		cardLayout1.show(panel, "DAILYITEMS");
		dailyItemsPanel.selectPanel("DRIVERS");
		setCurrentButton(driverB);
		Context.put("CURRENT_PANEL", "DRIVERS");
	}

	public void notesB_actionPerformed(ActionEvent e) {
		cardLayout1.show(panel, "DAILYITEMS");
		dailyItemsPanel.selectPanel("NOTES");
		setCurrentButton(notesB);
		Context.put("CURRENT_PANEL", "NOTES");
	}

	public void busB_actionPerformed(ActionEvent e) {
		cardLayout1.show(panel, "DAILYITEMS");
		dailyItemsPanel.selectPanel("BUSES");
		setCurrentButton(busesB);
		Context.put("CURRENT_PANEL", "BUSES");
	}

	public void toursB_actionPerformed(ActionEvent e) {
		cardLayout1.show(panel, "DAILYITEMS");
		dailyItemsPanel.selectPanel("TOURS");
		setCurrentButton(toursB);
		Context.put("CURRENT_PANEL", "TOURS");
	}

	public void filesB_actionPerformed(ActionEvent e) {
		cardLayout1.show(panel, "FILES");
		setCurrentButton(filesB);
		Context.put("CURRENT_PANEL", "FILES");
	}

	void setCurrentButton(JButton cb) {
		currentB.setBackground(Color.white);
		currentB.setOpaque(false);
		currentB = cb;
		// Default color blue
		currentB.setBackground(new Color(215, 225, 250));
		currentB.setOpaque(true);
	}
}