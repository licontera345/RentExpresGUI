package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;

import com.pinguela.rentexpres.desktop.dialog.AlquilerCreateDialog;
import com.pinguela.rentexpres.desktop.model.AlquilerSearchTableModel;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.view.AlquilerFilterPanel;
import com.pinguela.rentexpres.desktop.view.AlquilerSearchActionsView;
import com.pinguela.rentexpres.desktop.view.AlquilerSearchView;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AlquilerCriteria;
import com.pinguela.rentexpres.model.AlquilerDTO;
import com.pinguela.rentexpres.model.EstadoAlquilerDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.AlquilerService;
import com.pinguela.rentexpres.service.EstadoAlquilerService;

/**
 * Controlador de la búsqueda de alquileres. Sin lambdas ni Stream API.
 * 100 % Java 8 clásico.
 */
public class AlquilerSearchController {

	/* ─────────────────────────── Constantes ─────────────────────────── */
	private static final int PAGE_SIZE = 25;

	/* ─────────────────────────── Campos ─────────────────────────────── */
	private final AlquilerSearchView view;
	private final AlquilerService alquilerService;
	private final EstadoAlquilerService estadoService;
	private final AlquilerSearchTableModel model;
	private final Frame frame;

	private boolean initializing = true;
	private boolean loading = false;

	private int currentPage = 1;
	private int totalPages = 1;

	/* ─────────────────────────── Constructor ────────────────────────── */
	public AlquilerSearchController(AlquilerSearchView view, AlquilerService alquilerService,
			EstadoAlquilerService estadoService, Frame frame) throws RentexpresException {

		this.view = view;
		this.alquilerService = alquilerService;
		this.estadoService = estadoService;
		this.frame = frame;

		/* 1. Mapa id-estado → nombre (sin Stream) */
		Map<Integer, String> estadoMap = new HashMap<Integer, String>();
		List<EstadoAlquilerDTO> listaEstados = CatalogCache.getEstadosAlquiler(estadoService);
		for (int i = 0; i < listaEstados.size(); i++) {
			EstadoAlquilerDTO dto = listaEstados.get(i);
			estadoMap.put(dto.getId(), dto.getNombreEstado());
		}

		/* 2. Modelo y JTable */
		this.model = new AlquilerSearchTableModel(estadoMap);
		view.getTable().setModel(model);

		/* 3. UI inicial */
		cargarEstados();
		wireListeners();

		initializing = false;
		goFirstPage();
	}

	/* ─────────────────────────── Listeners ─────────────────────────── */
	private void wireListeners() {

		/* a) Filter – cambios */
		view.getFilterPanel().setOnChange(new AlquilerFilterPanel.OnChangeListener() {
			public void onChange() {
				if (!initializing && !loading)
					goFirstPage();
			}
		});

		/* b) Filter – toggle columna seleccionar */
		view.getFilterPanel().setToggleListener(new AlquilerFilterPanel.ToggleListener() {
			public void onToggle() {
				view.getTable().toggleSelectColumn();
			}
		});

		/* c) Paginador */
		view.getPager().onPrev(new PaginationPanelListener(-1));
		view.getPager().onNext(new PaginationPanelListener(1));
		view.getPager().onFirst(new PaginationPanelListener(PaginationPanelListener.FIRST));
		view.getPager().onLast(new PaginationPanelListener(PaginationPanelListener.LAST));

		/* d) Botones de acciones */
		view.getActions().setNuevoListener(new AlquilerSearchActionsView.NuevoListener() {
			public void onNuevo() {
				abrirNuevo();
			}
		});
		view.getActions().setLimpiarListener(new AlquilerSearchActionsView.LimpiarListener() {
			public void onLimpiar() {
				view.getFilter().clear();
				view.getTable().hideSelectColumn();
				goFirstPage();
			}
		});
		view.getActions().setBorrarSeleccionadosListener(new AlquilerSearchActionsView.BorrarSeleccionadosListener() {
			public void onBorrarSeleccionados() {
				borrarSeleccionados();
			}
		});
	}

	/*
	 * ────────────────── Auxiliar para el paginador (sin lambdas) ─────────────────
	 */
	private class PaginationPanelListener
			implements com.pinguela.rentexpres.desktop.util.PaginationPanel.OnPagerListener {
		static final int FIRST = 9999;
		static final int LAST = -9999;
		private final int delta;

		PaginationPanelListener(int delta) {
			this.delta = delta;
		}

		public void onAction() {
			if (loading)
				return;
			if (delta == FIRST) {
				goFirstPage();
				return;
			}
			if (delta == LAST) {
				if (currentPage < totalPages) {
					currentPage = totalPages;
					buscar();
				}
				return;
			}
			int next = currentPage + delta;
			if (next >= 1 && next <= totalPages) {
				currentPage = next;
				buscar();
			}
		}
	}

