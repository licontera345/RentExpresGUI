package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.ClienteEditDialog;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;

public class UpdateClienteAction implements ActionListener {

	private final Frame frame;
	private final Supplier<ClienteDTO> currentSupplier;
        private final ClienteService service;
        private final ActionCallback onRefresh;

        public UpdateClienteAction(Frame frame, Supplier<ClienteDTO> currentSupplier, ClienteService service,
                        ActionCallback onRefresh) {
		this.frame = frame;
		this.currentSupplier = currentSupplier;
		this.service = service;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ClienteDTO dto = currentSupplier.get();
		if (dto == null) {
			SwingUtils.showWarning(frame, "Selecciona un cliente para editar.");
			return;
		}
		ClienteEditDialog dlg = new ClienteEditDialog(frame, dto);
		dlg.setVisible(true);
		if (!dlg.isConfirmed())
			return;

		try {
			service.update(dlg.getCliente());
                        if (onRefresh != null)
                                onRefresh.execute();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frame, "Error actualizando el cliente: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
