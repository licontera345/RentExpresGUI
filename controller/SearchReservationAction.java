package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.ReservationDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ReservationEditDialog;
import com.pinguela.rentexpres.desktop.model.ReservationSearchTableModel;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.ReservationService;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

public class SearchReservationAction {
	private final Frame frame;
	private final ReservationService service;
	private final JTable table;

	public SearchReservationAction(Frame frame, ReservationService service, JTable table) {
		this.frame = frame;
		this.service = service;
		this.table = table;
	}

	public void load() throws RentexpresException {
		List<ReservationDTO> list = service.findAll();
		table.setModel(new ReservationSearchTableModel(list, null));
	}

	public void showDetail(ReservationDTO dto) {
		if (dto != null)
			new ReservationDetailDialog(frame, dto).setVisible(true);
	}

        public void showEdit(ReservationDTO dto, ActionCallback reload) {
		if (dto == null)
			return;
                ReservationEditDialog dlg = new ReservationEditDialog(frame, dto);
		dlg.setVisible(true);
		if (dlg.isConfirmed()) {
			try {
				service.update(dlg.getReservation());
			} catch (RentexpresException ignored) {
			}
                        if (reload != null)
                                reload.execute();
		}
	}
}