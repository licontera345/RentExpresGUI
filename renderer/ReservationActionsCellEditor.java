package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;
import java.util.EventObject;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.ReservationDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ReservationEditDialog;
import com.pinguela.rentexpres.desktop.model.ReservationSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.ReservationService;

public class ReservationActionsCellEditor extends AbstractActionsCellEditor {
        private static final long serialVersionUID = 1L;

        private ReservationDTO reservationActual;

        public ReservationActionsCellEditor(Frame frame, ReservationService service) {
                super();

		btnView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (reservationActual != null) {
					new ReservationDetailDialog(frame, reservationActual).setVisible(true);
				}
				fireEditingStopped();
			}
		});

		btnEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (reservationActual != null) {
					ReservationEditDialog dlg = new ReservationEditDialog(frame, reservationActual);
					dlg.setVisible(true);
					if (dlg.isConfirmed()) {
						try {
							service.update(dlg.getReservation());
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
				if (reservationActual != null
						&& SwingUtils.showConfirm(frame, "Delete reservation " + reservationActual.getId() + "?",
								"Confirm deletion") == JOptionPane.YES_OPTION) {
					try {
						service.delete(reservationActual.getId());
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
		if (table.getModel() instanceof ReservationSearchTableModel) {
			ReservationSearchTableModel model = (ReservationSearchTableModel) table.getModel();
			reservationActual = model.getReservationAt(row);
		}
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

}
