package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import com.pinguela.rentexpres.desktop.dialog.ReservaCreateDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.ReservaService;

public class ShowReservaCreateAction extends AbstractCreateAction<ReservaDTO, ReservaCreateDialog> {
    private final ReservaService reservaService;

    public ShowReservaCreateAction(Frame frame, ReservaService reservaService) {
        super(frame, null);
        this.reservaService = reservaService;
    }

    @Override
    protected ReservaCreateDialog createDialog() {
        return new ReservaCreateDialog(frame);
    }

    @Override
    protected void save(ReservaDTO dto) throws RentexpresException {
        reservaService.create(dto);
    }
}
