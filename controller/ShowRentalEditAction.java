package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.RentalEditDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.service.RentalService;


public class ShowRentalEditAction {
	private final Frame frame;
	private final RentalService rentalService;

	public ShowRentalEditAction(Frame frame, RentalService rentalService) {
		this.frame = frame;
		this.rentalService = rentalService;
	}

	public void execute(RentalDTO dto) throws RentexpresException {
		RentalEditDialog dlg = new RentalEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			rentalService.update(dlg.getRental());
		}
	}
}
