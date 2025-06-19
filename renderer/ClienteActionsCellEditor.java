package com.pinguela.rentexpres.desktop.renderer;

import java.awt.Component;
import java.awt.Frame;
import java.util.EventObject;
import java.util.function.Supplier;

import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.renderer.AbstractActionsCellEditor;

import com.pinguela.rentexpres.desktop.model.ClienteSearchTableModel;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;
import com.pinguela.rentexpres.desktop.controller.ClienteRowController;

public class ClienteActionsCellEditor extends AbstractActionsCellEditor {
	private static final long serialVersionUID = 1L;

        private final Frame frame;
        private final ClienteRowController controller;
        private final ActionCallback reload;
        private final Supplier<ClienteDTO> rowSupplier;
        private ClienteDTO clienteActual;

        public ClienteActionsCellEditor(Frame owner, ClienteService service, ActionCallback reload,
                        Supplier<ClienteDTO> rowSupplier) {
                super();
                this.frame = owner;
                this.reload = reload;
                this.rowSupplier = rowSupplier;
                this.controller = new ClienteRowController(owner, service, reload);

                btnView.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                controller.showDetail(clienteActual);
                                fireEditingStopped();
                        }
                });

                btnEdit.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                controller.edit(clienteActual);
                                fireEditingStopped();
                        }
                });

                btnDel.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                controller.delete(clienteActual);
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
