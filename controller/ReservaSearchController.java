package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.pinguela.rentexpres.desktop.dialog.ReservaCreateDialog;
import com.pinguela.rentexpres.desktop.model.ReservaSearchTableModel;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.view.ReservaFilterPanel;
import com.pinguela.rentexpres.desktop.view.ReservaSearchActionsView;
import com.pinguela.rentexpres.desktop.view.ReservaSearchView;
import com.pinguela.rentexpres.desktop.view.ReservaTablePanel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EstadoReservaDTO;
import com.pinguela.rentexpres.model.ReservaCriteria;
import com.pinguela.rentexpres.model.ReservaDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.EstadoReservaService;
import com.pinguela.rentexpres.service.ReservaService;
import com.pinguela.rentexpres.service.VehiculoService;

public class ReservaSearchController {
	private static final int PAGE_SIZE = 25;
	private static final String TODAS = "Todas";
	private static final String TODOS = "Todos";

	private final ReservaSearchView view;
	private final ReservaSearchTableModel model;
	private final ReservaService reservaService;
	private final EstadoReservaService estadoService;
	private final VehiculoService vehiculoService;
	private final Frame frame;

	private int currentPage = 1;
	private int totalPages = 1;

	private boolean initializing = true;
	private boolean loading = false;

	public ReservaSearchController(ReservaSearchView view, ReservaService reservaService,
			EstadoReservaService estadoService, VehiculoService vehiculoService, Frame owner)
			throws RentexpresException {
		this.view = Objects.requireNonNull(view);
		this.reservaService = Objects.requireNonNull(reservaService);
		this.estadoService = Objects.requireNonNull(estadoService);
		this.vehiculoService = Objects.requireNonNull(vehiculoService);
		this.frame = Objects.requireNonNull(owner);

		Map<Integer, String> estadoMap = CatalogCache.getEstadosReserva(estadoService).stream()
				.collect(Collectors.toMap(EstadoReservaDTO::getId, EstadoReservaDTO::getNombreEstado));

		this.model = new ReservaSearchTableModel(estadoMap);
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
		ReservaFilterPanel filter = view.getFilter();
                ReservaSearchActionsView actions = view.getActions();
                ReservaTablePanel tablePanel = view.getTable();

               filter.setOnChange(new Runnable() {
                       @Override
                       public void run() {
                               if (!initializing && !loading) {
                                       goFirstPage();
                               }
                       }
               });

               filter.setToggleListener(new Runnable() {
                       @Override
                       public void run() {
                               tablePanel.toggleSelectColumn();
                       }
               });

               filter.getCmbMarca().addActionListener(new java.awt.event.ActionListener() {
                       @Override
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                               if (!initializing && !loading) {
                                       String marca = (String) filter.getCmbMarca().getSelectedItem();
                                       try {
                                               cargarModelosPorMarca(marca);
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error al cargar modelos: " + ex.getMessage());
                                       }
                                       goFirstPage();
                               }
                       }
               });

               view.getPager().onPrev(new Runnable() {
                       @Override
                       public void run() {
                               if (!loading && currentPage > 1) {
                                       currentPage--;
                                       buscar();
                               }
                       }
               });
               view.getPager().onNext(new Runnable() {
                       @Override
                       public void run() {
                               if (!loading && currentPage < totalPages) {
                                       currentPage++;
                                       buscar();
                               }
                       }
               });
               view.getPager().onFirst(new Runnable() {
                       @Override
                       public void run() {
                               if (!loading) {
                                       goFirstPage();
                               }
                       }
               });
               view.getPager().onLast(new Runnable() {
                       @Override
                       public void run() {
                               if (!loading && currentPage < totalPages) {
                                       currentPage = totalPages;
                                       buscar();
                               }
                       }
               });

               actions.onNuevo(new Runnable() {
                       @Override
                       public void run() {
                               ReservaCreateDialog dlg = new ReservaCreateDialog(frame);
                               dlg.setVisible(true);
                               if (dlg.isConfirmed()) {
                                       try {
                                               reservaService.create(dlg.getReserva());
                                               goFirstPage();
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error guardando reserva: " + ex.getMessage());
                                       }
                               }
                       }
               });

               actions.onLimpiar(new Runnable() {
                       @Override
                       public void run() {
                               filter.clear();
                               view.getTable().hideSelectColumn();
                               goFirstPage();
                       }
               });

               actions.onBorrarSeleccionados(new Runnable() {
                       @Override
                       public void run() {
                               ReservaSearchTableModel m = (ReservaSearchTableModel) view.getTable().getTable().getModel();
                               List<ReservaDTO> seleccionados = m.getSelectedItems();
                               if (seleccionados.isEmpty())
                                       return;

                               int resp = SwingUtils.showConfirm(frame, "¿Eliminar las reservas seleccionadas?", "Confirmar borrado");
                               if (resp == JOptionPane.YES_OPTION) {
                                       try {
                                               for (ReservaDTO r : seleccionados) {
                                                       reservaService.delete(r.getId());
                                               }
                                               goFirstPage();
                                       } catch (RentexpresException ex) {
                                               SwingUtils.showError(view, "Error al eliminar reservas: " + ex.getMessage());
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
					ReservaDTO sel = model.getReservaAt(rowModel);
					if (sel != null) {
					
					}
				}
			}
		});
	}

