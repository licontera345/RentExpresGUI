package com.pinguela.rentexpres.desktop.renderer;

import static com.pinguela.rentexpres.desktop.util.AppIcons.DELETE;
import static com.pinguela.rentexpres.desktop.util.AppIcons.EDIT;
import static com.pinguela.rentexpres.desktop.util.AppIcons.VIEW;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ClienteActionsCellRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	private final JButton btnView = iconButton(VIEW, "Ver");
	private final JButton btnEdit = iconButton(EDIT, "Editar");
	private final JButton btnDel = iconButton(DELETE, "Borrar");

	public ClienteActionsCellRenderer() {
		setOpaque(true);
		add(btnView);
		add(btnEdit);
		add(btnDel);
	}

	@Override
	public Component getTableCellRendererComponent(JTable tbl, Object v, boolean sel, boolean focus, int row, int col) {
		setBackground(sel ? tbl.getSelectionBackground() : tbl.getBackground());
		return this;
	}

	private static JButton iconButton(ImageIcon ico, String tip) {
		JButton b = new JButton(ico);
		b.setToolTipText(tip);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		return b;
	}
}
