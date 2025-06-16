package com.pinguela.rentexpres.desktop.renderer;

import javax.swing.JTable;

// Renderizador de celda genérico para las columnas de texto en la tabla de Vehículos (filas alternadas en gris/blanco)
public class VehiculoTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (isSelected) {
			c.setBackground(table.getSelectionBackground());
			c.setForeground(table.getSelectionForeground());
		} else {
			c.setBackground(table.getBackground());
			c.setForeground(table.getForeground());
		}

		// Alineación a la derecha para campos numéricos
		if (value instanceof Number) {
			setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		} else {
			setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		}

		return c;
	}
}
