package com.pinguela.rentexpres.desktop.view;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.controller.ClienteSearchController;
import com.pinguela.rentexpres.desktop.util.ActionCallback;
import com.pinguela.rentexpres.service.ClienteService;
import com.pinguela.rentexpres.service.LocalidadService;
import com.pinguela.rentexpres.service.ProvinciaService;

/**
 * Vista principal de búsqueda de clientes. Integra: 1) ClienteFilterPanel
 * (parte superior) 2) ClienteSearchActionsView (inmediatamente tras el filtro)
 * 3) ClienteTablePanel (en el centro, dentro de JScrollPane) 4) PaginationPanel
 * (parte inferior)
 */
public class ClienteSearchView
                extends StandardSearchView<ClienteFilterPanel, ClienteSearchActionsView, ClienteTablePanel> {
        private static final long serialVersionUID = 1L;

        private ClienteSearchController controller = null;
        private boolean initialized = false;

	/**
	 * Constructor. Crea subcomponentes y arranca el controlador.
	 *
	 * @param clienteService   servicio de negocio de Cliente
	 * @param provinciaService servicio para cargar Provincias
	 * @param localidadService servicio para cargar Localidades
	 * @param owner            Frame propietario para diálogos
	 * @throws Exception 
	 */
        public ClienteSearchView(ClienteService clienteService, ProvinciaService provinciaService,
                        LocalidadService localidadService, Frame owner) throws Exception {

                ClienteFilterPanel filter = new ClienteFilterPanel();
                ClienteSearchActionsView actions = new ClienteSearchActionsView();
                ClienteTablePanel table = new ClienteTablePanel(clienteService, owner, new ActionCallback() {
                        @Override
                        public void execute() {
                                controller.buscar();
                        }
                });

                super(filter, actions, table);

                // El botón “Seleccionar” del filtro: al pulsarlo, muestra/oculta la columna de checkboxes
                filter.setToggleListener(new ActionCallback() {
                        @Override
                        public void execute() {
                                table.toggleSelectColumn();
                        }
                });

                controller = new ClienteSearchController(this, clienteService, provinciaService, localidadService, owner);
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
