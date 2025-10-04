package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.JComboBox;

import com.pinguela.rentexpres.desktop.dialog.VehicleCreateDialog;
import com.pinguela.rentexpres.desktop.model.VehicleSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.desktop.view.VehicleFilterPanel;
import com.pinguela.rentexpres.desktop.view.VehicleSearchActionsView;
import com.pinguela.rentexpres.desktop.view.VehicleSearchView;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.service.VehicleService;

public class VehicleSearchController {

        private static final int PAGE_SIZE = 25;
        private static final String TODAS = "Todas";
        private static final String TODOS = "Todos";

	private final VehicleSearchView view;
	private final VehicleService vehicleService;
	private final VehicleCategoryService categoryService;
	private final VehicleStatusService estadoService;
	private final VehicleSearchTableModel model;
	private final Frame frame;

	private boolean initializing = true;
	private boolean loading = false;

	private int currentPage = 1;
	private int totalPages = 1;

	private final SearchVehicleAction searchAction;

	public VehicleSearchController(VehicleSearchView view, VehicleService vehicleService,
			VehicleCategoryService categoryService, VehicleStatusService estadoService, Frame frame)
			throws RentexpresException {
		this.view = view;
		this.vehicleService = vehicleService;
		this.categoryService = categoryService;
		this.estadoService = estadoService;
		this.frame = frame;

		this.searchAction = new SearchVehicleAction(frame, vehicleService, categoryService, estadoService,
				view.getTable().getTable()
		);

		this.model = new VehicleSearchTableModel();
		view.getTable().getTable().setModel(model); 

                cargarEstados();
                cargarCategorys();
                cargarMakes();
                cargarModelsPorMake(null);

		wireListeners();

		initializing = false;
		goFirstPage();
	}

	private void cargarEstados() {
		try {
                        JComboBox<VehicleStatusDTO> cmb = view.getFilter().getCbEstado();
			cmb.removeAllItems();
			VehicleStatusDTO todos = new VehicleStatusDTO();
			todos.setId(null);
			todos.setStatusName("Todos");
			cmb.addItem(todos);
			for (VehicleStatusDTO e : estadoService.findAll()) {
				cmb.addItem(e);
			}
			cmb.setSelectedIndex(0);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error cargando estados: " + ex.getMessage());
		}
	}

        private void cargarCategorys() {
                try {
                        JComboBox<VehicleCategoryDTO> cmb = view.getFilter().getCbCategory();
                        cmb.removeAllItems();
                        VehicleCategoryDTO todas = new VehicleCategoryDTO();
                        todas.setId(null);
                        todas.setCategoryName("Todas");
                        cmb.addItem(todas);
                        for (VehicleCategoryDTO c : categoryService.findAll()) {
                                cmb.addItem(c);
                        }
                        cmb.setSelectedIndex(0);
                } catch (Exception ex) {
                        SwingUtils.showError(view, "Error cargando categorías: " + ex.getMessage());
                }
        }

        private void cargarMakes() throws RentexpresException {
                JComboBox<String> cmb = view.getFilter().getCmbMake();
                cmb.removeAllItems();
                cmb.addItem(TODAS);
                java.util.Set<String> makes = new java.util.HashSet<>();
                for (VehicleDTO v : CatalogCache.getVehicles(vehicleService)) {
                        makes.add(v.getMake());
                }
                java.util.List<String> lista = new java.util.ArrayList<>(makes);
                java.util.Collections.sort(lista);
                for (String m : lista) {
                        cmb.addItem(m);
                }
                cmb.setSelectedIndex(0);
        }

        private void cargarModelsPorMake(String make) throws RentexpresException {
                JComboBox<String> cmb = view.getFilter().getCmbModel();
                cmb.removeAllItems();
                cmb.addItem(TODOS);
                java.util.Set<String> models = new java.util.HashSet<>();
                for (VehicleDTO v : CatalogCache.getVehicles(vehicleService)) {
                        if (make == null || TODAS.equals(make) || make.equals(v.getMake())) {
                                models.add(v.getModel());
                        }
                }
                java.util.List<String> lista = new java.util.ArrayList<>(models);
                java.util.Collections.sort(lista);
                for (String m : lista) {
                        cmb.addItem(m);
                }
                cmb.setSelectedIndex(0);
        }

