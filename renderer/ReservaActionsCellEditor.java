package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;
import java.util.EventObject;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.ReservaDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ReservaEditDialog;
import com.pinguela.rentexpres.desktop.model.ReservaSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.ReservaService;

public class ReservaActionsCellEditor extends AbstractActionsCellEditor {
        private static final long serialVersionUID = 1L;

        private ReservaDTO reservaActual;

        public ReservaActionsCellEditor(Frame frame, ReservaService service) {
                super();

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

}
