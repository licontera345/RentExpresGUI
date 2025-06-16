package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.model.VehiculoSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.VehiculoService;

public class DeleteVehiculoAction implements ActionListener {

	private final JTable table;
	private final VehiculoService vehiculoService;
	private final SearchVehiculoAction searchAction;

	public DeleteVehiculoAction(JTable table, VehiculoService vehiculoService, SearchVehiculoAction searchAction) {
		this.table = table;
		this.vehiculoService = vehiculoService;
		this.searchAction = searchAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) {
			SwingUtils.showWarning(null, "Debe seleccionar un vehículo para eliminar.");
			return;
		}
		VehiculoSearchTableModel model = (VehiculoSearchTableModel) table.getModel();
		VehiculoDTO dto = model.getVehiculoAt(row);

		int opcion = JOptionPane.showConfirmDialog(null,
				"¿Seguro que desea eliminar el vehículo con ID " + dto.getId() + "?", "Confirmar eliminación",
				JOptionPane.YES_NO_OPTION);
		if (opcion != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			vehiculoService.delete(dto.getId());
			// Recargar tabla tras eliminación
			searchAction.load();
		} catch (Exception ex) {
			SwingUtils.showError(null, "Error al eliminar vehículo: " + ex.getMessage());
		}
	}
}
