package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.VehicleDetailDialog;
import com.pinguela.rentexpres.model.VehicleDTO;

public class ShowVehicleDetailAction {
	private final Frame frame;

	public ShowVehicleDetailAction(Frame frame) {
		this.frame = frame;
	}

	public void execute(VehicleDTO dto) {
		if (dto == null) {
			return;
		}
		VehicleDetailDialog dlg = new VehicleDetailDialog(frame, dto);
		dlg.setVisible(true);
	}
}
