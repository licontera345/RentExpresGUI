package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.pinguela.rentexpres.desktop.dialog.ReservationCreateDialog;
import com.pinguela.rentexpres.desktop.model.ReservationSearchTableModel;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.desktop.view.ReservationFilterPanel;
import com.pinguela.rentexpres.desktop.view.ReservationSearchActionsView;
import com.pinguela.rentexpres.desktop.view.ReservationSearchView;
import com.pinguela.rentexpres.desktop.view.ReservationTablePanel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationStatusDTO;
import com.pinguela.rentexpres.model.ReservationCriteria;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.ReservationStatusService;
import com.pinguela.rentexpres.service.ReservationService;
import com.pinguela.rentexpres.service.VehicleService;

public class ReservationSearchController {
	private static final int PAGE_SIZE = 25;
	private static final String TODAS = "Todas";
	private static final String TODOS = "Todos";

	private final ReservationSearchView view;
	private final ReservationSearchTableModel model;
	private final ReservationService reservationService;
	private final ReservationStatusService estadoService;
	private final VehicleService vehicleService;
	private final Frame frame;

	private int currentPage = 1;
	private int totalPages = 1;

	private boolean initializing = true;
	private boolean loading = false;

	public ReservationSearchController(ReservationSearchView view, ReservationService reservationService,
			ReservationStatusService estadoService, VehicleService vehicleService, Frame owner)
			throws RentexpresException {
		this.view = Objects.requireNonNull(view);
		this.reservationService = Objects.requireNonNull(reservationService);
		this.estadoService = Objects.requireNonNull(estadoService);
		this.vehicleService = Objects.requireNonNull(vehicleService);
		this.frame = Objects.requireNonNull(owner);

                Map<Integer, String> estadoMap = new java.util.LinkedHashMap<Integer, String>();
                for (ReservationStatusDTO est : CatalogCache.getEstadosReservation(estadoService)) {
                        estadoMap.put(est.getId(), est.getStatusName());
                }

		this.model = new ReservationSearchTableModel(estadoMap);
		view.getTable().setModel(model);
		wireListeners();
		cargarCombosIniciales();
		initializing = false;
	}

	public void init() {
		initializing = true;
		try {
			cargarCombosIniciales();
		} catch (RentexpresException ex) {
			SwingUtils.showError(view, "Error cargando catálogos: " + ex.getMessage());
		}
		initializing = false;
		goFirstPage();
	}

	private void wireListeners() {
		ReservationFilterPanel filter = view.getFilter();
                ReservationSearchActionsView actions = view.getActions();
                ReservationTablePanel tablePanel = view.getTable();

               filter.setOnChange(new ActionCallback() {
                       @Override
                       public void execute() {
                               if (!initializing && !loading) {
                                       goFirstPage();
                               }
                       }
               });

               filter.setToggleListener(new ActionCallback() {
                       @Override
                       public void execute() {
                               tablePanel.toggleSelectColumn();
                       }
               });

               filter.getCmbMake().addActionListener(new java.awt.event.ActionListener() {
                       @Override
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                               if (!initializing && !loading) {
                                       String make = (String) filter.getCmbMake().getSelectedItem();
                                       try {
                                               cargarModelsPorMake(make);
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error al cargar models: " + ex.getMessage());
                                       }
                                       goFirstPage();
                               }
                       }
               });

               view.getPager().onPrev(new PaginationPanel.OnPagerListener() {
                       @Override
                       public void onAction() {
                               if (!loading && currentPage > 1) {
                                       currentPage--;
                                       buscar();
                               }
                       }
               });
               view.getPager().onNext(new PaginationPanel.OnPagerListener() {
                       @Override
                       public void onAction() {
                               if (!loading && currentPage < totalPages) {
                                       currentPage++;
                                       buscar();
                               }
                       }
               });
               view.getPager().onFirst(new PaginationPanel.OnPagerListener() {
                       @Override
                       public void onAction() {
                               if (!loading) {
                                       goFirstPage();
                               }
                       }
               });
               view.getPager().onLast(new PaginationPanel.OnPagerListener() {
                       @Override
                       public void onAction() {
                               if (!loading && currentPage < totalPages) {
                                       currentPage = totalPages;
                                       buscar();
                               }
                       }
               });

               actions.onNuevo(new ActionCallback() {
                       @Override
                       public void execute() {
                               ReservationCreateDialog dlg = new ReservationCreateDialog(frame);
                               dlg.setVisible(true);
                               if (dlg.isConfirmed()) {
                                       try {
                                               reservationService.create(dlg.getReservation());
                                               goFirstPage();
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error guardando reservation: " + ex.getMessage());
                                       }
                               }
                       }
               });
               actions.onBuscar(new ActionCallback() {
                       @Override
                       public void execute() {
                               goFirstPage();
                       }
               });

               actions.onLimpiar(new ActionCallback() {
                       @Override
                       public void execute() {
                               filter.clear();
                               view.getTable().hideSelectColumn();
                               goFirstPage();
                       }
               });

               actions.onBorrarSeleccionados(new ActionCallback() {
                       @Override
                       public void execute() {
                               ReservationSearchTableModel m = (ReservationSearchTableModel) view.getTable().getTable().getModel();
                               List<ReservationDTO> selected = m.getSelectedItems();
                               if (selected.isEmpty())
                                       return;

                               int resp = SwingUtils.showConfirm(frame, "Delete las reservations seleccionadas?", "Confirm deletion");
                               if (resp == JOptionPane.YES_OPTION) {
                                       try {
                                               for (ReservationDTO r : selected) {
                                                       reservationService.delete(r.getId());
                                               }
                                               goFirstPage();
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error al delete reservations: " + ex.getMessage());
                                       }
                               }
                       }
               });

