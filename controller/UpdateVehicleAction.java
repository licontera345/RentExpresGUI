package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JOptionPane;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleService;


public class UpdateVehicleAction implements ActionListener {
	private final Frame frame;
	private final VehicleService vehicleService;
	private final Supplier<VehicleDTO> dtoSupplier;
        private final ActionCallback onRefresh;

        public UpdateVehicleAction(Frame frame, VehicleService vehicleService, Supplier<VehicleDTO> dtoSupplier,
                        ActionCallback onRefresh) {
		this.frame = frame;
		this.vehicleService = vehicleService;
		this.dtoSupplier = dtoSupplier;
		this.onRefresh = onRefresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		VehicleDTO dto = dtoSupplier.get();
		if (dto == null) {
			return;
		}
		try {
			if (dto.getId() == null) {
				vehicleService.create(dto, null);
			} else {
				vehicleService.update(dto, null);
			}
                        if (onRefresh != null) {
                                onRefresh.execute();
                        }
		} catch (RentexpresException ex) {
			JOptionPane.showMessageDialog(frame, "Error guardando vehicle: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
