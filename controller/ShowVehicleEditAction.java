package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.VehicleEditDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.service.VehicleService;

public class ShowVehicleEditAction {
	private final Frame frame;
	private final VehicleService vehicleService;
	private final VehicleCategoryService categoryService;
	private final VehicleStatusService estadoService;

	public ShowVehicleEditAction(Frame frame, VehicleService vehicleService,
			VehicleCategoryService categoryService, VehicleStatusService estadoService) {
		this.frame = frame;
		this.vehicleService = vehicleService;
		this.categoryService = categoryService;
		this.estadoService = estadoService;
	}

	public void execute(VehicleDTO dto) throws RentexpresException {
		if (dto == null) {
			return;
		}
                VehicleEditDialog dlg = new VehicleEditDialog(frame, dto, categoryService.findAll(),
                                estadoService.findAll(), vehicleService);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			vehicleService.update(dlg.getVehicle(), null);
		}
	}
}