		view.getTable().getTable().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
					JTable tabla = view.getTable().getTable();
					int rowView = tabla.getSelectedRow();
					if (rowView < 0)
						return;
					int rowModel = tabla.convertRowIndexToModel(rowView);
					ReservationDTO sel = model.getReservationAt(rowModel);
					if (sel != null) {
					
					}
				}
			}
		});
	}

	private void cargarCombosIniciales() throws RentexpresException {
		ReservationFilterPanel filter = view.getFilter();

		JComboBox<ReservationStatusDTO> cmbEstado = filter.getCmbEstado();
		cmbEstado.removeAllItems();
		ReservationStatusDTO todosEstado = new ReservationStatusDTO();
		todosEstado.setId(null);
		todosEstado.setStatusName("Todos");
		cmbEstado.addItem(todosEstado);
                for (ReservationStatusDTO e : CatalogCache.getEstadosReservation(estadoService)) {
                        cmbEstado.addItem(e);
                }
		cmbEstado.setRenderer(new DefaultListCellRenderer());
		cmbEstado.setSelectedIndex(0);

		JComboBox<String> cmbMake = filter.getCmbMake();
		cmbMake.removeAllItems();
		cmbMake.addItem(TODAS);
               java.util.Set<String> makesSet = new java.util.HashSet<String>();
               for (com.pinguela.rentexpres.model.VehicleDTO v : CatalogCache.getVehicles(vehicleService)) {
                       makesSet.add(v.getMake());
               }
               java.util.List<String> makesList = new java.util.ArrayList<String>(makesSet);
               java.util.Collections.sort(makesList);
               for (String m : makesList) {
                       cmbMake.addItem(m);
               }
		cmbMake.setSelectedIndex(0);

		cargarModelsPorMake(null);
	}

	private void cargarModelsPorMake(String make) throws RentexpresException {
		ReservationFilterPanel filter = view.getFilter();
		JComboBox<String> cmbModel = filter.getCmbModel();
		cmbModel.removeAllItems();
		cmbModel.addItem(TODOS);
               java.util.Set<String> modelsSet = new java.util.HashSet<String>();
               for (com.pinguela.rentexpres.model.VehicleDTO v : CatalogCache.getVehicles(vehicleService)) {
                       if (make == null || make.equals(TODAS) || make.equals(v.getMake())) {
                               modelsSet.add(v.getModel());
                       }
               }
               java.util.List<String> modelsList = new java.util.ArrayList<String>(modelsSet);
               java.util.Collections.sort(modelsList);
               for (String m : modelsList) {
                       cmbModel.addItem(m);
               }
		cmbModel.setSelectedIndex(0);
	}

	private ReservationCriteria buildCriteria() {
		ReservationFilterPanel f = view.getFilter();
		ReservationCriteria c = new ReservationCriteria();

		if (f.getReservationId() != null && f.getReservationId() > 0) {
			c.setId(f.getReservationId());
		}
		if (f.getVehicleId() != null && f.getVehicleId() > 0) {
			c.setVehicleId(f.getVehicleId());
		}
		if (f.getCustomerId() != null && f.getCustomerId() > 0) {
			c.setCustomerId(f.getCustomerId());
		}
		if (f.getStartDate() != null && !f.getStartDate().trim().isEmpty()) {
			c.setStartDate(f.getStartDate());
		}
		if (f.getEndDate() != null && !f.getEndDate().trim().isEmpty()) {
			c.setEndDate(f.getEndDate());
		}
		String make = f.getMake();
		if (make != null && !make.equals(TODAS)) {
			c.setMake(make);
		}
		String model = f.getModel();
		if (model != null && !model.equals(TODOS)) {
			c.setModel(model);
		}
		if (f.getDailyPrice() != null) {
			c.setDailyPrice(f.getDailyPrice().doubleValue());
		}
		if (f.getName() != null && !f.getName().trim().isEmpty()) {
			c.setName(f.getName());
		}
		if (f.getLastName() != null && !f.getLastName().trim().isEmpty()) {
			c.setLastName(f.getLastName());
		}
		if (f.getPhone() != null && !f.getPhone().trim().isEmpty()) {
			c.setPhone(f.getPhone());
		}
		ReservationStatusDTO estSel = f.getEstadoSeleccionado();
		if (estSel != null && estSel.getId() != null) {
			c.setReservationIdStatus(estSel.getId());
		}

		return c;
	}

	public void buscar() {
		if (loading)
			return;
		loading = true;
		try {
			ReservationCriteria crit = buildCriteria();
			crit.setPageNumber(currentPage);
			crit.setPageSize(PAGE_SIZE);

			Results<ReservationDTO> res = reservationService.findByCriteria(crit);
			totalPages = Math.max(1, (int) Math.ceil(res.getTotalRecords() / (double) PAGE_SIZE));

			model.setReservations(res.getResults());
			view.getPager().setInfo(currentPage, totalPages);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error al buscar reservations: " + ex.getMessage());
		} finally {
			loading = false;
		}
	}

	public void goFirstPage() {
		currentPage = 1;
		buscar();
	}
}
	