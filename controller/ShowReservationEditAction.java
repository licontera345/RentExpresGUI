package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.ReservationService;
import com.pinguela.rentexpres.desktop.dialog.ReservationEditDialog;
import com.pinguela.rentexpres.exception.RentexpresException;

public class ShowReservationEditAction {
	private final Frame frame;
	private final ReservationService reservationService;

	public ShowReservationEditAction(Frame frame, ReservationService reservationService) {
		this.frame = frame;
		this.reservationService = reservationService;
	}

	public void execute(ReservationDTO dto) throws RentexpresException {
		ReservationEditDialog dlg = new ReservationEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			reservationService.update(dlg.getReservation());
		}
	}
}
