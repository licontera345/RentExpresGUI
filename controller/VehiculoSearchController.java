package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.JComboBox;

import com.pinguela.rentexpres.desktop.dialog.VehiculoCreateDialog;
import com.pinguela.rentexpres.desktop.model.VehiculoSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.view.VehiculoFilterPanel;
import com.pinguela.rentexpres.desktop.view.VehiculoSearchActionsView;
import com.pinguela.rentexpres.desktop.view.VehiculoSearchView;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CategoriaVehiculoDTO;
import com.pinguela.rentexpres.model.EstadoVehiculoDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehiculoCriteria;
import com.pinguela.rentexpres.model.VehiculoDTO;
import com.pinguela.rentexpres.service.CategoriaVehiculoService;
import com.pinguela.rentexpres.service.EstadoVehiculoService;
import com.pinguela.rentexpres.service.VehiculoService;

public class VehiculoSearchController {

	private static final int PAGE_SIZE = 25;

	private final VehiculoSearchView view;
	private final VehiculoService vehiculoService;
	private final CategoriaVehiculoService categoriaService;
	private final EstadoVehiculoService estadoService;
	private final VehiculoSearchTableModel model;
	private final Frame frame;

	private boolean initializing = true;
	private boolean loading = false;

	private int currentPage = 1;
	private int totalPages = 1;

	private final SearchVehiculoAction searchAction;

	public VehiculoSearchController(VehiculoSearchView view, VehiculoService vehiculoService,
			CategoriaVehiculoService categoriaService, EstadoVehiculoService estadoService, Frame frame)
			throws RentexpresException {
		this.view = view;
		this.vehiculoService = vehiculoService;
		this.categoriaService = categoriaService;
		this.estadoService = estadoService;
		this.frame = frame;

		this.searchAction = new SearchVehiculoAction(frame, vehiculoService, categoriaService, estadoService,
				view.getTable().getTable()
		);

		this.model = new VehiculoSearchTableModel();
		view.getTable().getTable().setModel(model); 

		cargarEstados();
		cargarCategorias();

		wireListeners();

		initializing = false;
		goFirstPage();
	}

	private void cargarEstados() {
		try {
                        JComboBox<EstadoVehiculoDTO> cmb = view.getFilter().getCbEstado();
			cmb.removeAllItems();
			EstadoVehiculoDTO todos = new EstadoVehiculoDTO();
			todos.setId(null);
			todos.setNombreEstado("Todos");
			cmb.addItem(todos);
			for (EstadoVehiculoDTO e : estadoService.findAll()) {
				cmb.addItem(e);
			}
			cmb.setSelectedIndex(0);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error cargando estados: " + ex.getMessage());
		}
	}

	private void cargarCategorias() {
		try {
                        JComboBox<CategoriaVehiculoDTO> cmb = view.getFilter().getCbCategoria();
			cmb.removeAllItems();
			CategoriaVehiculoDTO todas = new CategoriaVehiculoDTO();
			todas.setId(null);
			todas.setNombreCategoria("Todas");
			cmb.addItem(todas);
			for (CategoriaVehiculoDTO c : categoriaService.findAll()) {
				cmb.addItem(c);
			}
			cmb.setSelectedIndex(0);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error cargando categorías: " + ex.getMessage());
		}
	}

	private void wireListeners() {
		VehiculoFilterPanel filtro = view.getFilter();
		filtro.setOnChange(() -> {
			if (!initializing && !loading) {
				goFirstPage();
			}
		});
		filtro.setToggleListener(() -> view.getTable().toggleSelectColumn());

		view.getPager().onFirst(() -> {
			if (!loading)
				goFirstPage();
		});
		view.getPager().onPrev(() -> {
			if (!loading && currentPage > 1) {
				currentPage--;
				buscar();
			}
		});
		view.getPager().onNext(() -> {
			if (!loading && currentPage < totalPages) {
				currentPage++;
				buscar();
			}
		});
		view.getPager().onLast(() -> {
			if (!loading && currentPage < totalPages) {
				currentPage = totalPages;
				buscar();
			}
		});

		VehiculoSearchActionsView acciones = view.getActions();
		acciones.onNuevo(this::onNuevoVehiculo);
		acciones.onLimpiar(() -> {
			view.getFilter().clear();
			view.getTable().hideSelectColumn();
			goFirstPage();
		});
		acciones.onBorrarSeleccionados(this::onEliminarSeleccionados);
		
	}