	/* ──────────────────────── Combos de Estado ─────────────────────── */
	private void cargarEstados() {
		try {
			JComboBox<EstadoAlquilerDTO> cmb = view.getFilterPanel().getCmbEstado();
			cmb.removeAllItems();

			EstadoAlquilerDTO todos = new EstadoAlquilerDTO();
			todos.setId(null);
			todos.setNombreEstado("Todos");
			cmb.addItem(todos);

			List<EstadoAlquilerDTO> lista = CatalogCache.getEstadosAlquiler(estadoService);
			for (int i = 0; i < lista.size(); i++)
				cmb.addItem(lista.get(i));

			cmb.setSelectedIndex(0);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error cargando estados: " + ex.getMessage());
		}
	}

	/* ───────────────────────── Nuevo Alquiler ─────────────────────── */
	private void abrirNuevo() {
		AlquilerCreateDialog dlg = new AlquilerCreateDialog(frame);
		dlg.setVisible(true);
		if (!dlg.isConfirmed())
			return;

		try {
			alquilerService.create(dlg.getAlquiler());
			goFirstPage();
		} catch (RentexpresException ex) {
			SwingUtils.showError(view, "Error guardando: " + ex.getMessage());
		}
	}

	/* ───────────────────────── Buscar ─────────────────────────────── */
	public void buscar() {
		if (loading)
			return;
		loading = true;
		try {
			AlquilerCriteria crit = buildCriteria();
			crit.setPageNumber(currentPage);
			crit.setPageSize(PAGE_SIZE);

			Results<AlquilerDTO> res = alquilerService.findByCriteria(crit);
			totalPages = Math.max(1, (int) Math.ceil(res.getTotalRecords() / (double) PAGE_SIZE));

			model.setAlquileres(res.getResults());
			view.getPager().setInfo(currentPage, totalPages);

		} catch (Exception ex) {
			SwingUtils.showError(view, "Error al buscar: " + ex.getMessage());
		} finally {
			loading = false;
		}
	}

	private AlquilerCriteria buildCriteria() {
		AlquilerFilterPanel f = view.getFilterPanel();
		AlquilerCriteria c = new AlquilerCriteria();

		if (f.getIdAlquiler() != null && f.getIdAlquiler() > 0)
			c.setIdAlquiler(f.getIdAlquiler());
		if (f.getIdReserva() != null && f.getIdReserva() > 0)
			c.setIdReserva(f.getIdReserva());
		if (f.getFechaInicio() != null && !f.getFechaInicio().isEmpty())
			c.setFechaInicioEfectivo(f.getFechaInicio());
		if (f.getFechaFin() != null && !f.getFechaFin().isEmpty())
			c.setFechaFinEfectivo(f.getFechaFin());

		if (f.getKmInicial() != null && f.getKmInicial() > 0)
			c.setKmInicial(f.getKmInicial().intValue());
		if (f.getKmFinal() != null && f.getKmFinal() > 0)
			c.setKmFinal(f.getKmFinal().intValue());

		EstadoAlquilerDTO estadoSel = f.getEstadoSeleccionado();
		if (estadoSel != null && estadoSel.getId() != null)
			c.setIdEstadoAlquiler(estadoSel.getId());

		return c;
	}

	/* ───────────────────────── Util ─────────────────────────────── */
	public void goFirstPage() {
		currentPage = 1;
		buscar();
	}

	private void borrarSeleccionados() {
		List<AlquilerDTO> selected = model.getSelectedItems();
		if (selected.isEmpty())
			return;

		String msg = "¿Eliminar " + selected.size() + " alquileres seleccionados?";
		int resp = javax.swing.JOptionPane.showConfirmDialog(frame, msg, "Confirmar borrado",
				javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
		if (resp != javax.swing.JOptionPane.YES_OPTION)
			return;

		StringBuilder errores = new StringBuilder();
		for (int i = 0; i < selected.size(); i++) {
			try {
				alquilerService.delete(selected.get(i).getId());
			} catch (RentexpresException ex) {
				errores.append("ID ").append(selected.get(i).getId()).append(": ").append(ex.getMessage()).append("\n");
			}
		}
		if (errores.length() > 0)
			SwingUtils.showError(view, "Errores:\n" + errores.toString());
		goFirstPage();
	}
}
