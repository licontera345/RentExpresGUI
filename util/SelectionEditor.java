package com.pinguela.rentexpres.desktop.util;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SelectionEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JCheckBox checkBox = new JCheckBox();

	public SelectionEditor() {
		checkBox.setHorizontalAlignment(JLabel.CENTER);
		checkBox.addItemListener(this);
	}

	@Override
	public Object getCellEditorValue() {
		return checkBox.isSelected();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value instanceof Boolean) {
			checkBox.setSelected((Boolean) value);
		} else {
			checkBox.setSelected(false);
		}
		return checkBox;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		fireEditingStopped();
	}
}