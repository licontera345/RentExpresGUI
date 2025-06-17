package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.renderer.AbstractActionsCellEditor;

import com.pinguela.rentexpres.desktop.dialog.AlquilerDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.AlquilerEditDialog;
import com.pinguela.rentexpres.desktop.model.AlquilerSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.view.AlquilerTablePanel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.service.AlquilerService;

/**
 * Editor con botones Ver / Editar / Borrar. 100 % Java 8 clásico – sin lambdas.
 */
public class AlquilerActionsCellEditor extends AbstractActionsCellEditor {
        private static final long serialVersionUID = 1L;

        private final Frame frame;
        private final AlquilerService service;
        private final AlquilerTablePanel parentTable; // Para refrescar y obtener DTO
        private AlquilerDTO alquilerActual;

        public AlquilerActionsCellEditor(Frame owner, AlquilerService srv, AlquilerTablePanel parent) {
                super();
                this.frame = owner;
                this.service = srv;
                this.parentTable = parent;

		/* Ver */
		btnVer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (alquilerActual != null) {
					new AlquilerDetailDialog(frame, alquilerActual).setVisible(true);
				}
				fireEditingStopped();
			}
		});

		/* Editar */
		btnEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (alquilerActual != null) {
					AlquilerEditDialog dlg = new AlquilerEditDialog(frame, alquilerActual);
					dlg.setVisible(true);
					if (dlg.isConfirmed()) {
						try {
							service.update(dlg.getAlquiler());
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
				if (alquilerActual != null
						&& SwingUtils.showConfirm(frame, "¿Eliminar alquiler " + alquilerActual.getId() + "?",
								"Confirmar borrado") == JOptionPane.YES_OPTION) {
					try {
						service.delete(alquilerActual.getId());
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
		if (table.getModel() instanceof AlquilerSearchTableModel) {
			AlquilerSearchTableModel m = (AlquilerSearchTableModel) table.getModel();
			alquilerActual = m.getAlquilerAt(table.convertRowIndexToModel(row));
		}
		return panel;
	}

        public Object getCellEditorValue() {
                return null;
        }
}
