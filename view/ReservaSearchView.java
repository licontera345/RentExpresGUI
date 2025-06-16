package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.pinguela.rentexpres.desktop.controller.ReservaSearchController;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.EstadoReservaService;
import com.pinguela.rentexpres.service.ReservaService;
import com.pinguela.rentexpres.service.VehiculoService;

public class ReservaSearchView extends JPanel {
	private static final long serialVersionUID = 1L;

	private final ReservaFilterPanel filter;
	private final ReservaSearchActionsView actions;
	private final ReservaTablePanel table;
	private final PaginationPanel pager;
	private ReservaSearchController controller;

	private boolean initialized = false;

	public void initIfNeeded() {
		if (!initialized) {
			controller.init();
			initialized = true;
		}
	}

	public ReservaSearchView(ReservaService rs, EstadoReservaService es, VehiculoService vs, Frame owner) throws RentexpresException {
		this.filter = new ReservaFilterPanel();
		this.actions = new ReservaSearchActionsView();
		this.pager = new PaginationPanel();
		this.table = new ReservaTablePanel(rs, owner, () -> controller.buscar());

		// Barra de herramientas
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		bar.add(actions);

		// Panel central: filtros arriba, tabla en el centro, paginador abajo
		JPanel main = new JPanel(new BorderLayout());
		main.add(filter, BorderLayout.NORTH);
		main.add(new JScrollPane(table), BorderLayout.CENTER);
		main.add(pager, BorderLayout.SOUTH);

		// Layout de la vista
		setLayout(new BorderLayout(8, 8));
		add(bar, BorderLayout.NORTH);
		add(main, BorderLayout.CENTER);

		// Inicializar controlador
		controller = new ReservaSearchController(this, rs, es, vs, owner);

		// Conectar botón "Limpiar"
		actions.onLimpiar(() -> {
			filter.clear();
			table.hideSelectColumn();
			controller.goFirstPage();
		});

		// Conectar botón "Seleccionar" del filtro
		filter.setToggleListener(() -> table.toggleSelectColumn());
	}

	public ReservaFilterPanel getFilter() {
		return filter;
	}

	public ReservaSearchActionsView getActions() {
		return actions;
	}

	public ReservaTablePanel getTable() {
		return table;
	}

	public PaginationPanel getPager() {
		return pager;
	}
}
