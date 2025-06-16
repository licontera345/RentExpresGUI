package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.pinguela.rentexpres.desktop.controller.ClienteSearchController;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.service.ClienteService;
import com.pinguela.rentexpres.service.LocalidadService;
import com.pinguela.rentexpres.service.ProvinciaService;

/**
 * Vista principal de búsqueda de clientes. Integra: 1) ClienteFilterPanel
 * (parte superior) 2) ClienteSearchActionsView (inmediatamente tras el filtro)
 * 3) ClienteTablePanel (en el centro, dentro de JScrollPane) 4) PaginationPanel
 * (parte inferior)
 */
public class ClienteSearchView extends JPanel {
	private static final long serialVersionUID = 1L;

	private final ClienteFilterPanel filter;
	private final ClienteSearchActionsView actions;
	private final ClienteTablePanel table;
	private final PaginationPanel pager;

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

		// 2) Creamos la barra de acciones (los botones)
		this.actions = new ClienteSearchActionsView();

		// 3) Creamos la tabla de resultados (le pasamos el callback
		// controller.buscar())
		this.table = new ClienteTablePanel(clienteService, owner, () -> controller.buscar());

		// 4) Creamos el paginador
		this.pager = new PaginationPanel();

		// ——— Construimos un panel intermedio “northPanel” para filtros + acciones ———
		// Dentro de este panel:
		// BorderLayout.NORTH = filter
		// BorderLayout.SOUTH = actions
		JPanel northPanel = new JPanel(new BorderLayout(0, 4));
		northPanel.add(actions, BorderLayout.SOUTH);

		// ——— Configuramos el layout principal de ClienteSearchView ———
		setLayout(new BorderLayout(8, 8));
		add(northPanel, BorderLayout.NORTH); // FILTROS + BOTONES
		add(new JScrollPane(table), BorderLayout.CENTER); // TABLA

		// 1) Creamos el panel de filtros
		this.filter = new ClienteFilterPanel();
		table.add(filter, BorderLayout.NORTH);

		// El botón “Seleccionar” del filtro: al pulsarlo, muestra/oculta la columna de
		// checkboxes
		filter.setToggleListener(() -> table.toggleSelectColumn());
		add(pager, BorderLayout.SOUTH); // PAGINADOR

		// ——— Creamos y conectamos el controlador ———
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

	// ————— Getters para que el controlador pueda enganchar sus listeners —————

	/** @return el panel de filtros */
	public ClienteFilterPanel getFilter() {
		return filter;
	}

	/** @return la barra de botones (Nuevo/Limpiar/Eliminar) */
	public ClienteSearchActionsView getActions() {
		return actions;
	}

	/** @return el panel que contiene la JTable */
	public ClienteTablePanel getTable() {
		return table;
	}

	/** @return el componente de paginación (flechas/página) */
	public PaginationPanel getPager() {
		return pager;
	}
}
