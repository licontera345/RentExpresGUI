package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.ClienteEditDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;

public class ShowClienteEditAction {

	private final Frame frame;
	private final ClienteService clienteService;

	public ShowClienteEditAction(Frame frame, ClienteService clienteService) {
		this.frame = frame;
		this.clienteService = clienteService;
	}

	public void execute(ClienteDTO dto) throws RentexpresException {
		ClienteEditDialog dlg = new ClienteEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			clienteService.update(dlg.getCliente());
		}
	}
}
