package com.pinguela.rentexpres.desktop.renderer;

import static com.pinguela.rentexpres.desktop.util.AppIcons.DELETE;
import static com.pinguela.rentexpres.desktop.util.AppIcons.EDIT;
import static com.pinguela.rentexpres.desktop.util.AppIcons.VIEW;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class UsuarioActionsCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	private static final Color ROW_ALT = new Color(245, 248, 252);

	private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
	private final JButton btnView = new JButton(VIEW);
	private final JButton btnEdit = new JButton(EDIT);
	private final JButton btnDelete = new JButton(DELETE);

	public UsuarioActionsCellRenderer() {
		for (JButton b : new JButton[] { btnView, btnEdit, btnDelete }) {
			b.setBorderPainted(false);
			b.setFocusPainted(false);
			b.setContentAreaFilled(false);
			panel.add(b);
		}
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (!isSelected) {
			Color alt = ROW_ALT;
			setBackground(row % 2 == 0 ? UIManager.getColor("Table.background") : alt);
			panel.setBackground(getBackground());
		} else {
			panel.setBackground(table.getSelectionBackground());
		}

		return panel;
	}
}
