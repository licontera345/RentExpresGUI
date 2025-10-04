package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.VehicleSearchController;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.desktop.util.ActionCallback;

/**
 * Vista de búsqueda de Vehicles.
 *
 * Estructura: - Barra de acciones (toolbar) en el norte - Panel central con
 * filtros (NORTH), tabla (CENTER), paginador (SOUTH) - Controlador conectado al
 * conjunto
 */
public class VehicleSearchView
                extends StandardSearchView<VehicleFilterPanel, VehicleSearchActionsView, VehicleTablePanel> {
        private static final long serialVersionUID = 1L;

        private VehicleSearchController controller;
        private boolean initialized = false;

	/**
	 * Constructor. Crea los paneles y asigna el controlador.
	 *
	 * @param vs    VehicleService para obtener datos de vehicles
	 * @param cs    VehicleCategoryService para llenar combo de categorías
	 * @param ess   VehicleStatusService para llenar combo de estados
	 * @param owner Frame padre (para modales)
	 * @throws RentexpresException si falla la inicialización del controlador
	 */
        public VehicleSearchView(VehicleService vs, VehicleCategoryService cs, VehicleStatusService ess, Frame owner)
                        throws RentexpresException {
                super(new VehicleFilterPanel(), new VehicleSearchActionsView(),
                      new VehicleTablePanel(null, vs));

                VehicleFilterPanel filter = getFilter();
                VehicleSearchActionsView actions = getActions();
                VehicleTablePanel table = getTable();

                controller = new VehicleSearchController(this, vs, cs, ess, owner);

                table.setSearchAction(controller.getSearchAction());

               actions.onLimpiar(new ActionCallback() {
                       @Override
                       public void execute() {
                               filter.clear();
                               table.hideSelectColumn();
                               controller.goFirstPage();
                       }
               });

               actions.onBorrarSeleccionados(new ActionCallback() {
                       @Override
                       public void execute() {
                               controller.onDeleteSeleccionados();
                       }
               });

               actions.onNuevo(new ActionCallback() {
                       @Override
                       public void execute() {
                               controller.onNuevoVehicle();
                       }
               });

               filter.setOnChange(new ActionCallback() {
                       @Override
                       public void execute() {
                               controller.goFirstPage();
                       }
               });

               filter.setToggleListener(new ActionCallback() {
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
