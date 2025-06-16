package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.pinguela.rentexpres.desktop.controller.VehiculoSearchController;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.CategoriaVehiculoService;
import com.pinguela.rentexpres.service.EstadoVehiculoService;
import com.pinguela.rentexpres.service.VehiculoService;

/**
 * Vista de búsqueda de Vehículos.
 *
 * Estructura: - Barra de acciones (toolbar) en el norte - Panel central con
 * filtros (NORTH), tabla (CENTER), paginador (SOUTH) - Controlador conectado al
 * conjunto
 */
public class VehiculoSearchView extends JPanel {
	private static final long serialVersionUID = 1L;

	private final VehiculoFilterPanel filter;
	private final VehiculoSearchActionsView actions;
	private final VehiculoTablePanel table;
	private final PaginationPanel pager;
	private VehiculoSearchController controller;
	private boolean initialized = false;

	/**
	 * Constructor. Crea los paneles y asigna el controlador.
	 *
	 * @param vs    VehiculoService para obtener datos de vehículos
	 * @param cs    CategoriaVehiculoService para llenar combo de categorías
	 * @param ess   EstadoVehiculoService para llenar combo de estados
	 * @param owner Frame padre (para modales)
	 * @throws RentexpresException si falla la inicialización del controlador
	 */
	public VehiculoSearchView(VehiculoService vs, CategoriaVehiculoService cs, EstadoVehiculoService ess, Frame owner)
			throws RentexpresException {
		this.filter = new VehiculoFilterPanel();
		this.actions = new VehiculoSearchActionsView();
		this.pager = new PaginationPanel();

		// POR AHORA pasamos null como SearchVehiculoAction; luego lo ligaremos desde el
		// controlador:
		this.table = new VehiculoTablePanel(null, vs);

		// Barra de herramientas con acciones
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		bar.add(actions);

		// Panel central: filtros arriba, tabla en el centro, paginador abajo
		JPanel main = new JPanel(new BorderLayout());
		main.add(filter, BorderLayout.NORTH);
		main.add(new JScrollPane(table), BorderLayout.CENTER);
		main.add(pager, BorderLayout.SOUTH);

		// Layout principal: un pequeño margen de 8px entre componentes
		setLayout(new BorderLayout(8, 8));
		add(bar, BorderLayout.NORTH);
		add(main, BorderLayout.CENTER);

		// Inicializar controlador
		controller = new VehiculoSearchController(this, vs, cs, ess, owner);

		// Ahora que el controlador existe, pasarle su SearchVehiculoAction al panel de
		// tabla:
		table.setSearchAction(controller.getSearchAction());

		// Conectar botón "Limpiar filtros" de actions
               actions.onLimpiar(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               filter.clear();
                               table.hideSelectColumn();
                               controller.goFirstPage();
                       }
               });

		// Conectar botón "Eliminar seleccionados" de actions
               actions.onBorrarSeleccionados(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               controller.onEliminarSeleccionados();
                       }
               });

		// Conectar botón "Nuevo" de actions
               actions.onNuevo(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               controller.onNuevoVehiculo();
                       }
               });

		// Los filtros invocan búsqueda automáticamente (por eso no hay botón "Buscar")
               filter.setOnChange(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               controller.goFirstPage();
                       }
               });

		// Conectar botón "Seleccionar" del filtro para mostrar/ocultar columna de
		// selección
               filter.setToggleListener(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               table.toggleSelectColumn();
                       }
               });
	}

	/**
	 * Inicializa el controlador solo la primera vez.
	 */
	public void initIfNeeded() {
		if (!initialized) {
			controller.init();
			initialized = true;
		}
	}

	// Getters para que el controlador y otros componentes puedan acceder a cada
	// parte de la vista

	public VehiculoFilterPanel getFilter() {
		return filter;
	}

	public VehiculoSearchActionsView getActions() {
		return actions;
	}

	public VehiculoTablePanel getTable() {
		return table;
	}

	public PaginationPanel getPager() {
		return pager;
	}
}
