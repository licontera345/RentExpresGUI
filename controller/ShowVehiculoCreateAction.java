package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.VehiculoCreateDialog;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.CategoriaVehiculoService;
import com.pinguela.rentexpres.service.EstadoVehiculoService;
import com.pinguela.rentexpres.service.VehiculoService;

public class ShowVehiculoCreateAction {
	private final Frame frame;
	private final VehiculoService vehiculoService;
	private final CategoriaVehiculoService categoriaService;
	private final EstadoVehiculoService estadoService;

	public ShowVehiculoCreateAction(Frame frame, VehiculoService vehiculoService,
			CategoriaVehiculoService categoriaService, EstadoVehiculoService estadoService) {
		this.frame = frame;
		this.vehiculoService = vehiculoService;
		this.categoriaService = categoriaService;
		this.estadoService = estadoService;
	}

	public void execute() throws RentexpresException {
		VehiculoCreateDialog dlg = new VehiculoCreateDialog(frame, categoriaService.findAll(), estadoService.findAll());
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			VehiculoDTO dto = dlg.getVehiculo();
			vehiculoService.create(dto, null);
		}
	}
}
