package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.RentalDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.RentalEditDialog;
import com.pinguela.rentexpres.desktop.model.RentalSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.service.RentalService;

public class RentalController {

	private static final String ERROR_ACTUALIZANDO = "Error actualizando rental:\n";

	private final Frame frame;
	private final RentalService service;
	private final JTable table;
	private final JButton btnVer;
	private final JButton btnEditar;

	public RentalController(Frame frame, RentalService service, JTable table, JButton btnVer, JButton btnEditar) {
		this.frame = frame;
		this.service = service;
		this.table = table;
		this.btnVer = btnVer;
		this.btnEditar = btnEditar;

		initController();
	}

	private void initController() {
		btnVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					int modelRow = table.convertRowIndexToModel(selectedRow);
					RentalSearchTableModel model = (RentalSearchTableModel) table.getModel();
					RentalDTO dto = model.getRentalAt(modelRow);
					if (dto != null) {
						new RentalDetailDialog(frame, dto).setVisible(true);
					}
				}
			}
		});

		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					int modelRow = table.convertRowIndexToModel(selectedRow);
					RentalSearchTableModel model = (RentalSearchTableModel) table.getModel();
					RentalDTO dto = model.getRentalAt(modelRow);
					if (dto != null) {
						RentalEditDialog dialog = new RentalEditDialog(frame, dto);
						dialog.setVisible(true);
						if (dialog.isConfirmed()) {
							try {
								service.update(dialog.getRental());
							} catch (RentexpresException ex) {
								SwingUtils.showError(frame, ERROR_ACTUALIZANDO + ex.getMessage());
							}
						}
					}
				}
			}
		});
	}
}
