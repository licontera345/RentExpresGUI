package com.pinguela.rentexpres.desktop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.model.VehiculoSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.VehiculoService;

public class DeleteSelectedVehiculoAction implements ActionListener {

	private final JTable table;
	private final VehiculoService vehiculoService;
	private final SearchVehiculoAction searchAction;

	public DeleteSelectedVehiculoAction(JTable table, VehiculoService vehiculoService,
			SearchVehiculoAction searchAction) {
		this.table = table;
		this.vehiculoService = vehiculoService;
		this.searchAction = searchAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		VehiculoSearchTableModel model = (VehiculoSearchTableModel) table.getModel();
		List<VehiculoDTO> seleccionados = model.getSelectedItems();

		if (seleccionados.isEmpty()) {
			SwingUtils.showWarning(null, "No hay vehículos seleccionados para eliminar.");
			return;
		}

		int opcion = JOptionPane.showConfirmDialog(null,
				"¿Seguro que desea eliminar los " + seleccionados.size() + " vehículos seleccionados?",
				"Confirmar eliminación múltiple", JOptionPane.YES_NO_OPTION);
		if (opcion != JOptionPane.YES_OPTION) {
			return;
		}

		boolean fell = false;
		StringBuilder errores = new StringBuilder();
		for (VehiculoDTO dto : seleccionados) {
			try {
				vehiculoService.delete(dto.getId());
			} catch (Exception ex) {
				fell = true;
				errores.append("ID ").append(dto.getId()).append(": ").append(ex.getMessage()).append("\n");
			}
		}

		if (fell) {
			SwingUtils.showError(null, "Ocurrieron errores al eliminar:\n" + errores.toString());
		} else {
			SwingUtils.showInfo(null, "Vehículos eliminados correctamente.");
		}

		try {
			searchAction.load();
		} catch (Exception ex) {
			SwingUtils.showError(null, "Error al recargar tabla: " + ex.getMessage());
		}
	}
}