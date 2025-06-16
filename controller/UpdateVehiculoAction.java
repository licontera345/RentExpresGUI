package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.VehiculoService;


public class UpdateVehiculoAction implements ActionListener {
	private final Frame frame;
	private final VehiculoService vehiculoService;
	private final Supplier<VehiculoDTO> dtoSupplier;
        private final ActionCallback onRefresh;

        public UpdateVehiculoAction(Frame frame, VehiculoService vehiculoService, Supplier<VehiculoDTO> dtoSupplier,
                        ActionCallback onRefresh) {
		this.frame = frame;
		this.vehiculoService = vehiculoService;
		this.dtoSupplier = dtoSupplier;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		VehiculoDTO dto = dtoSupplier.get();
		if (dto == null) {
			return;
		}
		try {
			if (dto.getId() == null) {
				vehiculoService.create(dto, null);
			} else {
				vehiculoService.update(dto, null);
			}
                        if (onRefresh != null) {
                                onRefresh.execute();
                        }
		} catch (RentexpresException ex) {
			JOptionPane.showMessageDialog(frame, "Error guardando vehículo: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
