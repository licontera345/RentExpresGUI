package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.VehiculoSearchController;
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
public class VehiculoSearchView
                extends StandardSearchView<VehiculoFilterPanel, VehiculoSearchActionsView, VehiculoTablePanel> {
        private static final long serialVersionUID = 1L;

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
                super(new VehiculoFilterPanel(), new VehiculoSearchActionsView(),
                      new VehiculoTablePanel(null, vs));

                VehiculoFilterPanel filter = getFilter();
                VehiculoSearchActionsView actions = getActions();
                VehiculoTablePanel table = getTable();

                controller = new VehiculoSearchController(this, vs, cs, ess, owner);

                table.setSearchAction(controller.getSearchAction());

               actions.onLimpiar(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               filter.clear();
                               table.hideSelectColumn();
                               controller.goFirstPage();
                       }
               });

               actions.onBorrarSeleccionados(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               controller.onEliminarSeleccionados();
                       }
               });

               actions.onNuevo(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               controller.onNuevoVehiculo();
                       }
               });

               filter.setOnChange(new com.pinguela.rentexpres.desktop.util.ActionCallback() {
                       @Override
                       public void execute() {
                               controller.goFirstPage();
                       }
               });

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

        // Los getters se heredan de StandardSearchView
}
