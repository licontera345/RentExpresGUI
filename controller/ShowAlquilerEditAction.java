package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.AlquilerEditDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.service.AlquilerService;


public class ShowAlquilerEditAction {
	private final Frame frame;
	private final AlquilerService alquilerService;

	public ShowAlquilerEditAction(Frame frame, AlquilerService alquilerService) {
		this.frame = frame;
		this.alquilerService = alquilerService;
	}

	public void execute(AlquilerDTO dto) throws RentexpresException {
		AlquilerEditDialog dlg = new AlquilerEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			alquilerService.update(dlg.getAlquiler());
		}
	}
}
