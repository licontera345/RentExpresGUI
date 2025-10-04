package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.RentalCreateDialog;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.service.RentalService;

public class ShowRentalCreateAction extends AbstractCreateAction<RentalDTO, RentalCreateDialog> {
    private final RentalService rentalService;

    public ShowRentalCreateAction(Frame frame, RentalService rentalService) {
        super(frame, null);
        this.rentalService = rentalService;
    }

    @Override
    protected RentalCreateDialog createDialog() {
        return new RentalCreateDialog(frame);
    }

    @Override
    protected void save(RentalDTO dto) throws RentexpresException {
        if (rentalService.existsByReservation(dto.getReservationId())) {
            throw new RentexpresException("Esta reservation ya tiene un rental asignado.");
        }
        rentalService.create(dto);
    }
}
