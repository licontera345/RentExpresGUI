package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.AlquilerCreateDialog;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.service.AlquilerService;

public class ShowAlquilerCreateAction extends AbstractCreateAction<AlquilerDTO, AlquilerCreateDialog> {
    private final AlquilerService alquilerService;

    public ShowAlquilerCreateAction(Frame frame, AlquilerService alquilerService) {
        super(frame, null);
        this.alquilerService = alquilerService;
    }

    @Override
    protected AlquilerCreateDialog createDialog() {
        return new AlquilerCreateDialog(frame);
    }

    @Override
    protected void save(AlquilerDTO dto) throws RentexpresException {
        if (alquilerService.existsByReserva(dto.getIdReserva())) {
            throw new RentexpresException("Esta reserva ya tiene un alquiler asignado.");
        }
        alquilerService.create(dto);
    }
}
