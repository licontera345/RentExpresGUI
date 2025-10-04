package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.CustomerDetailDialog;
import com.pinguela.rentexpres.model.CustomerDTO;

public class ShowCustomerDetailAction {

    private final Frame frame;

    public ShowCustomerDetailAction(Frame frame) {
        this.frame = frame;
    }

    public void execute(CustomerDTO dto) {
        new CustomerDetailDialog(frame, dto).setVisible(true);
    }
}
