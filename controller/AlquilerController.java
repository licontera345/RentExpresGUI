package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.AlquilerDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.AlquilerEditDialog;
import com.pinguela.rentexpres.desktop.model.AlquilerSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.service.AlquilerService;

public class AlquilerController {

	private static final String ERROR_ACTUALIZANDO = "Error actualizando alquiler:\n";

	private final Frame frame;
	private final AlquilerService service;
	private final JTable table;
	private final JButton btnVer;
	private final JButton btnEditar;

	public AlquilerController(Frame frame, AlquilerService service, JTable table, JButton btnVer, JButton btnEditar) {
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
					AlquilerSearchTableModel model = (AlquilerSearchTableModel) table.getModel();
					AlquilerDTO dto = model.getAlquilerAt(modelRow);
					if (dto != null) {
						new AlquilerDetailDialog(frame, dto).setVisible(true);
					}
				}
			}
		});

		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					int modelRow = table.convertRowIndexToModel(selectedRow);
					AlquilerSearchTableModel model = (AlquilerSearchTableModel) table.getModel();
					AlquilerDTO dto = model.getAlquilerAt(modelRow);
					if (dto != null) {
						AlquilerEditDialog dialog = new AlquilerEditDialog(frame, dto);
						dialog.setVisible(true);
						if (dialog.isConfirmed()) {
							try {
								service.update(dialog.getAlquiler());
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