	private void wireListeners() {
                VehicleFilterPanel filtro = view.getFilter();
                filtro.setOnChange(new ActionCallback() {
                        @Override
                        public void execute() {
                                if (!initializing && !loading) {
                                        goFirstPage();
                                }
                        }
                });
                filtro.setToggleListener(new ActionCallback() {
                        @Override
                        public void execute() {
                                view.getTable().toggleSelectColumn();
                        }
                });
                filtro.setOnMakeChange(new java.util.function.Consumer<String>() {
                        @Override
                        public void accept(String make) {
                                if (!initializing && !loading) {
                                        try {
                                                cargarModelsPorMake(make);
                                        } catch (RentexpresException ex) {
                                                SwingUtils.showError(view, "Error al cargar models: " + ex.getMessage());
                                        }
                                        goFirstPage();
                                }
                        }
                });

                view.getPager().onFirst(new PaginationPanel.OnPagerListener() {
                        @Override
                        public void onAction() {
                                if (!loading)
                                        goFirstPage();
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
                view.getPager().onLast(new PaginationPanel.OnPagerListener() {
                        @Override
                        public void onAction() {
                                if (!loading && currentPage < totalPages) {
                                        currentPage = totalPages;
                                        buscar();
                                }
                        }
                });

               VehicleSearchActionsView acciones = view.getActions();
               acciones.onNuevo(new ActionCallback() {
                       @Override
                       public void execute() {
                               onNuevoVehicle();
                       }
               });
               acciones.onBuscar(new ActionCallback() {
                       @Override
                       public void execute() {
                               goFirstPage();
                       }
               });
               acciones.onLimpiar(new ActionCallback() {
                       @Override
                       public void execute() {
                               view.getFilter().clear();
                                view.getTable().hideSelectColumn();
                                goFirstPage();
                        }
                });
                acciones.onBorrarSeleccionados(new ActionCallback() {
                        @Override
                        public void execute() {
                                onDeleteSeleccionados();
                        }
                });
		
	}

	public void onNuevoVehicle() {
		try {
                        VehicleCreateDialog dlg = new VehicleCreateDialog(frame, categoryService.findAll(),
                                        estadoService.findAll(), vehicleService);
			dlg.setVisible(true);
			if (!dlg.isConfirmed()) {
				return;
			}
			vehicleService.create(dlg.getVehicle(), null);
			goFirstPage();
		} catch (RentexpresException ex) {
			SwingUtils.showError(view, "Error guardando: " + ex.getMessage());
		}
	}

	public void onDeleteSeleccionados() {
                java.util.List<Integer> ids = new java.util.ArrayList<Integer>();
                for (VehicleDTO v : model.getSelectedItems()) {
                        ids.add(v.getId());
                }
		if (ids.isEmpty()) {
			SwingUtils.showWarning(view, "No hay vehicles selected.");
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(frame, "Delete " + ids.size() + " vehicles selected?",
				"Confirm deletion múltiple", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}
		StringBuilder errores = new StringBuilder();
		for (Integer id : ids) {
			try {
				vehicleService.delete(id);
			} catch (Exception ex) {
				errores.append("ID ").append(id).append(": ").append(ex.getMessage()).append("\n");
			}
		}
		if (errores.length() > 0) {
			SwingUtils.showError(view, "Errores al delete:\n" + errores);
		}
		goFirstPage();
	}

	private VehicleCriteria buildCriteria() {
                VehicleFilterPanel f = view.getFilter();
		VehicleCriteria c = new VehicleCriteria();

                String make = f.getMake();
                if (make != null && !make.equals(TODAS)) {
                        c.setMake(make);
                }
                String model = f.getModel();
                if (model != null && !model.equals(TODOS)) {
                        c.setModel(model);
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
		VehicleStatusDTO est = f.getEstadoSeleccionado();
		if (est != null && est.getId() != null) {
			c.setVehicleStatusId(est.getId());
		}
		VehicleCategoryDTO cat = f.getCategorySeleccionada();
		if (cat != null && cat.getId() != null) {
			c.setCategoryId(cat.getId());
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
			VehicleCriteria crit = buildCriteria();
			crit.setPageNumber(currentPage);
			crit.setPageSize(PAGE_SIZE);

			Results<VehicleDTO> res = vehicleService.findByCriteria(crit);
			totalPages = (int) Math.ceil((double) res.getTotalRecords() / PAGE_SIZE);

			model.setVehicles(res.getResults());
			view.getPager().setInfo(currentPage, totalPages);
		} catch (Exception ex) {
			SwingUtils.showError(view, "Error al buscar: " + ex.getMessage());
		} finally {
			loading = false;
		}
	}

	public SearchVehicleAction getSearchAction() {
		return searchAction;
	}

	public Frame getFrame() {
		return frame;
	}
}
