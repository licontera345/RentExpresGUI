package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.model.VehicleSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleService;

public class DeleteSelectedVehicleAction implements ActionListener {

	private final JTable table;
	private final VehicleService vehicleService;
	private final SearchVehicleAction searchAction;

	public DeleteSelectedVehicleAction(JTable table, VehicleService vehicleService,
			SearchVehicleAction searchAction) {
		this.table = table;
		this.vehicleService = vehicleService;
		this.searchAction = searchAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		VehicleSearchTableModel model = (VehicleSearchTableModel) table.getModel();
		List<VehicleDTO> selected = model.getSelectedItems();

		if (selected.isEmpty()) {
			SwingUtils.showWarning(null, "No hay vehicles selected para delete.");
			return;
		}

		int opcion = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete los " + selected.size() + " vehicles selected?",
				"Confirm deletion múltiple", JOptionPane.YES_NO_OPTION);
		if (opcion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean fell = false;
		StringBuilder errores = new StringBuilder();
		for (VehicleDTO dto : selected) {
			try {
				vehicleService.delete(dto.getId());
			} catch (Exception ex) {
				fell = true;
				errores.append("ID ").append(dto.getId()).append(": ").append(ex.getMessage()).append("\n");
			}
		}

		if (fell) {
			SwingUtils.showError(null, "Ocurrieron errores al delete:\n" + errores.toString());
		} else {
			SwingUtils.showInfo(null, "Vehicles eliminados correctamente.");
		}

		try {
			searchAction.load();
		} catch (Exception ex) {
			SwingUtils.showError(null, "Error al recargar tabla: " + ex.getMessage());
		}
	}
}