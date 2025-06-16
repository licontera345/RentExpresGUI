package com.pinguela.rentexpres.desktop.view;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.pinguela.rentexpres.desktop.controller.AlquilerSearchController;
import com.pinguela.rentexpres.desktop.util.PaginationPanel;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.AlquilerService;
import com.pinguela.rentexpres.service.EstadoAlquilerService;
import com.pinguela.rentexpres.service.VehiculoService;

public class AlquilerSearchView extends JPanel {
	private static final long serialVersionUID = 1L;

	/* ───────── componentes ───────── */
	private final AlquilerFilterPanel filter = new AlquilerFilterPanel();
	private final AlquilerSearchActionsView actions = new AlquilerSearchActionsView();
	private final PaginationPanel pager = new PaginationPanel();
	private final AlquilerTablePanel table;
	private AlquilerSearchController controller;

	public AlquilerSearchView(AlquilerService alquilerSvc, EstadoAlquilerService estadoSvc, VehiculoService vehiculoSvc,
			Frame owner) throws RentexpresException {

		table = new AlquilerTablePanel(owner, alquilerSvc);

		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		bar.add(actions);

		JPanel main = new JPanel(new BorderLayout());
		main.add(filter, BorderLayout.NORTH);
		main.add(new JScrollPane(table), BorderLayout.CENTER);
		main.add(pager, BorderLayout.SOUTH);

		setLayout(new BorderLayout(8, 8));
		add(bar, BorderLayout.NORTH);
		add(main, BorderLayout.CENTER);

		controller = new AlquilerSearchController(this, alquilerSvc, estadoSvc, owner);
		table.setReloadCallback(new AlquilerTablePanel.ReloadCallback() {
			public void reload() {
				controller.buscar();
			}
		});

		/* acciones */
		actions.setLimpiarListener(new AlquilerSearchActionsView.LimpiarListener() {
			public void onLimpiar() {
				filter.clear();
				table.hideSelectColumn();
				controller.goFirstPage();
			}
		});
		filter.setToggleListener(new AlquilerFilterPanel.ToggleListener() {
			public void onToggle() {
				table.toggleSelectColumn();
			}
		});
	}

	/* ───────── getters para el controlador ───────── */
	public AlquilerFilterPanel getFilter() {
		return filter;
	}

	public AlquilerSearchActionsView getActions() {
		return actions;
	}

	public AlquilerTablePanel getTable() {
		return table;
	}

	public PaginationPanel getPager() {
		return pager;
	}

	/* ───────── alias requerido por el controlador ───────── */
	public AlquilerFilterPanel getFilterPanel() { // <-- ahora existe
		return filter;
	}
}
