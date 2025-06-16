package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import com.pinguela.rentexpres.desktop.model.AlquilerSearchTableModel;
import com.pinguela.rentexpres.model.AlquilerDTO;

public class AlquilerTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final Color ROW_ALT = new Color(245, 248, 252);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		if (value instanceof Number)
			setHorizontalAlignment(SwingConstants.RIGHT);
		else
			setHorizontalAlignment(SwingConstants.CENTER);

		if (!isSelected) {
			setBackground(row % 2 == 0 ? UIManager.getColor("Table.background") : ROW_ALT);
		}

		AlquilerDTO dto = ((AlquilerSearchTableModel) table.getModel())
				.getAlquilerAt(table.convertRowIndexToModel(row));

		if (dto.getIdEstadoAlquiler() != null && dto.getIdEstadoAlquiler() == 4) {
			setForeground(Color.RED);
		} else {
			setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
		}
		return this;
	}
}
