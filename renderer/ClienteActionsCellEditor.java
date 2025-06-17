package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;
import java.util.EventObject;
import java.util.function.Supplier;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.renderer.AbstractActionsCellEditor;

import com.pinguela.rentexpres.desktop.dialog.ClienteDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ClienteEditDialog;
import com.pinguela.rentexpres.desktop.model.ClienteSearchTableModel;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;

public class ClienteActionsCellEditor extends AbstractActionsCellEditor {
	private static final long serialVersionUID = 1L;

        private final Frame frame;
        private final ClienteService service;
        private final ActionCallback reload;
        private final Supplier<ClienteDTO> rowSupplier;
        private ClienteDTO clienteActual;

        public ClienteActionsCellEditor(Frame owner, ClienteService service, ActionCallback reload,
                        Supplier<ClienteDTO> rowSupplier) {
                super();
                this.frame = owner;
                this.service = service;
                this.reload = reload;
                this.rowSupplier = rowSupplier;

		btnView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
                                if (clienteActual != null) {
                                        new ClienteDetailDialog(frame, clienteActual).setVisible(true);
                                }
                                fireEditingStopped();
                        }
                });

		btnEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
                                if (clienteActual != null) {
                                        ClienteEditDialog dlg = new ClienteEditDialog(frame, clienteActual);
                                        dlg.setVisible(true);
                                        if (dlg.isConfirmed()) {
                                                try {
                                                        service.update(dlg.getCliente());
                                                        if (reload != null) {
                                                                reload.execute();
                                                        }
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
                                if (clienteActual != null
                                                && SwingUtils.showConfirm(frame, "¿Eliminar cliente " + clienteActual.getId() + "?",
                                                                "Confirmar borrado") == JOptionPane.YES_OPTION) {
                                        try {
                                                service.delete(clienteActual.getId());
                                                if (reload != null) {
                                                        reload.execute();
                                                }
                                        } catch (RentexpresException ex) {
                                                SwingUtils.showError(frame, ex.getMessage());
                                        }
                                }
                                fireEditingStopped();
                        }
                });
        }

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (rowSupplier != null) {
                        clienteActual = rowSupplier.get();
                } else if (table.getModel() instanceof ClienteSearchTableModel) {
                        ClienteSearchTableModel model = (ClienteSearchTableModel) table.getModel();
                        clienteActual = model.getClienteAt(row);
                }
                return panel;
        }

	@Override
	public Object getCellEditorValue() {
		return null;
	}

}
