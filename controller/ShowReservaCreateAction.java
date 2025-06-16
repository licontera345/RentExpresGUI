package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.ReservaService;
import com.pinguela.rentexpres.desktop.dialog.ReservaCreateDialog;
import com.pinguela.rentexpres.exception.RentexpresException;

public class ShowReservaCreateAction {
    private final Frame frame;
    private final ReservaService reservaService;

    public ShowReservaCreateAction(Frame frame, ReservaService reservaService) {
        this.frame = frame;
        this.reservaService = reservaService;
    }

    public void execute() throws RentexpresException {
        ReservaCreateDialog dlg = new ReservaCreateDialog(frame);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            ReservaDTO dto = dlg.getReserva();
            reservaService.create(dto);
        }
    }
}
