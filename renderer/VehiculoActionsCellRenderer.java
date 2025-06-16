package com.pinguela.rentexpres.desktop.renderer;

import static com.pinguela.rentexpres.desktop.util.AppIcons.DELETE;
import static com.pinguela.rentexpres.desktop.util.AppIcons.EDIT;
import static com.pinguela.rentexpres.desktop.util.AppIcons.VIEW;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Renderizador de la columna “Acciones” para la tabla de Vehículo. Muestra los
 * botones Ver, Editar y Borrar con sus iconos correspondientes. Sigue el mismo
 * patrón que AlquilerActionsCellRenderer y ReservaActionsCellRenderer.
 */
public class VehiculoActionsCellRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	private final JButton btnVer = iconButton(VIEW, "Ver");
	private final JButton btnEdit = iconButton(EDIT, "Editar");
	private final JButton btnDel = iconButton(DELETE, "Borrar");

	public VehiculoActionsCellRenderer() {
		super(new FlowLayout(FlowLayout.CENTER, 6, 0));
		setOpaque(true);
		add(btnVer);
		add(btnEdit);
		add(btnDel);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		return this;
	}

	private static JButton iconButton(ImageIcon ico, String tooltip) {
		JButton b = new JButton(ico);
		b.setToolTipText(tooltip);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		return b;
	}
}
