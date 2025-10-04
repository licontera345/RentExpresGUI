package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;

import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.RentalDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.RentalEditDialog;
import com.pinguela.rentexpres.desktop.model.RentalSearchTableModel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.service.RentalService;

public class SearchRentalAction {
	private final Frame frame;
	private final RentalService service;
	private final JTable table;

	public SearchRentalAction(Frame frame, RentalService service, JTable table) {
		this.frame = frame;
		this.service = service;
		this.table = table;
	}

	public void load() throws RentexpresException {
		List<RentalDTO> list = service.findAll();
		table.setModel(new RentalSearchTableModel(list, null));
	}

	public void showDetail(RentalDTO dto) {
		if (dto != null)
			new RentalDetailDialog(frame, dto).setVisible(true);
	}

        public void showEdit(RentalDTO dto, ActionCallback reload) {
		if (dto == null)
			return;
                RentalEditDialog dlg = new RentalEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			try {
				service.update(dlg.getRental());
			} catch (RentexpresException ignored) {
			}
                        if (reload != null)
                                reload.execute();
		}
	}
}