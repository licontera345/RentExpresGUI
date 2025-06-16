package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class UsuarioTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final Color ROW_ALT = new Color(245, 248, 252);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (!isSelected) {
			setBackground(row % 2 == 0 ? UIManager.getColor("Table.background") : ROW_ALT);
		}

		if (value instanceof Number) {
			setHorizontalAlignment(SwingConstants.RIGHT);
		} else {
			setHorizontalAlignment(SwingConstants.LEFT);
		}

		setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

		return this;
	}
}
