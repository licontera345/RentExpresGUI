package com.pinguela.rentexpres.desktop.controller;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.desktop.dialog.CustomerDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.CustomerEditDialog;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.CustomerService;

/** Controller for actions on a single customer row. */
public class CustomerRowController {
    private final Frame frame;
    private final CustomerService service;
    private final ActionCallback reload;

    public CustomerRowController(Frame frame, CustomerService service, ActionCallback reload) {
        this.frame = frame;
        this.service = service;
        this.reload = reload;
    }

    public void showDetail(CustomerDTO dto) {
        if (dto != null) {
            new CustomerDetailDialog(frame, dto).setVisible(true);
        }
    }

    public void edit(CustomerDTO dto) {
        if (dto == null) return;
        CustomerEditDialog dlg = new CustomerEditDialog(frame, dto);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            try {
                service.update(dlg.getCustomer());
                if (reload != null) reload.execute();
            } catch (RentexpresException ex) {
                SwingUtils.showError(frame, ex.getMessage());
            }
        }
    }

    public void delete(CustomerDTO dto) {
        if (dto == null) return;
        if (SwingUtils.showConfirm(frame,
                "Delete customer " + dto.getId() + "?", "Confirm deletion") == JOptionPane.YES_OPTION) {
            try {
                service.delete(dto.getId());
                if (reload != null) reload.execute();
            } catch (RentexpresException ex) {
                SwingUtils.showError(frame, ex.getMessage());
            }
        }
    }
}
