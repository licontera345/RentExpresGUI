package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.ClienteCreateDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.ClienteService;

public class ShowClienteCreateAction {

	private final Frame frame;
	private final ClienteService clienteService;

	public ShowClienteCreateAction(Frame frame, ClienteService clienteService) {
		this.frame = frame;
		this.clienteService = clienteService;
	}

	public void execute() throws RentexpresException {
		ClienteCreateDialog dlg = new ClienteCreateDialog(frame);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			ClienteDTO dto = dlg.getCliente();
			clienteService.create(dto);
		}
	}
}
