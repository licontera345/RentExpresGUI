package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;

import com.pinguela.rentexpres.desktop.dialog.RentalCreateDialog;
import com.pinguela.rentexpres.desktop.model.RentalSearchTableModel;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.desktop.view.RentalFilterPanel;
import com.pinguela.rentexpres.desktop.view.RentalSearchActionsView;
import com.pinguela.rentexpres.desktop.view.RentalSearchView;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalCriteria;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.RentalStatusDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.RentalService;
import com.pinguela.rentexpres.service.RentalStatusService;

/**
 * Controlador de la búsqueda de rentals. Sin lambdas ni Stream API.
 * 100 % Java 8 clásico.
 */
public class RentalSearchController {

	/* ─────────────────────────── Constantes ─────────────────────────── */
	private static final int PAGE_SIZE = 25;

	/* ─────────────────────────── Campos ─────────────────────────────── */
	private final RentalSearchView view;
	private final RentalService rentalService;
	private final RentalStatusService estadoService;
	private final RentalSearchTableModel model;
	private final Frame frame;

	private boolean initializing = true;
	private boolean loading = false;

	private int currentPage = 1;
	private int totalPages = 1;

	/* ─────────────────────────── Constructor ────────────────────────── */
	public RentalSearchController(RentalSearchView view, RentalService rentalService,
			RentalStatusService estadoService, Frame frame) throws RentexpresException {

		this.view = view;
		this.rentalService = rentalService;
		this.estadoService = estadoService;
		this.frame = frame;

		/* 1. Mapa id-estado → name (sin Stream) */
		Map<Integer, String> estadoMap = new HashMap<Integer, String>();
		List<RentalStatusDTO> listaEstados = CatalogCache.getEstadosRental(estadoService);
		for (int i = 0; i < listaEstados.size(); i++) {
			RentalStatusDTO dto = listaEstados.get(i);
			estadoMap.put(dto.getId(), dto.getStatusName());
		}

		/* 2. Model y JTable */
		this.model = new RentalSearchTableModel(estadoMap);
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
		view.getFilterPanel().setOnChange(new RentalFilterPanel.OnChangeListener() {
			public void onChange() {
				if (!initializing && !loading)
					goFirstPage();
			}
		});

		/* b) Filter – toggle columna seleccionar */
		view.getFilterPanel().setToggleListener(new RentalFilterPanel.ToggleListener() {
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
                view.getActions().onNuevo(new ActionCallback() {
                        @Override
                        public void execute() {
                                abrirNuevo();
                        }
                });
                view.getActions().onBuscar(new ActionCallback() {
                        @Override
                        public void execute() {
                                goFirstPage();
                        }
                });
                view.getActions().onLimpiar(new ActionCallback() {
                        @Override
                        public void execute() {
                                view.getFilter().clear();
                                view.getTable().hideSelectColumn();
                                goFirstPage();
                        }
                });
                view.getActions().onBorrarSeleccionados(new ActionCallback() {
                        @Override
                        public void execute() {
                                borrarSeleccionados();
                        }
                });
	}

	/*
	 * ────────────────── Auxiliar para el paginador (sin lambdas) ─────────────────
	 */
        private class PaginationPanelListener
                        implements PaginationPanel.OnPagerListener {
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
			JComboBox<RentalStatusDTO> cmb = view.getFilterPanel().getCmbEstado();
			cmb.removeAllItems();

			RentalStatusDTO todos = new RentalStatusDTO();
			todos.setId(null);
			todos.setStatusName("Todos");
			cmb.addItem(todos);

			List<RentalStatusDTO> lista = CatalogCache.getEstadosRental(estadoService);
			for (int i = 0; i < lista.size(); i++)
				cmb.addItem(lista.get(i));

			cmb.setSelectedIndex(0);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error cargando estados: " + ex.getMessage());
		}
	}

	/* ───────────────────────── Nuevo Rental ─────────────────────── */
	private void abrirNuevo() {
                RentalCreateDialog dlg = new RentalCreateDialog(frame);
                dlg.setVisible(true);
                if (!dlg.isConfirmed())
                        return;

                try {
                        RentalDTO dto = dlg.getRental();
                        if (rentalService.existsByReservation(dto.getReservationId())) {
                                SwingUtils.showWarning(view,
                                                "La reservation ya tiene un rental asignado.");
                                return;
                        }
                        rentalService.create(dto);
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
			RentalCriteria crit = buildCriteria();
			crit.setPageNumber(currentPage);
			crit.setPageSize(PAGE_SIZE);

			Results<RentalDTO> res = rentalService.findByCriteria(crit);
			totalPages = Math.max(1, (int) Math.ceil(res.getTotalRecords() / (double) PAGE_SIZE));

			model.setRentals(res.getResults());
			view.getPager().setInfo(currentPage, totalPages);

		} catch (Exception ex) {
			SwingUtils.showError(view, "Error al buscar: " + ex.getMessage());
		} finally {
			loading = false;
		}
	}

	private RentalCriteria buildCriteria() {
		RentalFilterPanel f = view.getFilterPanel();
		RentalCriteria c = new RentalCriteria();

		if (f.getIdRental() != null && f.getIdRental() > 0)
			c.setIdRental(f.getIdRental());
		if (f.getReservationId() != null && f.getReservationId() > 0)
			c.setReservationId(f.getReservationId());
		if (f.getStartDate() != null && !f.getStartDate().isEmpty())
			c.setActualStartDate(f.getStartDate());
		if (f.getEndDate() != null && !f.getEndDate().isEmpty())
			c.setActualEndDate(f.getEndDate());

		if (f.getStartKm() != null && f.getStartKm() > 0)
			c.setStartKm(f.getStartKm().intValue());
                if (f.getEndKm() != null && f.getEndKm() > 0)
                        c.setEndKm(f.getEndKm().intValue());

                RentalStatusDTO estadoSel = f.getEstadoSeleccionado();
                if (estadoSel != null && estadoSel.getId() != null)
                        c.setRentalStatusId(estadoSel.getId());

                if (f.getCosteTotal() != null)
                        c.setTotalCost(f.getCosteTotal());
                if (f.getCustomerId() != null && f.getCustomerId() > 0)
                        c.setCustomerId(f.getCustomerId());
                if (f.getName() != null && !f.getName().isEmpty())
                        c.setName(f.getName());
                if (f.getApellido() != null && !f.getApellido().isEmpty())
                        c.setLastName(f.getApellido());
                if (f.getPhone() != null && !f.getPhone().isEmpty())
                        c.setPhone(f.getPhone());

                if (f.getVehicleId() != null && f.getVehicleId() > 0)
                        c.setVehicleId(f.getVehicleId());
                if (f.getLicensePlate() != null && !f.getLicensePlate().isEmpty())
                        c.setLicensePlate(f.getLicensePlate());
                if (f.getMake() != null && !f.getMake().isEmpty())
                        c.setMake(f.getMake());
                if (f.getModel() != null && !f.getModel().isEmpty())
                        c.setModel(f.getModel());
                if (f.getDailyPrice() != null)
                        c.setDailyPrice(f.getDailyPrice());

		return c;
	}

	/* ───────────────────────── Util ─────────────────────────────── */
	public void goFirstPage() {
		currentPage = 1;
		buscar();
	}

	private void borrarSeleccionados() {
		List<RentalDTO> selected = model.getSelectedItems();
		if (selected.isEmpty())
			return;

		String msg = "Delete " + selected.size() + " rentals selected?";
		int resp = javax.swing.JOptionPane.showConfirmDialog(frame, msg, "Confirm deletion",
				javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
		if (resp != javax.swing.JOptionPane.YES_OPTION)
			return;

		StringBuilder errores = new StringBuilder();
		for (int i = 0; i < selected.size(); i++) {
			try {
				rentalService.delete(selected.get(i).getId());
			} catch (RentexpresException ex) {
				errores.append("ID ").append(selected.get(i).getId()).append(": ").append(ex.getMessage()).append("\n");
			}
		}
		if (errores.length() > 0)
			SwingUtils.showError(view, "Errores:\n" + errores.toString());
		goFirstPage();
	}
}