	private void cargarCombosIniciales() throws RentexpresException {
		ReservaFilterPanel filter = view.getFilter();

		JComboBox<EstadoReservaDTO> cmbEstado = filter.getCmbEstado();
		cmbEstado.removeAllItems();
		EstadoReservaDTO todosEstado = new EstadoReservaDTO();
		todosEstado.setId(null);
		todosEstado.setNombreEstado("Todos");
		cmbEstado.addItem(todosEstado);
		CatalogCache.getEstadosReserva(estadoService).forEach(cmbEstado::addItem);
		cmbEstado.setRenderer(new DefaultListCellRenderer());
		cmbEstado.setSelectedIndex(0);

		JComboBox<String> cmbMarca = filter.getCmbMarca();
		cmbMarca.removeAllItems();
		cmbMarca.addItem(TODAS);
               java.util.Set<String> marcasSet = new java.util.HashSet<String>();
               for (com.pinguela.rentexpres.model.VehiculoDTO v : CatalogCache.getVehiculos(vehiculoService)) {
                       marcasSet.add(v.getMarca());
               }
               java.util.List<String> marcasList = new java.util.ArrayList<String>(marcasSet);
               java.util.Collections.sort(marcasList);
               for (String m : marcasList) {
                       cmbMarca.addItem(m);
               }
		cmbMarca.setSelectedIndex(0);

		cargarModelosPorMarca(null);
	}

	private void cargarModelosPorMarca(String marca) throws RentexpresException {
		ReservaFilterPanel filter = view.getFilter();
		JComboBox<String> cmbModelo = filter.getCmbModelo();
		cmbModelo.removeAllItems();
		cmbModelo.addItem(TODOS);
               java.util.Set<String> modelosSet = new java.util.HashSet<String>();
               for (com.pinguela.rentexpres.model.VehiculoDTO v : CatalogCache.getVehiculos(vehiculoService)) {
                       if (marca == null || marca.equals(TODAS) || marca.equals(v.getMarca())) {
                               modelosSet.add(v.getModelo());
                       }
               }
               java.util.List<String> modelosList = new java.util.ArrayList<String>(modelosSet);
               java.util.Collections.sort(modelosList);
               for (String m : modelosList) {
                       cmbModelo.addItem(m);
               }
		cmbModelo.setSelectedIndex(0);
	}

	private ReservaCriteria buildCriteria() {
		ReservaFilterPanel f = view.getFilter();
		ReservaCriteria c = new ReservaCriteria();

		if (f.getIdReserva() != null && f.getIdReserva() > 0) {
			c.setId(f.getIdReserva());
		}
		if (f.getIdVehiculo() != null && f.getIdVehiculo() > 0) {
			c.setIdVehiculo(f.getIdVehiculo());
		}
		if (f.getIdCliente() != null && f.getIdCliente() > 0) {
			c.setIdCliente(f.getIdCliente());
		}
		if (f.getFechaInicio() != null && !f.getFechaInicio().trim().isEmpty()) {
			c.setFechaInicio(f.getFechaInicio());
		}
		if (f.getFechaFin() != null && !f.getFechaFin().trim().isEmpty()) {
			c.setFechaFin(f.getFechaFin());
		}
		String marca = f.getMarca();
		if (marca != null && !marca.equals(TODAS)) {
			c.setMarca(marca);
		}
		String modelo = f.getModelo();
		if (modelo != null && !modelo.equals(TODOS)) {
			c.setModelo(modelo);
		}
		if (f.getPrecioDia() != null) {
			c.setPrecioDia(f.getPrecioDia().doubleValue());
		}
		if (f.getNombre() != null && !f.getNombre().trim().isEmpty()) {
			c.setNombre(f.getNombre());
		}
		if (f.getApellido1() != null && !f.getApellido1().trim().isEmpty()) {
			c.setApellido1(f.getApellido1());
		}
		if (f.getTelefono() != null && !f.getTelefono().trim().isEmpty()) {
			c.setTelefono(f.getTelefono());
		}
		EstadoReservaDTO estSel = f.getEstadoSeleccionado();
		if (estSel != null && estSel.getId() != null) {
			c.setIdEstadoReserva(estSel.getId());
		}

		return c;
	}

	public void buscar() {
		if (loading)
			return;
		loading = true;
		try {
			ReservaCriteria crit = buildCriteria();
			crit.setPageNumber(currentPage);
			crit.setPageSize(PAGE_SIZE);

			Results<ReservaDTO> res = reservaService.findByCriteria(crit);
			totalPages = Math.max(1, (int) Math.ceil(res.getTotalRecords() / (double) PAGE_SIZE));

			model.setReservas(res.getResults());
			view.getPager().setInfo(currentPage, totalPages);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error al buscar reservas: " + ex.getMessage());
		} finally {
			loading = false;
		}
	}

	public void goFirstPage() {
		currentPage = 1;
		buscar();
	}
}
	