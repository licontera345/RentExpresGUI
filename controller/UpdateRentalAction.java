package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.RentalEditDialog;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.service.RentalService;

public class UpdateRentalAction {

    private final Frame owner;
    private final RentalService rentalService;
    private final RentalDTO rental;
    private final ActionCallback onUpdated;

    public UpdateRentalAction(Frame owner, RentalService rentalService, RentalDTO rental, ActionCallback onUpdated) {
        this.owner = owner;
        this.rentalService = rentalService;
        this.rental = rental;
        this.onUpdated = onUpdated;
    }

    public void actionPerformed(Void v) {
        RentalEditDialog dlg = new RentalEditDialog(owner, rental);
        dlg.setVisible(true);

        if (!dlg.isConfirmed()) return;

        try {
            rentalService.update(dlg.getRental());
            if (onUpdated != null) onUpdated.execute();
        } catch (RentexpresException ex) {
            SwingUtils.showError(owner, "Error actualizando el rental:\n" + ex.getMessage());
        }
    }
}
