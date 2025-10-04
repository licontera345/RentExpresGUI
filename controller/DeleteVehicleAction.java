package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.model.VehicleSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleService;

public class DeleteVehicleAction implements ActionListener {

	private final JTable table;
	private final VehicleService vehicleService;
	private final SearchVehicleAction searchAction;

	public DeleteVehicleAction(JTable table, VehicleService vehicleService, SearchVehicleAction searchAction) {
		this.table = table;
		this.vehicleService = vehicleService;
		this.searchAction = searchAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) {
			SwingUtils.showWarning(null, "Debe seleccionar un vehicle para delete.");
			return;
		}
		VehicleSearchTableModel model = (VehicleSearchTableModel) table.getModel();
		VehicleDTO dto = model.getVehicleAt(row);

		int opcion = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete el vehicle con ID " + dto.getId() + "?", "Confirm deletion",
				JOptionPane.YES_NO_OPTION);
		if (opcion != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			vehicleService.delete(dto.getId());
			// Recargar tabla tras eliminación
			searchAction.load();
		} catch (Exception ex) {
			SwingUtils.showError(null, "Error al delete vehicle: " + ex.getMessage());
		}
	}
}
