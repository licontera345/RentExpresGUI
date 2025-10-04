package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.VehicleCreateDialog;
import com.pinguela.rentexpres.desktop.dialog.VehicleDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.VehicleEditDialog;
import com.pinguela.rentexpres.desktop.model.VehicleSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.service.VehicleService;

public class SearchVehicleAction {
	private final Frame frame;
	private final VehicleService vehicleService;
	private final VehicleCategoryService categoryService;
	private final VehicleStatusService estadoService;
	private final JTable table;

	public SearchVehicleAction(Frame frame, VehicleService vehicleService, VehicleCategoryService categoryService,
			VehicleStatusService estadoService, JTable table) {
		this.frame = frame;
		this.vehicleService = vehicleService;
		this.categoryService = categoryService;
		this.estadoService = estadoService;
		this.table = table;
	}

	public void load() throws RentexpresException {
		List<VehicleDTO> list = vehicleService.findAll();
		table.setModel(new VehicleSearchTableModel(list));
	}

        public void showCreate(ActionCallback onReload) throws RentexpresException {
                VehicleCreateDialog dlg = new VehicleCreateDialog(frame, categoryService.findAll(),
                                estadoService.findAll(), vehicleService);
		dlg.setVisible(true);
		if (!dlg.isConfirmed()) {
			return;
		}
		try {
			VehicleDTO dto = dlg.getVehicle();
			vehicleService.create(dto, null);
                        if (onReload != null) {
                                onReload.execute();
                        }
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error al crear vehicle: " + ex.getMessage());
		}
	}

	public void showDetail(VehicleDTO dto) {
		if (dto == null) {
			return;
		}
		VehicleDetailDialog dlg = new VehicleDetailDialog(frame, dto);
		dlg.setVisible(true);
	}

        public void showEdit(VehicleDTO dto, ActionCallback onReload) throws RentexpresException {
		if (dto == null) {
			return;
		}
                VehicleEditDialog dlg = new VehicleEditDialog(frame, dto, categoryService.findAll(),
                                estadoService.findAll(), vehicleService);
		dlg.setVisible(true);
		if (!dlg.isConfirmed()) {
			return;
		}
		try {
			vehicleService.update(dlg.getVehicle(), null);
                        if (onReload != null) {
                                onReload.execute();
                        }
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error al actualizar vehicle: " + ex.getMessage());
		}
	}

	public void loadByCriteria(VehicleCriteria criteria) {
		try {
			Results<VehicleDTO> results = vehicleService.findByCriteria(criteria);
			List<VehicleDTO> list = results.getResults();
			table.setModel(new VehicleSearchTableModel(list));
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error al buscar vehicles: " + ex.getMessage());
		}
	}

	public VehicleStatusService getEstadoService() {
		return estadoService;
	}

	public VehicleCategoryService getCategoryService() {
		return categoryService;
	}

	public SearchVehicleAction getSearchAction() {
		return this;
	}
}
