package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.JComboBox;

import com.pinguela.rentexpres.desktop.dialog.VehiculoCreateDialog;
import com.pinguela.rentexpres.desktop.model.VehiculoSearchTableModel;
import com.pinguela.rentexpres.desktop.util.SwingUtils;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.desktop.util.CatalogCache;
import com.pinguela.rentexpres.desktop.view.VehiculoFilterPanel;
import com.pinguela.rentexpres.desktop.view.VehiculoSearchActionsView;
import com.pinguela.rentexpres.desktop.view.VehiculoSearchView;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
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
        private static final String TODAS = "Todas";
        private static final String TODOS = "Todos";

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
                cargarMarcas();
                cargarModelosPorMarca(null);

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

        private void cargarMarcas() throws RentexpresException {
                JComboBox<String> cmb = view.getFilter().getCmbMarca();
                cmb.removeAllItems();
                cmb.addItem(TODAS);
                java.util.Set<String> marcas = new java.util.HashSet<>();
                for (VehiculoDTO v : CatalogCache.getVehiculos(vehiculoService)) {
                        marcas.add(v.getMarca());
                }
                java.util.List<String> lista = new java.util.ArrayList<>(marcas);
                java.util.Collections.sort(lista);
                for (String m : lista) {
                        cmb.addItem(m);
                }
                cmb.setSelectedIndex(0);
        }

        private void cargarModelosPorMarca(String marca) throws RentexpresException {
                JComboBox<String> cmb = view.getFilter().getCmbModelo();
                cmb.removeAllItems();
                cmb.addItem(TODOS);
                java.util.Set<String> modelos = new java.util.HashSet<>();
                for (VehiculoDTO v : CatalogCache.getVehiculos(vehiculoService)) {
                        if (marca == null || TODAS.equals(marca) || marca.equals(v.getMarca())) {
                                modelos.add(v.getModelo());
                        }
                }
                java.util.List<String> lista = new java.util.ArrayList<>(modelos);
                java.util.Collections.sort(lista);
                for (String m : lista) {
                        cmb.addItem(m);
                }
                cmb.setSelectedIndex(0);
        }

	private void wireListeners() {
                VehiculoFilterPanel filtro = view.getFilter();
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
                filtro.setOnMarcaChange(marca -> {
                        if (!initializing && !loading) {
                                try {
                                        cargarModelosPorMarca(marca);
                                } catch (RentexpresException ex) {
                                        SwingUtils.showError(view, "Error al cargar modelos: " + ex.getMessage());
                                }
                                goFirstPage();
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

               VehiculoSearchActionsView acciones = view.getActions();
               acciones.onNuevo(new ActionCallback() {
                       @Override
                       public void execute() {
                               onNuevoVehiculo();
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
                                onEliminarSeleccionados();
                        }
                });
		
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
                java.util.List<Integer> ids = new java.util.ArrayList<Integer>();
                for (VehiculoDTO v : model.getSelectedItems()) {
                        ids.add(v.getId());
                }
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

                String marca = f.getMarca();
                if (marca != null && !marca.equals(TODAS)) {
                        c.setMarca(marca);
                }
                String modelo = f.getModelo();
                if (modelo != null && !modelo.equals(TODOS)) {
                        c.setModelo(modelo);
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