	public void onNuevoVehiculo() {
		try {
			VehiculoCreateDialog dlg = new VehiculoCreateDialog(frame, categoriaService.findAll(),
					estadoService.findAll());
			dlg.setVisible(true);
			if (!dlg.isConfirmed()) {
				return;
			}
			vehiculoService.create(dlg.getVehiculo(), null);
			goFirstPage();
		} catch (RentexpresException ex) {
			SwingUtils.showError(view, "Error guardando: " + ex.getMessage());
		}
	}

	public void onEliminarSeleccionados() {
		List<Integer> ids = model.getSelectedItems().stream().map(VehiculoDTO::getId).collect(Collectors.toList());
		if (ids.isEmpty()) {
			SwingUtils.showWarning(view, "No hay vehículos seleccionados.");
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(frame, "¿Eliminar " + ids.size() + " vehículos seleccionados?",
				"Confirmar eliminación múltiple", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}
		StringBuilder errores = new StringBuilder();
		for (Integer id : ids) {
			try {
				vehiculoService.delete(id);
			} catch (Exception ex) {
				errores.append("ID ").append(id).append(": ").append(ex.getMessage()).append("\n");
			}
		}
		if (errores.length() > 0) {
			SwingUtils.showError(view, "Errores al eliminar:\n" + errores);
		}
		goFirstPage();
	}

	private VehiculoCriteria buildCriteria() {
                VehiculoFilterPanel f = view.getFilter();
		VehiculoCriteria c = new VehiculoCriteria();

		if (f.getMarca() != null && !f.getMarca().isBlank()) {
			c.setMarca(f.getMarca());
		}
		if (f.getModelo() != null && !f.getModelo().isBlank()) {
			c.setModelo(f.getModelo());
		}
		if (f.getAnioDesde() != null) {
			c.setAnioDesde(f.getAnioDesde());
		}
		if (f.getAnioHasta() != null) {
			c.setAnioHasta(f.getAnioHasta());
		}
		if (f.getPrecioMax() != null) {
			c.setPrecioMax(f.getPrecioMax());
		}
		EstadoVehiculoDTO est = f.getEstadoSeleccionado();
		if (est != null && est.getId() != null) {
			c.setIdEstadoVehiculo(est.getId());
		}
		CategoriaVehiculoDTO cat = f.getCategoriaSeleccionada();
		if (cat != null && cat.getId() != null) {
			c.setIdCategoria(cat.getId());
		}
		return c;
	}

	public void goFirstPage() {
		currentPage = 1;
		buscar();
	}

	public void init() {
	}

	private void buscar() {
		if (loading) {
			return;
		}
		loading = true;
		try {
			VehiculoCriteria crit = buildCriteria();
			crit.setPageNumber(currentPage);
			crit.setPageSize(PAGE_SIZE);

			Results<VehiculoDTO> res = vehiculoService.findByCriteria(crit);
			totalPages = (int) Math.ceil((double) res.getTotalRecords() / PAGE_SIZE);

			model.setVehiculos(res.getResults());
			view.getPager().setInfo(currentPage, totalPages);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error al buscar: " + ex.getMessage());
		} finally {
			loading = false;
		}
	}

	public SearchVehiculoAction getSearchAction() {
		return searchAction;
	}

	public Frame getFrame() {
		return frame;
	}
}
