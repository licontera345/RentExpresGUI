package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.dialog.VehiculoDetailDialog;
import com.pinguela.rentexpres.model.VehiculoDTO;

public class ShowVehiculoDetailAction {
	private final Frame frame;

	public ShowVehiculoDetailAction(Frame frame) {
		this.frame = frame;
	}

	public void execute(VehiculoDTO dto) {
		if (dto == null) {
			return;
		}
		VehiculoDetailDialog dlg = new VehiculoDetailDialog(frame, dto);
		dlg.setVisible(true);
	}
}
