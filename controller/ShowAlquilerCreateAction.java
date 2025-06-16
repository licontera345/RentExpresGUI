package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.AlquilerCreateDialog;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.service.AlquilerService;

public class ShowAlquilerCreateAction {
    private final Frame frame;
    private final AlquilerService alquilerService;

    public ShowAlquilerCreateAction(Frame frame, AlquilerService alquilerService) {
        this.frame = frame;
        this.alquilerService = alquilerService;
    }

    public void execute() throws RentexpresException {
        AlquilerCreateDialog dlg = new AlquilerCreateDialog(frame);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            AlquilerDTO dto = dlg.getAlquiler();
            try {
                if (alquilerService.existsByReserva(dto.getIdReserva())) {
                    SwingUtils.showError(frame, "Esta reserva ya tiene un alquiler asignado.");
                    return;
                }
                alquilerService.create(dto);
            } catch (RentexpresException ex) {
                SwingUtils.showError(frame, "Error guardando: " + ex.getMessage());
            }
        }
    }
}
