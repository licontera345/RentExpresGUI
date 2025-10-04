package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.ReservationDetailDialog;
import com.pinguela.rentexpres.model.ReservationDTO;

public class ShowReservationDetailAction {
    private final Frame frame;

    public ShowReservationDetailAction(Frame frame) {
        this.frame = frame;
    }

    public void execute(ReservationDTO dto) {
        ReservationDetailDialog dlg = new ReservationDetailDialog(frame, dto);
        dlg.setVisible(true);
    }
}
