package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.renderer.AbstractActionsCellEditor;

import com.pinguela.rentexpres.desktop.dialog.RentalDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.RentalEditDialog;
import com.pinguela.rentexpres.desktop.model.RentalSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.view.RentalTablePanel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.service.RentalService;

/**
 * Editor con botones Ver / Editar / Borrar. 100 % Java 8 clásico – sin lambdas.
 */
public class RentalActionsCellEditor extends AbstractActionsCellEditor {
        private static final long serialVersionUID = 1L;

        private final Frame frame;
        private final RentalService service;
        private final RentalTablePanel parentTable; // Para refrescar y obtener DTO
        private RentalDTO rentalActual;

        public RentalActionsCellEditor(Frame owner, RentalService srv, RentalTablePanel parent) {
                super();
                this.frame = owner;
                this.service = srv;
                this.parentTable = parent;

		/* Ver */
		btnView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (rentalActual != null) {
					new RentalDetailDialog(frame, rentalActual).setVisible(true);
				}
				fireEditingStopped();
			}
		});

		/* Editar */
		btnEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (rentalActual != null) {
					RentalEditDialog dlg = new RentalEditDialog(frame, rentalActual);
					dlg.setVisible(true);
					if (dlg.isConfirmed()) {
						try {
							service.update(dlg.getRental());
						} catch (RentexpresException ex) {
							SwingUtils.showError(frame, ex.getMessage());
						}
						parentTable.reloadIfNeeded();
					}
				}
				fireEditingStopped();
			}
		});

		/* Borrar */
		btnDel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (rentalActual != null
						&& SwingUtils.showConfirm(frame, "Delete rental " + rentalActual.getId() + "?",
								"Confirm deletion") == JOptionPane.YES_OPTION) {
					try {
						service.delete(rentalActual.getId());
					} catch (RentexpresException ex) {
						SwingUtils.showError(frame, ex.getMessage());
					}
					parentTable.reloadIfNeeded();
				}
				fireEditingStopped();
			}
		});
	}

	/* === TableCellEditor === */
	public Component getTableCellEditorComponent(JTable table, Object v, boolean sel, int row, int col) {
		if (table.getModel() instanceof RentalSearchTableModel) {
			RentalSearchTableModel m = (RentalSearchTableModel) table.getModel();
			rentalActual = m.getRentalAt(table.convertRowIndexToModel(row));
		}
		return panel;
	}

        public Object getCellEditorValue() {
                return null;
        }
}
