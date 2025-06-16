package com.pinguela.rentexpres.desktop.renderer;

import static com.pinguela.rentexpres.desktop.util.AppIcons.DELETE;
import static com.pinguela.rentexpres.desktop.util.AppIcons.EDIT;
import static com.pinguela.rentexpres.desktop.util.AppIcons.VIEW;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.pinguela.rentexpres.desktop.dialog.ReservaDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ReservaEditDialog;
import com.pinguela.rentexpres.desktop.model.ReservaSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.ReservaService;

public class ReservaActionsCellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;

	private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
	private final JButton btnVer = iconButton(VIEW, "Ver");
	private final JButton btnEdit = iconButton(EDIT, "Editar");
	private final JButton btnDel = iconButton(DELETE, "Borrar");

	private ReservaDTO reservaActual;

	public ReservaActionsCellEditor(Frame frame, ReservaService service) {
		panel.add(btnVer);
		panel.add(btnEdit);
		panel.add(btnDel);

		btnVer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (reservaActual != null) {
					new ReservaDetailDialog(frame, reservaActual).setVisible(true);
				}
				fireEditingStopped();
			}
		});

		btnEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (reservaActual != null) {
					ReservaEditDialog dlg = new ReservaEditDialog(frame, reservaActual);
					dlg.setVisible(true);
					if (dlg.isConfirmed()) {
						try {
							service.update(dlg.getReserva());
						} catch (RentexpresException ex) {
							SwingUtils.showError(frame, ex.getMessage());
						}
					}
				}
				fireEditingStopped();
			}
		});

		btnDel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (reservaActual != null
						&& SwingUtils.showConfirm(frame, "¿Eliminar reserva " + reservaActual.getId() + "?",
								"Confirmar borrado") == JOptionPane.YES_OPTION) {
					try {
						service.delete(reservaActual.getId());
					} catch (RentexpresException ex) {
						SwingUtils.showError(frame, " No se pudo borrar: " + ex.getMessage());
					}
				}
				fireEditingStopped();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (table.getModel() instanceof ReservaSearchTableModel) {
			ReservaSearchTableModel model = (ReservaSearchTableModel) table.getModel();
			reservaActual = model.getReservaAt(row);
		}
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject e) {
		return true;
	}

	private static JButton iconButton(ImageIcon icon, String tooltip) {
		JButton b = new JButton(icon);
		b.setToolTipText(tooltip);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		return b;
	}
}
