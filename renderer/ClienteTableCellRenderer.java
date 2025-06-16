package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class ClienteTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final Color ROW_ALT = new Color(245, 248, 252);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		// Alineación: números a la derecha, texto al centro
		setHorizontalAlignment(value instanceof Number ? SwingConstants.RIGHT : SwingConstants.CENTER);

		// Fondo estilo “zebra”
		if (!isSelected) {
			setBackground(row % 2 == 0 ? UIManager.getColor("Table.background") : ROW_ALT);
		}

		return this;
	}
}
