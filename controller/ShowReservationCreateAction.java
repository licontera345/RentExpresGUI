package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import com.pinguela.rentexpres.desktop.dialog.ReservationCreateDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.ReservationService;

public class ShowReservationCreateAction extends AbstractCreateAction<ReservationDTO, ReservationCreateDialog> {
    private final ReservationService reservationService;

    public ShowReservationCreateAction(Frame frame, ReservationService reservationService) {
        super(frame, null);
        this.reservationService = reservationService;
    }

    @Override
    protected ReservationCreateDialog createDialog() {
        return new ReservationCreateDialog(frame);
    }

    @Override
    protected void save(ReservationDTO dto) throws RentexpresException {
        reservationService.create(dto);
    }
}
