package main.java.memoranda.ui;

import java.awt.Component;

import javax.swing.*;

import main.java.memoranda.Route;

/**
 * Helper to make drop down box of Routes more readable
 *
 * @author John Thurstonson
 * @version 04/10/2021
 * <p>
 * References:
 * //https://stackoverflow.com/questions/20155122/is-there-any-way-to-add-objects-to-a-jcombobox-and-assign-a-string-to-be-shown
 */
public class RouteListCellRenderer extends DefaultListCellRenderer {

    /**
     * Create readable format for route drop down box
     */
    public Component getListCellRendererComponent(
        JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Route) {
            value = ((Route) value).getName();
        }
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        return this;
    }
}
