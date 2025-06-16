package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.ClienteDetailDialog;
import com.pinguela.rentexpres.model.ClienteDTO;

public class ShowClienteDetailAction {

    private final Frame frame;

    public ShowClienteDetailAction(Frame frame) {
        this.frame = frame;
    }

    public void execute(ClienteDTO dto) {
        new ClienteDetailDialog(frame, dto).setVisible(true);
    }
}
