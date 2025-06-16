package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.AlquilerEditDialog;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.service.AlquilerService;

public class UpdateAlquilerAction {

    private final Frame owner;
    private final AlquilerService alquilerService;
    private final AlquilerDTO alquiler;
    private final Runnable onUpdated;

    public UpdateAlquilerAction(Frame owner, AlquilerService alquilerService, AlquilerDTO alquiler, Runnable onUpdated) {
        this.owner = owner;
        this.alquilerService = alquilerService;
        this.alquiler = alquiler;
        this.onUpdated = onUpdated;
    }

    public void actionPerformed(Void v) {
        AlquilerEditDialog dlg = new AlquilerEditDialog(owner, alquiler);
        dlg.setVisible(true);

        if (!dlg.isConfirmed()) return;

        try {
            alquilerService.update(dlg.getAlquiler());
            if (onUpdated != null) onUpdated.run();
        } catch (RentexpresException ex) {
            SwingUtils.showError(owner, "Error actualizando el alquiler:\n" + ex.getMessage());
        }
    }
}
