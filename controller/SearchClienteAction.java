package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.pinguela.rentexpres.desktop.dialog.ClienteDetailDialog;
import com.pinguela.rentexpres.desktop.dialog.ClienteEditDialog;
import com.pinguela.rentexpres.desktop.model.ClienteSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.model.LocalidadDTO;
import com.pinguela.rentexpres.model.ProvinciaDTO;
import com.pinguela.rentexpres.service.ClienteService;
import com.pinguela.rentexpres.service.LocalidadService;
import com.pinguela.rentexpres.service.ProvinciaService;


public class SearchClienteAction {

	private static final String ERROR_CARGANDO = "Error cargando clientes:\n";
	private static final String ERROR_ACTUALIZANDO = "Error actualizando cliente:\n";

	private final Frame frame;
	private final ClienteService clienteService;
	private final LocalidadService localidadService;
	private final ProvinciaService provinciaService;
	private final JTable table;

	private ClienteSearchTableModel model;

	public SearchClienteAction(Frame frame, ClienteService clienteService, LocalidadService localidadService,
			ProvinciaService provinciaService, JTable table) {
		this.frame = frame;
		this.clienteService = clienteService;
		this.localidadService = localidadService;
		this.provinciaService = provinciaService;
		this.table = table;
	}


	public void loadAsync() {
		new Thread(() -> {
			try {
				List<ClienteDTO> clientes = clienteService.findAll();
				Map<String, String> locMap = buildLocMap();
				Map<String, String> provMap = buildProvMap();

				model = new ClienteSearchTableModel(clientes, locMap, provMap);

				SwingUtilities.invokeLater(() -> table.setModel(model));

			} catch (RentexpresException ex) {
				SwingUtilities.invokeLater(() -> SwingUtils.showError(frame, ERROR_CARGANDO + ex.getMessage()));
			}
		}).start();
	}


	public void showDetail() {
		ClienteDTO sel = getSelected();
		if (sel != null)
			new ClienteDetailDialog(frame, sel).setVisible(true);
		else
			SwingUtils.showWarning(frame, "Selecciona un cliente para ver el detalle.");
	}

	public void showEdit() {
		ClienteDTO sel = getSelected();
		if (sel == null) {
			SwingUtils.showWarning(frame, "Selecciona un cliente para editar.");
			return;
		}

                ClienteEditDialog dlg = new ClienteEditDialog(frame, sel);
		dlg.setVisible(true);

		if (dlg.isConfirmed()) {
			updateAsync(dlg.getCliente());
		}
	}


	private ClienteDTO getSelected() {
		int row = table.getSelectedRow();
		return row < 0 ? null : model.getClienteAt(row);
	}

	private void updateAsync(ClienteDTO cliente) {
		new Thread(() -> {
			try {
				clienteService.update(cliente);
				loadAsync(); 
			} catch (RentexpresException ex) {
				SwingUtilities.invokeLater(() -> SwingUtils.showError(frame, ERROR_ACTUALIZANDO + ex.getMessage()));
			}
		}).start();
	}

	private Map<String, String> buildLocMap() throws RentexpresException {
		return localidadService.findAll().stream()
				.collect(Collectors.toMap(LocalidadDTO::getNombre, LocalidadDTO::getNombre));
	}

	private Map<String, String> buildProvMap() throws RentexpresException {
		return provinciaService.findAll().stream()
				.collect(Collectors.toMap(ProvinciaDTO::getNombre, ProvinciaDTO::getNombre));
	}
}
