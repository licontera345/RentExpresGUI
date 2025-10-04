package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.CustomerSearchController;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.service.CustomerService;
import com.pinguela.rentexpres.service.CityService;
import com.pinguela.rentexpres.service.ProvinceService;

/**
 * Vista principal de búsqueda de customers. Integra: 1) CustomerFilterPanel
 * (parte superior) 2) CustomerSearchActionsView (inmediatamente tras el filtro)
 * 3) CustomerTablePanel (en el centro, dentro de JScrollPane) 4) PaginationPanel
 * (parte inferior)
 */
public class CustomerSearchView
                extends StandardSearchView<CustomerFilterPanel, CustomerSearchActionsView, CustomerTablePanel> {
        private static final long serialVersionUID = 1L;

        private static CustomerSearchController controller = null;
        private boolean initialized = false;

	/**
	 * Constructor. Crea subcomponentes y arranca el controlador.
	 *
	 * @param customerService   servicio de negocio de Customer
	 * @param provinceService servicio para cargar Provinces
	 * @param cityService servicio para cargar Cities
	 * @param owner            Frame propietario para diálogos
	 * @throws Exception 
	 */
        public CustomerSearchView(CustomerService customerService, ProvinceService provinceService,
                        CityService cityService, Frame owner) throws Exception {

                super(new CustomerFilterPanel(), new CustomerSearchActionsView(),
                      new CustomerTablePanel(customerService, owner, new ActionCallback() {
                              @Override
                              public void execute() {
                                      controller.buscar();
                              }
                      }));

                CustomerFilterPanel filter = getFilter();
                CustomerSearchActionsView actions = getActions();
                CustomerTablePanel table = getTable();

                // El botón “Seleccionar” del filtro: al pulsarlo, muestra/oculta la columna de checkboxes
                filter.setToggleListener(new ActionCallback() {
                        @Override
                        public void execute() {
                                table.toggleSelectColumn();
                        }
                });

                controller = new CustomerSearchController(this, customerService, provinceService, cityService, owner);
        }

	/**
	 * Inicializa combos y carga la primera página. Se llama solo una vez.
	 * @throws Exception 
	 */
	public void initIfNeeded() throws Exception {
		if (!initialized) {
			controller.init();
			initialized = true;
		}
	}

        // Los getters se heredan de StandardSearchView
}
