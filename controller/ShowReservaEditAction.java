package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.ReservaService;
import com.pinguela.rentexpres.desktop.dialog.ReservaEditDialog;
import com.pinguela.rentexpres.exception.RentexpresException;

public class ShowReservaEditAction {
	private final Frame frame;
	private final ReservaService reservaService;

	public ShowReservaEditAction(Frame frame, ReservaService reservaService) {
		this.frame = frame;
		this.reservaService = reservaService;
	}

	public void execute(ReservaDTO dto) throws RentexpresException {
		ReservaEditDialog dlg = new ReservaEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			reservaService.update(dlg.getReserva());
		}
	}
}
