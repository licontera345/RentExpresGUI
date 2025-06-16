package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;

import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.AlquilerDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.AlquilerEditDialog;
import com.pinguela.rentexpres.desktop.model.AlquilerSearchTableModel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.service.AlquilerService;

public class SearchAlquilerAction {
	private final Frame frame;
	private final AlquilerService service;
	private final JTable table;

	public SearchAlquilerAction(Frame frame, AlquilerService service, JTable table) {
		this.frame = frame;
		this.service = service;
		this.table = table;
	}

	public void load() throws RentexpresException {
		List<AlquilerDTO> list = service.findAll();
		table.setModel(new AlquilerSearchTableModel(list, null));
	}

	public void showDetail(AlquilerDTO dto) {
		if (dto != null)
			new AlquilerDetailDialog(frame, dto).setVisible(true);
	}

	public void showEdit(AlquilerDTO dto, Runnable reload) {
		if (dto == null)
			return;
		var dlg = new AlquilerEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			try {
				service.update(dlg.getAlquiler());
			} catch (RentexpresException ignored) {
			}
			if (reload != null)
				reload.run();
		}
	}
}