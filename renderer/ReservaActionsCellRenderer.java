package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import static com.pinguela.rentexpres.desktop.util.AppIcons.*;

public class ReservaActionsCellRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	private final JButton btnVer = iconButton(VIEW, "Ver");
	private final JButton btnEdit = iconButton(EDIT, "Editar");
	private final JButton btnDel = iconButton(DELETE, "Borrar");

	public ReservaActionsCellRenderer() {
		setOpaque(true);
		add(btnVer);
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
