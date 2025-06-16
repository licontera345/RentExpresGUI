package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

import javax.swing.JTable;

import com.pinguela.rentexpres.desktop.dialog.VehiculoCreateDialog;
import com.pinguela.rentexpres.desktop.dialog.VehiculoDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.VehiculoEditDialog;
import com.pinguela.rentexpres.desktop.model.VehiculoSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehiculoCriteria;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.CategoriaVehiculoService;
import com.pinguela.rentexpres.service.EstadoVehiculoService;
import com.pinguela.rentexpres.service.VehiculoService;

public class SearchVehiculoAction {
	private final Frame frame;
	private final VehiculoService vehiculoService;
	private final CategoriaVehiculoService categoriaService;
	private final EstadoVehiculoService estadoService;
	private final JTable table;

	public SearchVehiculoAction(Frame frame, VehiculoService vehiculoService, CategoriaVehiculoService categoriaService,
			EstadoVehiculoService estadoService, JTable table) {
		this.frame = frame;
		this.vehiculoService = vehiculoService;
		this.categoriaService = categoriaService;
		this.estadoService = estadoService;
		this.table = table;
	}

	public void load() throws RentexpresException {
		List<VehiculoDTO> list = vehiculoService.findAll();
		table.setModel(new VehiculoSearchTableModel(list));
	}

        public void showCreate(ActionCallback onReload) throws RentexpresException {
		VehiculoCreateDialog dlg = new VehiculoCreateDialog(frame, categoriaService.findAll(), estadoService.findAll());
		dlg.setVisible(true);
		if (!dlg.isConfirmed()) {
			return;
		}
		try {
			VehiculoDTO dto = dlg.getVehiculo();
			vehiculoService.create(dto, null);
                        if (onReload != null) {
                                onReload.execute();
                        }
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error al crear vehículo: " + ex.getMessage());
		}
	}

	public void showDetail(VehiculoDTO dto) {
		if (dto == null) {
			return;
		}
		VehiculoDetailDialog dlg = new VehiculoDetailDialog(frame, dto);
		dlg.setVisible(true);
	}

        public void showEdit(VehiculoDTO dto, ActionCallback onReload) throws RentexpresException {
		if (dto == null) {
			return;
		}
		VehiculoEditDialog dlg = new VehiculoEditDialog(frame, dto, categoriaService.findAll(),
				estadoService.findAll());
		dlg.setVisible(true);
		if (!dlg.isConfirmed()) {
			return;
		}
		try {
			vehiculoService.update(dlg.getVehiculo(), null);
                        if (onReload != null) {
                                onReload.execute();
                        }
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error al actualizar vehículo: " + ex.getMessage());
		}
	}

	public void loadByCriteria(VehiculoCriteria criteria) {
		try {
			Results<VehiculoDTO> results = vehiculoService.findByCriteria(criteria);
			List<VehiculoDTO> list = results.getResults();
			table.setModel(new VehiculoSearchTableModel(list));
		} catch (Exception ex) {
			SwingUtils.showError(frame, "Error al buscar vehículos: " + ex.getMessage());
		}
	}

	public EstadoVehiculoService getEstadoService() {
		return estadoService;
	}

	public CategoriaVehiculoService getCategoriaService() {
		return categoriaService;
	}

	public SearchVehiculoAction getSearchAction() {
		return this;
	}
}
