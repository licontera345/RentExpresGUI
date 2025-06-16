package com.pinguela.rentexpres.desktop.util;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SelectionRenderer extends JCheckBox implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	public SelectionRenderer() {
		setHorizontalAlignment(JLabel.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value instanceof Boolean) {
			setSelected((Boolean) value);
		} else {
			setSelected(false);
		}
		if (isSelected) {
			setBackground(table.getSelectionBackground());
		} else {
			setBackground(table.getBackground());
		}
		return this;
	}
}