package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.ReservaDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ReservaEditDialog;
import com.pinguela.rentexpres.desktop.model.ReservaSearchTableModel;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.service.ReservaService;
import com.pinguela.rentexpres.exception.RentexpresException;

public class SearchReservaAction {
	private final Frame frame;
	private final ReservaService service;
	private final JTable table;

	public SearchReservaAction(Frame frame, ReservaService service, JTable table) {
		this.frame = frame;
		this.service = service;
		this.table = table;
	}

	public void load() throws RentexpresException {
		List<ReservaDTO> list = service.findAll();
		table.setModel(new ReservaSearchTableModel(list, null));
	}

	public void showDetail(ReservaDTO dto) {
		if (dto != null)
			new ReservaDetailDialog(frame, dto).setVisible(true);
	}

	public void showEdit(ReservaDTO dto, Runnable reload) {
		if (dto == null)
			return;
                ReservaEditDialog dlg = new ReservaEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			try {
				service.update(dlg.getReserva());
			} catch (RentexpresException ignored) {
			}
			if (reload != null)
				reload.run();
		}
	}
}