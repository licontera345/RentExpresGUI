package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.ReservaDetailDialog;
import com.pinguela.rentexpres.model.ReservaDTO;

public class ShowReservaDetailAction {
    private final Frame frame;

    public ShowReservaDetailAction(Frame frame) {
        this.frame = frame;
    }

    public void execute(ReservaDTO dto) {
        ReservaDetailDialog dlg = new ReservaDetailDialog(frame, dto);
        dlg.setVisible(true);
    }
}
